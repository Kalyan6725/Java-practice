"""
numpy_stats.py
--------------
All the NumPy calculations asked for in the case study.

Each function is small and returns a single number so it is easy to reuse.
"""

import numpy as np


def fund_returns(nav_history):
    """Return the percentage return of every fund as a NumPy array.

    Fund return = (last NAV - first NAV) / first NAV * 100
    """
    returns = []
    for fund_id, group in nav_history.sort_values("Date").groupby("FundID"):
        first_nav = group["NAV"].iloc[0]
        last_nav = group["NAV"].iloc[-1]
        if first_nav != 0:
            returns.append((last_nav - first_nav) / first_nav * 100)
    return np.array(returns)


def calculate_statistics(investors, transactions, nav_history):
    """Calculate all the NumPy statistics and return them in a dictionary."""

    # Mean Investment Amount.
    mean_investment = np.mean(transactions["Amount"])

    # Median Investor Income.
    median_income = np.median(investors["AnnualIncome"])

    # Standard Deviation of NAV.
    std_nav = np.std(nav_history["NAV"])

    # 90th and 95th Percentile of fund returns.
    returns = fund_returns(nav_history)
    percentile_90 = np.percentile(returns, 90)
    percentile_95 = np.percentile(returns, 95)

    # Correlation between Investor Income and Investment Amount.
    # First we add up how much each investor invested, then match it with income.
    invested_per_investor = transactions.groupby("InvestorID")["Amount"].sum()
    merged = invested_per_investor.reset_index().merge(
        investors[["InvestorID", "AnnualIncome"]], on="InvestorID", how="left"
    )
    if len(merged) > 1:
        correlation = np.corrcoef(merged["AnnualIncome"], merged["Amount"])[0, 1]
    else:
        correlation = float("nan")

    # Average Daily NAV (mean NAV across all funds and all days).
    average_daily_nav = np.mean(nav_history["NAV"])

    return {
        "Mean Investment Amount": mean_investment,
        "Median Investor Income": median_income,
        "Standard Deviation of NAV": std_nav,
        "90th Percentile Fund Return": percentile_90,
        "95th Percentile Fund Return": percentile_95,
        "Correlation (Income vs Investment)": correlation,
        "Average Daily NAV": average_daily_nav,
    }
