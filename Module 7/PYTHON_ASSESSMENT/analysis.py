"""
analysis.py
-----------
Pandas analysis: find important investors and rank the funds.

These functions use the "holdings" table from the FundPortfolio class and
the original tables. They return small DataFrames or simple values.
"""

# Rupee limits from the case study.
TEN_LAKHS = 1_000_000
FIFTEEN_LAKHS = 1_500_000


# ---------- Investor analysis ----------

def top_investors(holdings, top_n=20):
    """Top investors by total portfolio (current) value."""
    by_investor = (
        holdings.groupby(["InvestorID", "InvestorName"])["CurrentValue"]
        .sum()
        .reset_index()
        .sort_values("CurrentValue", ascending=False)
    )
    return by_investor.head(top_n)


def investors_investment_above(holdings, limit=TEN_LAKHS):
    """Investors whose total invested amount is above the given limit."""
    by_investor = (
        holdings.groupby(["InvestorID", "InvestorName"])["NetInvested"]
        .sum()
        .reset_index()
    )
    return by_investor[by_investor["NetInvested"] > limit]


def high_risk_investors(investors):
    """Investors with a High risk profile."""
    return investors[investors["RiskProfile"] == "High"]


def investors_many_transactions(transactions, min_count=10):
    """Investors who made more than `min_count` transactions."""
    counts = transactions.groupby("InvestorID").size().reset_index(name="TransactionCount")
    return counts[counts["TransactionCount"] > min_count]


def investors_income_above(investors, limit=FIFTEEN_LAKHS):
    """Investors whose annual income is above the given limit."""
    return investors[investors["AnnualIncome"] > limit]


# ---------- Fund analysis ----------

def fund_return_table(nav_history):
    """Return a table of each fund's percentage return (first vs last NAV)."""
    rows = []
    for fund_id, group in nav_history.sort_values("Date").groupby("FundID"):
        first_nav = group["NAV"].iloc[0]
        last_nav = group["NAV"].iloc[-1]
        if first_nav != 0:
            ret = (last_nav - first_nav) / first_nav * 100
            rows.append({"FundID": fund_id, "Return%": ret})
    import pandas as pd
    return pd.DataFrame(rows)


def best_and_worst_fund(nav_history, funds):
    """Return the best and worst performing funds (by return %)."""
    returns = fund_return_table(nav_history).merge(funds, on="FundID", how="left")
    best = returns.loc[returns["Return%"].idxmax()]
    worst = returns.loc[returns["Return%"].idxmin()]
    return best, worst


def highest_expense_ratio(funds, nav_history):
    """The fund with the highest expense ratio (among funds that were traded)."""
    traded = funds[funds["FundID"].isin(nav_history["FundID"].unique())]
    return traded.loc[traded["ExpenseRatio"].idxmax()]


def highest_aum(holdings):
    """The fund with the highest Assets Under Management (sum of current value)."""
    aum = holdings.groupby(["FundID", "FundName"])["CurrentValue"].sum().reset_index()
    return aum.loc[aum["CurrentValue"].idxmax()]


def most_popular_fund(transactions, funds):
    """The fund with the most transactions."""
    counts = transactions.groupby("FundID").size().reset_index(name="TransactionCount")
    counts = counts.merge(funds, on="FundID", how="left")
    return counts.loc[counts["TransactionCount"].idxmax()]
