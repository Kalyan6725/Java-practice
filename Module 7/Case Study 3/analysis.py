# analysis.py
# This file does the calculations and analysis.
# (Part 4 - New Columns, Part 5 - NumPy, Part 6 - Pandas Analysis,
#  Part 7 - GroupBy, Part 8 - Business Rules, Part 9 - Finance Metrics)

import numpy as np
import pandas as pd

# A "Lakh" is 100,000. These make the business rules easier to read.
TWENTY_LAKHS = 2_000_000
THIRTY_LAKHS = 3_000_000


def add_new_columns(df):
    """Part 4 - Create new columns."""
    # Money already repaid and money still due.
    df["Amount Paid"] = df["Paid EMIs"] * df["EMI Amount"]
    df["EMI Due"] = df["Pending EMIs"] * df["EMI Amount"]

    # Monthly income and Debt-to-Income ratio.
    df["Monthly Income"] = df["Salary"] / 12
    df["Debt-to-Income Ratio"] = df["Loan Amount"] / df["Salary"]

    # Payment Completion % = paid EMIs out of all EMIs.
    total_emis = df["Paid EMIs"] + df["Pending EMIs"]
    df["Payment Completion %"] = (df["Paid EMIs"] / total_emis) * 100

    # A simple text status based on the pending EMIs.
    df["Payment Status"] = np.where(
        df["Pending EMIs"] == 0,
        "Paid",
        np.where(df["Paid EMIs"] == 0, "Pending", "Partial"),
    )

    return df


def numpy_tasks(df):
    """Part 5 - NumPy tasks on the Loan Amount values."""
    loan_values = df["Loan Amount"].to_numpy()

    results = {
        "Average Loan Amount": np.mean(loan_values),
        "Median Loan Amount": np.median(loan_values),
        "Maximum Loan Amount": np.max(loan_values),
        "Minimum Loan Amount": np.min(loan_values),
        "Standard Deviation": np.std(loan_values),
        "Variance": np.var(loan_values),
        "25th Percentile": np.percentile(loan_values, 25),
        "75th Percentile": np.percentile(loan_values, 75),
    }
    return results


def pandas_analysis(df):
    """Part 6 - Pandas analysis."""
    top_10_loans = (
        df[["Customer Name", "Loan Type", "Loan Amount"]]
        .sort_values("Loan Amount", ascending=False)
        .head(10)
    )

    top_10_salary = (
        df[["Customer Name", "City", "Salary"]]
        .drop_duplicates("Customer Name")
        .sort_values("Salary", ascending=False)
        .head(10)
    )

    big_loans = df[df["Loan Amount"] > TWENTY_LAKHS]

    pending_payments = df[df["Payment Status"] != "Paid"]
    fully_paid = df[df["Payment Status"] == "Paid"]

    return {
        "top_10_loans": top_10_loans,
        "top_10_salary": top_10_salary,
        "big_loans": big_loans,
        "pending_payments": pending_payments,
        "fully_paid": fully_paid,
    }


def groupby_analysis(df):
    """Part 7 - GroupBy analysis."""

    city_summary = df.groupby("City").agg(
        Number_of_Customers=("Customer Name", "nunique"),
        Average_Salary=("Salary", "mean"),
        Total_Loan_Amount=("Loan Amount", "sum"),
    )

    loan_type_summary = df.groupby("Loan Type").agg(
        Number_of_Loans=("LoanID", "count"),
        Average_Loan_Amount=("Loan Amount", "mean"),
        Total_Loan_Amount=("Loan Amount", "sum"),
    )

    loan_status_summary = df.groupby("Loan Status").agg(
        Number_of_Loans=("LoanID", "count"),
        Total_Loan_Amount=("Loan Amount", "sum"),
    )

    payment_status_summary = df.groupby("Payment Status").agg(
        Number_of_Loans=("LoanID", "count"),
        Total_Amount_Paid=("Amount Paid", "sum"),
    )

    return {
        "city": city_summary,
        "loan_type": loan_type_summary,
        "loan_status": loan_status_summary,
        "payment_status": payment_status_summary,
    }


def business_rules(df):
    """Part 8 - Flag risky loans using simple business rules."""
    df["Flag_HighLoan"] = df["Loan Amount"] > THIRTY_LAKHS
    df["Flag_LowSalary"] = df["Salary"] < 30_000
    df["Flag_HighDTI"] = df["Debt-to-Income Ratio"] > 5
    df["Flag_HighEMIDue"] = df["EMI Due"] > 10_000
    df["Flag_PendingPayment"] = df["Payment Status"] == "Pending"
    df["Flag_Rejected"] = df["Loan Status"] == "Rejected"

    # NOTE: The case study also mentions "Credit Score below 650", but the
    # data has no Credit Score column, so that rule is skipped.

    flag_columns = [
        "Flag_HighLoan",
        "Flag_LowSalary",
        "Flag_HighDTI",
        "Flag_HighEMIDue",
        "Flag_PendingPayment",
        "Flag_Rejected",
    ]

    # Count how many rules each loan triggers.
    df["Risk Flags"] = df[flag_columns].sum(axis=1)

    # Any loan that triggers at least one rule is "flagged".
    flagged = df[df["Risk Flags"] > 0]

    return df, flagged


def finance_metrics(df):
    """Part 9 - Finance metrics for the whole loan portfolio."""
    total_portfolio = df["Loan Amount"].sum()
    total_collected = df["Amount Paid"].sum()
    outstanding = (df["Loan Amount"] - df["Amount Paid"]).sum()

    if total_portfolio == 0:
        recovery_percent = 0
    else:
        recovery_percent = (total_collected / total_portfolio) * 100

    total_loans = len(df)
    pending_loans = int((df["Payment Status"] == "Pending").sum())
    if total_loans == 0:
        default_percent = 0
    else:
        default_percent = (pending_loans / total_loans) * 100

    metrics = {
        "Total Loan Portfolio": total_portfolio,
        "Total Amount Collected": total_collected,
        "Outstanding Amount": outstanding,
        "Loan Recovery %": recovery_percent,
        "Default %": default_percent,
        "Average EMI": df["EMI Amount"].mean(),
    }
    return metrics
