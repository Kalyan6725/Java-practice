"""
Case Study 1: Credit Risk & Loan Portfolio Analysis
====================================================

A bank's Risk Management team wants to identify high-risk customers and
calculate portfolio risk metrics from three input files:
    - customers.csv      (CustomerID, Age, Salary, City)
    - loans.csv          (LoanID, CustomerID, LoanAmount, InterestRate,
                          Tenure, EMI, PaidEMIs, DefaultFlag)
    - credit_scores.csv  (CustomerID, CreditScore)

This script covers the full assignment:
    Python  -> read multiple CSVs, exception handling, functions, OOP (Loan class)
    NumPy   -> mean / median / percentile / correlation / std-dev
    Pandas  -> merge, top-20 risky customers, high-risk filter
    Cleaning-> missing-value imputation + outlier removal
    Finance -> DTI, utilisation, default %, NPA %, avg EMI, expected loss
    Output  -> risk_report.xlsx, high_risk_customers.csv, summary.json
"""

from __future__ import annotations

import json
import os
from dataclasses import dataclass

import numpy as np
import pandas as pd

# --------------------------------------------------------------------------- #
# Configuration
# --------------------------------------------------------------------------- #
BASE_DIR = os.path.dirname(os.path.abspath(__file__))

CUSTOMERS_FILE = os.path.join(BASE_DIR, "customers.csv")
LOANS_FILE = os.path.join(BASE_DIR, "loans.csv")
# The brief calls it credit_score.csv; the supplied file is credit_scores.csv.
CREDIT_FILE_CANDIDATES = [
    os.path.join(BASE_DIR, "credit_scores.csv"),
    os.path.join(BASE_DIR, "credit_score.csv"),
]

# Assumption for Expected Loss = PD * EAD * LGD.
LGD = 0.45  # Loss Given Default (industry proxy for unsecured retail loans)


# --------------------------------------------------------------------------- #
# OOP - Loan class
# --------------------------------------------------------------------------- #
@dataclass
class Loan:
    """Represents a single loan and exposes derived risk measures."""

    loan_id: str
    customer_id: int
    amount: float
    interest_rate: float
    tenure: int
    emi: float
    paid_emis: int
    default_flag: int

    @property
    def remaining_emis(self) -> int:
        """Number of EMIs still to be paid."""
        return max(self.tenure - self.paid_emis, 0)

    @property
    def outstanding(self) -> float:
        """Outstanding principal proxy = remaining EMIs * EMI amount."""
        return self.remaining_emis * self.emi

    @property
    def utilisation(self) -> float:
        """Loan utilisation = outstanding / total repayable (0..1)."""
        total_repayable = self.emi * self.tenure
        if total_repayable == 0:
            return 0.0
        return self.outstanding / total_repayable

    def dti(self, monthly_income: float) -> float:
        """Debt-to-Income ratio = EMI / monthly income."""
        if monthly_income <= 0:
            return np.nan
        return self.emi / monthly_income

    def is_defaulted(self) -> bool:
        return self.default_flag == 1

    def __str__(self) -> str:  # pragma: no cover - convenience only
        return (
            f"Loan({self.loan_id}, cust={self.customer_id}, "
            f"amount={self.amount:,.0f}, default={self.default_flag})"
        )


# --------------------------------------------------------------------------- #
# Python - reading CSVs with exception handling
# --------------------------------------------------------------------------- #
def read_csv_safe(path: str, required_columns: list[str] | None = None) -> pd.DataFrame:
    """Read a CSV file with robust exception handling for corrupted/missing files.

    Raises a clear error only when the file is unusable; otherwise returns a
    validated DataFrame.
    """
    if not os.path.exists(path):
        raise FileNotFoundError(f"Input file not found: {path}")

    try:
        df = pd.read_csv(path)
    except pd.errors.EmptyDataError as exc:
        raise ValueError(f"File is empty/corrupted: {path}") from exc
    except pd.errors.ParserError as exc:
        # Retry skipping malformed rows so a few bad lines don't abort the run.
        print(f"  [WARN] Parser error in {os.path.basename(path)}; "
              f"skipping bad lines. ({exc})")
        df = pd.read_csv(path, on_bad_lines="skip")
    except UnicodeDecodeError:
        df = pd.read_csv(path, encoding="latin-1")

    if df.empty:
        raise ValueError(f"No usable rows in: {path}")

    if required_columns:
        missing = [c for c in required_columns if c not in df.columns]
        if missing:
            raise ValueError(
                f"{os.path.basename(path)} missing columns: {missing}"
            )

    return df


