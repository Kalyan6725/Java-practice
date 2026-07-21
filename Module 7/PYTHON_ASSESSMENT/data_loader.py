"""
data_loader.py
--------------
Step 1 of the project: read the CSV files, clean them, and merge them.

We keep the functions small and simple so they are easy to reuse.
Data source folder: new_csv/
"""

import pandas as pd

# Folder that holds all the CSV files.
DATA_FOLDER = "new_csv"


def read_data():
    """Read the four CSV files and return them as DataFrames.

    We use a try/except so the program gives a clear message if a file
    is missing or cannot be read (Exception Handling requirement).
    """
    try:
        investors = pd.read_csv(f"{DATA_FOLDER}/investors.csv")
        funds = pd.read_csv(f"{DATA_FOLDER}/funds.csv")
        transactions = pd.read_csv(f"{DATA_FOLDER}/transactions.csv")
        nav_history = pd.read_csv(f"{DATA_FOLDER}/nav_history.csv")
    except FileNotFoundError as error:
        print("Could not find a data file:", error)
        raise
    except pd.errors.EmptyDataError as error:
        print("A data file is empty or corrupted:", error)
        raise

    return investors, funds, transactions, nav_history


def clean_data(investors, funds, transactions, nav_history):
    """Clean the four datasets.

    Steps (from the case study):
      - remove duplicate rows
      - convert date columns to real dates
      - fill missing values using the given rules
      - drop rows with negative NAV
    """
    # 1) Remove duplicate rows.
    investors = investors.drop_duplicates()
    funds = funds.drop_duplicates()
    transactions = transactions.drop_duplicates()
    nav_history = nav_history.drop_duplicates()

    # 2) Convert the date columns to datetime.
    transactions["TransactionDate"] = pd.to_datetime(
        transactions["TransactionDate"], errors="coerce"
    )
    nav_history["Date"] = pd.to_datetime(nav_history["Date"], errors="coerce")

    # 3) Fill missing values using the case study rules.
    #    AnnualIncome -> median
    investors["AnnualIncome"] = investors["AnnualIncome"].fillna(
        investors["AnnualIncome"].median()
    )
    #    RiskProfile -> "Moderate"
    investors["RiskProfile"] = investors["RiskProfile"].fillna("Moderate")
    #    ExpenseRatio -> mean
    funds["ExpenseRatio"] = funds["ExpenseRatio"].fillna(
        funds["ExpenseRatio"].mean()
    )
    #    NAV -> previous day NAV (forward fill after sorting by fund and date)
    nav_history = nav_history.sort_values(["FundID", "Date"])
    nav_history["NAV"] = nav_history.groupby("FundID")["NAV"].ffill()

    # 4) Remove rows with a negative NAV (bad data).
    nav_history = nav_history[nav_history["NAV"] >= 0]

    return investors, funds, transactions, nav_history


def get_latest_nav(nav_history):
    """Return the most recent NAV for every fund as a small DataFrame."""
    nav_sorted = nav_history.sort_values(["FundID", "Date"])
    latest = nav_sorted.groupby("FundID").tail(1)
    latest = latest[["FundID", "NAV"]].rename(columns={"NAV": "LatestNAV"})
    return latest


def merge_data(investors, funds, transactions, nav_history):
    """Merge all four datasets into one big table.

    We start from transactions (one row per trade) and add the investor
    details, the fund details, and the latest NAV of each fund.
    """
    latest_nav = get_latest_nav(nav_history)

    df = transactions.merge(investors, on="InvestorID", how="left")
    df = df.merge(funds, on="FundID", how="left")
    df = df.merge(latest_nav, on="FundID", how="left")

    return df


def remove_outliers(transactions, nav_history):
    """Remove outliers as described in the case study.

    - Transactions with Amount above the 99th percentile.
    - NAV daily changes bigger than 3 standard deviations.
    Returns the cleaned transactions and nav_history.
    """
    # 1) Amount above the 99th percentile.
    amount_limit = transactions["Amount"].quantile(0.99)
    transactions = transactions[transactions["Amount"] <= amount_limit]

    # 2) NAV day-to-day change greater than 3 standard deviations.
    nav_history = nav_history.sort_values(["FundID", "Date"])
    nav_history["NAV_Change"] = nav_history.groupby("FundID")["NAV"].diff()
    change_limit = 3 * nav_history["NAV_Change"].std()
    # Keep rows where the change is small enough (or is the first day = NaN).
    keep = (
        nav_history["NAV_Change"].isna()
        | (nav_history["NAV_Change"].abs() <= change_limit)
    )
    nav_history = nav_history[keep].drop(columns=["NAV_Change"])

    return transactions, nav_history
