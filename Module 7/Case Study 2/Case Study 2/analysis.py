# analysis.py
# This file does the calculations and analysis.
# (Part 4 - New Columns, Part 5 - NumPy, Part 6 - Pandas Analysis,
#  Part 7 - GroupBy, Part 8 - Detect Issues, Part 9 - Finance Metrics)

import numpy as np
import pandas as pd


def add_new_columns(df):
    """Part 4 - Create new columns."""
    df["Investment Amount"] = df["Units Purchased"] * df["Purchase NAV"]
    df["Current Value"] = df["Units Purchased"] * df["Latest NAV"]
    df["Profit"] = df["Current Value"] - df["Investment Amount"]
    df["ROI %"] = (df["Profit"] / df["Investment Amount"]) * 100
    return df


def numpy_tasks(nav_history):
    """Part 5 - NumPy tasks on the NAV values."""
    nav_values = nav_history["NAV"].to_numpy()

    results = {
        "Average NAV": np.mean(nav_values),
        "Maximum NAV": np.max(nav_values),
        "Minimum NAV": np.min(nav_values),
        "Variance of NAV": np.var(nav_values),
        "Standard Deviation of NAV": np.std(nav_values),
    }

    # Rolling average with a window of 5 (using pandas, which is easy to read).
    rolling_avg = nav_history["NAV"].rolling(window=5).mean()

    return results, rolling_avg


def pandas_analysis(df):
    """Part 6 - Pandas analysis."""
    top_5_investors = (
        df.groupby("Investor Name")["Investment Amount"]
        .sum()
        .sort_values(ascending=False)
        .head(5)
    )

    top_5_funds = (
        df.groupby("Fund Name")["Profit"]
        .sum()
        .sort_values(ascending=False)
        .head(5)
    )

    fund_profit = df.groupby("Fund Name")["Profit"].sum()
    worst_fund = fund_profit.sort_values().index[0]

    highest_nav_fund = df.loc[df["Latest NAV"].idxmax(), "Fund Name"]
    lowest_nav_fund = df.loc[df["Latest NAV"].idxmin(), "Fund Name"]

    return {
        "top_5_investors": top_5_investors,
        "top_5_funds": top_5_funds,
        "worst_fund": worst_fund,
        "highest_nav_fund": highest_nav_fund,
        "lowest_nav_fund": lowest_nav_fund,
    }


def groupby_analysis(df):
    """Part 7 - GroupBy analysis."""

    category_summary = df.groupby("Category").agg(
        Average_ROI=("ROI %", "mean"),
        Average_NAV=("Latest NAV", "mean"),
        Total_Investment=("Investment Amount", "sum"),
    )

    amc_summary = df.groupby("AMC").agg(
        Number_of_Funds=("Fund Name", "nunique"),
        Average_NAV=("Latest NAV", "mean"),
        Total_Investment=("Investment Amount", "sum"),
    )

    state_summary = df.groupby("State").agg(
        Number_of_Investors=("Investor Name", "nunique"),
        Total_Investment=("Investment Amount", "sum"),
        Average_ROI=("ROI %", "mean"),
    )

    investor_type_summary = df.groupby("InvestorType").agg(
        Total_Investment=("Investment Amount", "sum"),
        Average_Profit=("Profit", "mean"),
    )

    return {
        "category": category_summary,
        "amc": amc_summary,
        "state": state_summary,
        "investor_type": investor_type_summary,
    }


def detect_issues(df, nav_history, transactions):
    """Part 8 - Detect data issues."""
    today = pd.Timestamp.now()

    issues = {
        "duplicate_nav_records": int(nav_history.duplicated().sum()),
        "negative_nav": int((nav_history["NAV"] < 0).sum()),
        "future_dates": int((nav_history["Date"] > today).sum()),
        "missing_fund_ids": int(transactions["FundID"].isna().sum()),
        "missing_investor_ids": int(transactions["InvestorID"].isna().sum()),
        "invalid_purchase_nav": int((transactions["PurchaseNAV"] < 0).sum()),
    }
    return issues


def finance_metrics(df, nav_history):
    """Part 9 - Finance metrics."""
    df["Absolute Return"] = df["Current Value"] - df["Investment Amount"]

    # Holding period is assumed to be 1 year, so annual return = ROI %.
    df["Annual Return %"] = df["ROI %"]

    # Volatility of NAV values.
    volatility = np.std(nav_history["NAV"].to_numpy())

    # Sharpe ratio using average ROI and a 6% risk free rate.
    risk_free_rate = 6.0
    average_return = df["ROI %"].mean()
    if volatility == 0:
        sharpe_ratio = 0
    else:
        sharpe_ratio = (average_return - risk_free_rate) / volatility

    return df, volatility, sharpe_ratio