def resolve_credit_file() -> str:
    """Return the first credit-score file that exists."""
    for candidate in CREDIT_FILE_CANDIDATES:
        if os.path.exists(candidate):
            return candidate
    raise FileNotFoundError(
        "credit_scores.csv / credit_score.csv not found in " + BASE_DIR
    )


def load_data() -> tuple[pd.DataFrame, pd.DataFrame, pd.DataFrame]:
    """Load all three input files."""
    print("Reading input files ...")
    customers = read_csv_safe(CUSTOMERS_FILE, ["CustomerID", "Age", "Salary", "City"])
    loans = read_csv_safe(
        LOANS_FILE,
        ["LoanID", "CustomerID", "LoanAmount", "InterestRate",
         "Tenure", "EMI", "PaidEMIs", "DefaultFlag"],
    )
    credit = read_csv_safe(resolve_credit_file(), ["CustomerID", "CreditScore"])
    print(f"  customers: {len(customers)} rows")
    print(f"  loans    : {len(loans)} rows")
    print(f"  credit   : {len(credit)} rows")
    return customers, loans, credit


# --------------------------------------------------------------------------- #
# NumPy - statistics
# --------------------------------------------------------------------------- #
def numpy_statistics(df: pd.DataFrame) -> dict:
    """Compute the required NumPy statistics on the merged data."""
    loan_amount = df["LoanAmount"].to_numpy(dtype=float)
    salary = df["Salary"].to_numpy(dtype=float)
    interest = df["InterestRate"].to_numpy(dtype=float)

    stats = {
        "mean_loan_amount": float(np.mean(loan_amount)),
        "median_salary": float(np.median(salary)),
        "interest_rate_25th_percentile": float(np.percentile(interest, 25)),
        "interest_rate_50th_percentile": float(np.percentile(interest, 50)),
        "interest_rate_75th_percentile": float(np.percentile(interest, 75)),
        "interest_rate_90th_percentile": float(np.percentile(interest, 90)),
        "salary_loan_correlation": float(np.corrcoef(salary, loan_amount)[0, 1]),
        "loan_amount_std_dev": float(np.std(loan_amount)),
        "salary_std_dev": float(np.std(salary)),
    }
    return stats


# --------------------------------------------------------------------------- #
# Data cleaning - missing values & outliers
# --------------------------------------------------------------------------- #
def handle_missing_values(df: pd.DataFrame) -> pd.DataFrame:
    """Impute missing values per the brief.

    Salary        -> median
    CreditScore   -> mean
    InterestRate  -> previous value (forward fill)
    """
    df = df.copy()

    if df["Salary"].isna().any():
        df["Salary"] = df["Salary"].fillna(df["Salary"].median())

    if df["CreditScore"].isna().any():
        df["CreditScore"] = df["CreditScore"].fillna(df["CreditScore"].mean())

    if df["InterestRate"].isna().any():
        # Forward fill = previous value; back-fill guards a leading NaN.
        df["InterestRate"] = df["InterestRate"].ffill().bfill()

    return df


def remove_outliers(df: pd.DataFrame) -> pd.DataFrame:
    """Remove loans whose amount is above the 99th percentile."""
    threshold = np.percentile(df["LoanAmount"].to_numpy(dtype=float), 99)
    before = len(df)
    cleaned = df[df["LoanAmount"] <= threshold].copy()
    print(f"  Outlier removal: {before - len(cleaned)} rows dropped "
          f"(LoanAmount > {threshold:,.0f})")
    return cleaned


