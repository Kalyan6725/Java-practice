# data_loader.py
# This file reads the CSV files and cleans the data.
# (Part 1 - Read Data, Part 2 - Data Cleaning, Part 3 - Merge Data)

import pandas as pd


def read_data():
    """Read all four CSV files from the data folder and return them."""
    try:
        funds = pd.read_csv("data/funds.csv")
        investors = pd.read_csv("data/investors.csv")
        transactions = pd.read_csv("data/transactions.csv")
        nav_history = pd.read_csv("data/nav_history.csv")
    except FileNotFoundError as error:
        # If a file is missing we stop the program with a clear message.
        print("Could not find a data file:", error)
        raise

    return funds, investors, transactions, nav_history


def clean_data(funds, investors, transactions, nav_history):
    """Clean the four datasets (Part 2)."""

    # 1) Remove duplicate rows from every dataset.
    funds = funds.drop_duplicates()
    investors = investors.drop_duplicates()
    transactions = transactions.drop_duplicates()
    nav_history = nav_history.drop_duplicates()

    # 2) Convert Date columns into datetime format.
    nav_history["Date"] = pd.to_datetime(nav_history["Date"], errors="coerce")
    transactions["PurchaseDate"] = pd.to_datetime(
        transactions["PurchaseDate"], errors="coerce"
    )

    # 3) Fill missing NAV using Forward Fill.
    #    We sort first so forward fill uses the previous day for the same fund.
    nav_history = nav_history.sort_values(["FundID", "Date"])
    nav_history["NAV"] = nav_history["NAV"].ffill()

    # 4) Replace missing InvestorType with "Retail".
    #    First remove extra spaces/tabs so "Corporate " and "Corporate" match.
    investors["InvestorType"] = investors["InvestorType"].str.strip()
    investors["InvestorType"] = investors["InvestorType"].fillna("Retail")

    # 5) Remove rows having negative NAV.
    nav_history = nav_history[nav_history["NAV"] >= 0]

    return funds, investors, transactions, nav_history


def get_latest_nav(nav_history):
    """Return a DataFrame with the latest NAV for each fund."""
    # Sort by date, then keep the last row for each fund (the newest NAV).
    nav_sorted = nav_history.sort_values(["FundID", "Date"])
    latest = nav_sorted.groupby("FundID").tail(1)
    latest = latest[["FundID", "NAV"]].rename(columns={"NAV": "LatestNAV"})
    return latest


def merge_data(funds, investors, transactions, nav_history):
    """Merge all four datasets into one DataFrame (Part 3)."""

    latest_nav = get_latest_nav(nav_history)

    # Start from transactions and join the other tables one by one.
    df = transactions.merge(investors, on="InvestorID", how="left")
    df = df.merge(funds, on="FundID", how="left")
    df = df.merge(latest_nav, on="FundID", how="left")

    # Keep and rename only the columns the case study asks for.
    df = df.rename(
        columns={
            "InvestorName": "Investor Name",
            "FundName": "Fund Name",
            "AMC": "AMC",
            "State": "State",
            "UnitsPurchased": "Units Purchased",
            "PurchaseNAV": "Purchase NAV",
            "LatestNAV": "Latest NAV",
        }
    )

    return df
