"""Analyse the loan data: stats, cleaning, risk and output files."""

import os
import json
import numpy as np
import pandas as pd

from loan import Loan

# Where to save the output files
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
OUTPUT_DIR = os.path.join(BASE_DIR, "output")

# Loss Given Default (used for expected loss)
LGD = 0.45


def numpy_statistics(df):
    # Some basic numbers using numpy
    loan_amount = df["LoanAmount"].to_numpy()
    salary = df["Salary"].to_numpy()
    interest = df["InterestRate"].to_numpy()

    stats = {
        "mean_loan_amount": float(np.mean(loan_amount)),
        "median_salary": float(np.median(salary)),
        "interest_25th_percentile": float(np.percentile(interest, 25)),
        "interest_50th_percentile": float(np.percentile(interest, 50)),
        "interest_75th_percentile": float(np.percentile(interest, 75)),
        "salary_loan_correlation": float(np.corrcoef(salary, loan_amount)[0, 1]),
        "loan_amount_std_dev": float(np.std(loan_amount)),
        "salary_std_dev": float(np.std(salary)),
    }
    return stats


def handle_missing_values(df):
    # Fill in the blank values
    df["Salary"] = df["Salary"].fillna(df["Salary"].median())
    df["CreditScore"] = df["CreditScore"].fillna(df["CreditScore"].mean())
    df["InterestRate"] = df["InterestRate"].ffill().bfill()
    return df


def remove_outliers(df):
    # Drop very large loans (above the 99th percentile)
    threshold = np.percentile(df["LoanAmount"].to_numpy(), 99)
    before = len(df)
    df = df[df["LoanAmount"] <= threshold]
    print("  Outlier removal:", before - len(df), "rows dropped")
    return df


def merge_data(customers, loans, credit):
    # Join the three tables on CustomerID
    merged = loans.merge(customers, on="CustomerID", how="left")
    merged = merged.merge(credit, on="CustomerID", how="left")
    return merged


def add_finance_metrics(df):
    # Go through each loan and calculate some numbers using the Loan class
    dti_list = []
    util_list = []
    outstanding_list = []

    for i in range(len(df)):
        row = df.iloc[i]
        loan = Loan(
            row["LoanID"],
            row["CustomerID"],
            row["LoanAmount"],
            row["InterestRate"],
            row["Tenure"],
            row["EMI"],
            row["PaidEMIs"],
            row["DefaultFlag"],
        )
        monthly_income = row["Salary"] / 12
        dti_list.append(loan.dti(monthly_income))
        util_list.append(loan.utilisation())
        outstanding_list.append(loan.outstanding())

    df["MonthlyIncome"] = df["Salary"] / 12
    df["DTI"] = dti_list
    df["LoanUtilisation"] = util_list
    df["Outstanding"] = outstanding_list
    return df


def find_high_risk_customers(df):
    # Customers who look risky
    mask = (
        (df["CreditScore"] < 650)
        & (df["Salary"] < 60000)
        & (df["LoanAmount"] > 1000000)
        & (df["DefaultFlag"] == 1)
    )
    return df[mask]


def top_risky_customers(df, n=20):
    # Sort by loan amount and take the top ones as a simple risk view
    return df.sort_values("LoanAmount", ascending=False).head(n)


def portfolio_metrics(df):
    # Overall numbers for the whole loan portfolio
    total_loans = len(df)
    defaults = int((df["DefaultFlag"] == 1).sum())

    total_outstanding = float(df["Outstanding"].sum())
    npa_outstanding = float(df[df["DefaultFlag"] == 1]["Outstanding"].sum())

    if total_loans > 0:
        default_rate = defaults / total_loans
    else:
        default_rate = 0

    expected_loss = default_rate * total_outstanding * LGD

    if total_outstanding > 0:
        npa_percentage = npa_outstanding / total_outstanding * 100
    else:
        npa_percentage = 0

    metrics = {
        "total_loans": total_loans,
        "total_defaults": defaults,
        "average_dti": float(df["DTI"].mean()),
        "average_loan_utilisation": float(df["LoanUtilisation"].mean()),
        "default_percentage": round(default_rate * 100, 2),
        "npa_percentage": round(npa_percentage, 2),
        "average_emi": float(df["EMI"].mean()),
        "total_outstanding": round(total_outstanding, 2),
        "expected_loss": round(expected_loss, 2),
    }
    return metrics


def generate_outputs(merged, top_risky, high_risk, stats, metrics):
    # Make the output folder if it is not there
    if not os.path.exists(OUTPUT_DIR):
        os.makedirs(OUTPUT_DIR)

    xlsx_path = os.path.join(OUTPUT_DIR, "risk_report.xlsx")
    csv_path = os.path.join(OUTPUT_DIR, "high_risk_customers.csv")
    json_path = os.path.join(OUTPUT_DIR, "summary.json")

    # Excel file with a few sheets
    writer = pd.ExcelWriter(xlsx_path, engine="openpyxl")
    merged.to_excel(writer, sheet_name="Portfolio", index=False)
    top_risky.to_excel(writer, sheet_name="Top20_Risky", index=False)
    high_risk.to_excel(writer, sheet_name="High_Risk", index=False)
    writer.close()
    print("  Written:", os.path.basename(xlsx_path))

    # CSV file
    high_risk.to_csv(csv_path, index=False)
    print("  Written:", os.path.basename(csv_path))

    # JSON summary
    summary = {
        "numpy_statistics": stats,
        "finance_metrics": metrics,
    }
    with open(json_path, "w") as f:
        json.dump(summary, f, indent=2)
    print("  Written:", os.path.basename(json_path))