# --------------------------------------------------------------------------- #
# Pandas - merge & risk selection
# --------------------------------------------------------------------------- #
def merge_data(
    customers: pd.DataFrame, loans: pd.DataFrame, credit: pd.DataFrame
) -> pd.DataFrame:
    """Merge the three datasets on CustomerID."""
    merged = loans.merge(customers, on="CustomerID", how="left")
    merged = merged.merge(credit, on="CustomerID", how="left")
    return merged


def add_finance_metrics(df: pd.DataFrame) -> pd.DataFrame:
    """Add per-loan finance metrics using the Loan class."""
    df = df.copy()
    dti, utilisation, outstanding = [], [], []

    for row in df.itertuples(index=False):
        loan = Loan(
            loan_id=row.LoanID,
            customer_id=row.CustomerID,
            amount=row.LoanAmount,
            interest_rate=row.InterestRate,
            tenure=int(row.Tenure),
            emi=row.EMI,
            paid_emis=int(row.PaidEMIs),
            default_flag=int(row.DefaultFlag),
        )
        monthly_income = row.Salary / 12.0
        dti.append(loan.dti(monthly_income))
        utilisation.append(loan.utilisation)
        outstanding.append(loan.outstanding)

    df["MonthlyIncome"] = df["Salary"] / 12.0
    df["DTI"] = dti
    df["LoanUtilisation"] = utilisation
    df["Outstanding"] = outstanding
    return df


def find_high_risk_customers(df: pd.DataFrame) -> pd.DataFrame:
    """Customers with CreditScore<650 AND Salary<60000 AND Loan>10L AND Default=1."""
    mask = (
        (df["CreditScore"] < 650)
        & (df["Salary"] < 60000)
        & (df["LoanAmount"] > 1_000_000)
        & (df["DefaultFlag"] == 1)
    )
    return df[mask].copy()


def build_risk_score(df: pd.DataFrame) -> pd.DataFrame:
    """Rank customers by a composite risk score to get the Top 20 risky."""
    df = df.copy()

    # Normalise components to 0..1 (higher = riskier).
    credit_component = (850 - df["CreditScore"]).clip(lower=0) / 850
    dti_component = df["DTI"].clip(lower=0, upper=1)
    util_component = df["LoanUtilisation"].clip(lower=0, upper=1)
    default_component = df["DefaultFlag"].astype(float)

    df["RiskScore"] = (
        0.35 * credit_component
        + 0.25 * dti_component
        + 0.15 * util_component
        + 0.25 * default_component
    )
    return df


def top_risky_customers(df: pd.DataFrame, n: int = 20) -> pd.DataFrame:
    """Return the top-N riskiest loans/customers."""
    return df.sort_values("RiskScore", ascending=False).head(n).copy()


# --------------------------------------------------------------------------- #
# Finance - portfolio metrics
# --------------------------------------------------------------------------- #
def portfolio_metrics(df: pd.DataFrame) -> dict:
    """Compute portfolio-level finance metrics."""
    total_loans = len(df)
    defaults = int((df["DefaultFlag"] == 1).sum())

    total_outstanding = float(df["Outstanding"].sum())
    npa_outstanding = float(df.loc[df["DefaultFlag"] == 1, "Outstanding"].sum())

    default_rate = defaults / total_loans if total_loans else 0.0

    # Expected Loss = PD * EAD * LGD  (EAD proxied by outstanding balance).
    df_el = df.copy()
    df_el["ExpectedLoss"] = default_rate * df_el["Outstanding"] * LGD

    metrics = {
        "total_loans": total_loans,
        "total_defaults": defaults,
        "average_dti": float(df["DTI"].mean()),
        "average_loan_utilisation": float(df["LoanUtilisation"].mean()),
        "default_percentage": round(default_rate * 100, 2),
        "npa_percentage": round(
            (npa_outstanding / total_outstanding * 100) if total_outstanding else 0.0,
            2,
        ),
        "average_emi": float(df["EMI"].mean()),
        "total_outstanding": round(total_outstanding, 2),
        "expected_loss": round(float(df_el["ExpectedLoss"].sum()), 2),
        "loss_given_default_assumption": LGD,
    }
    return metrics


# --------------------------------------------------------------------------- #
# Automation - output generation
# --------------------------------------------------------------------------- #
def generate_outputs(
    merged: pd.DataFrame,
    top_risky: pd.DataFrame,
    high_risk: pd.DataFrame,
    stats: dict,
    metrics: dict,
) -> None:
    """Write risk_report.xlsx, high_risk_customers.csv, summary.json."""
    xlsx_path = os.path.join(BASE_DIR, "risk_report.xlsx")
    csv_path = os.path.join(BASE_DIR, "high_risk_customers.csv")
    json_path = os.path.join(BASE_DIR, "summary.json")

    # ---- risk_report.xlsx (multiple sheets) ----
    with pd.ExcelWriter(xlsx_path, engine="openpyxl") as writer:
        merged.to_excel(writer, sheet_name="Portfolio", index=False)
        top_risky.to_excel(writer, sheet_name="Top20_Risky", index=False)
        high_risk.to_excel(writer, sheet_name="High_Risk", index=False)

        stats_df = pd.DataFrame(stats.items(), columns=["Metric", "Value"])
        stats_df.to_excel(writer, sheet_name="NumPy_Stats", index=False)

        metrics_df = pd.DataFrame(metrics.items(), columns=["Metric", "Value"])
        metrics_df.to_excel(writer, sheet_name="Finance_Metrics", index=False)
    print(f"  Written: {os.path.basename(xlsx_path)}")

    # ---- high_risk_customers.csv ----
    high_risk.to_csv(csv_path, index=False)
    print(f"  Written: {os.path.basename(csv_path)}")

    # ---- summary.json ----
    summary = {
        "record_counts": {
            "total_loans_after_cleaning": int(len(merged)),
            "high_risk_customers": int(len(high_risk)),
            "top_risky_selected": int(len(top_risky)),
        },
        "numpy_statistics": stats,
        "finance_metrics": metrics,
        "top_20_risky_customer_ids": top_risky["CustomerID"].tolist(),
    }
    with open(json_path, "w", encoding="utf-8") as fh:
        json.dump(summary, fh, indent=2)
    print(f"  Written: {os.path.basename(json_path)}")


# --------------------------------------------------------------------------- #
# Orchestration
# --------------------------------------------------------------------------- #
def main() -> None:
    print("=" * 70)
    print("Credit Risk & Loan Portfolio Analysis")
    print("=" * 70)

    # 1. Load ---------------------------------------------------------------
    customers, loans, credit = load_data()

    # 2. Merge --------------------------------------------------------------
    print("\nMerging datasets ...")
    merged = merge_data(customers, loans, credit)

    # 3. Clean --------------------------------------------------------------
    print("\nCleaning data ...")
    merged = handle_missing_values(merged)
    merged = remove_outliers(merged)

    # 4. Finance metrics per loan ------------------------------------------
    merged = add_finance_metrics(merged)

    # 5. NumPy statistics ---------------------------------------------------
    print("\nNumPy statistics:")
    stats = numpy_statistics(merged)
    for key, value in stats.items():
        print(f"  {key:35s}: {value:,.4f}")

    # 6. Risk selection -----------------------------------------------------
    print("\nRisk analysis ...")
    merged = build_risk_score(merged)
    top_risky = top_risky_customers(merged, n=20)
    high_risk = find_high_risk_customers(merged)
    print(f"  Top risky selected : {len(top_risky)}")
    print(f"  High-risk customers: {len(high_risk)}")

    # 7. Portfolio finance metrics -----------------------------------------
    print("\nPortfolio finance metrics:")
    metrics = portfolio_metrics(merged)
    for key, value in metrics.items():
        print(f"  {key:35s}: {value}")

    # 8. Outputs ------------------------------------------------------------
    print("\nGenerating output files ...")
    generate_outputs(merged, top_risky, high_risk, stats, metrics)

    print("\nDone.")


if __name__ == "__main__":
    try:
        main()
    except (FileNotFoundError, ValueError) as exc:
        print(f"[ERROR] {exc}")
        raise