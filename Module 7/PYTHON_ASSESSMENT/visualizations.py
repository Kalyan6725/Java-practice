"""
visualizations.py
-----------------
Make the six charts asked for in the case study and save them in output/.

We use matplotlib with the "Agg" backend so the charts are saved as PNG
files without opening any window.
"""

import matplotlib
matplotlib.use("Agg")  # save charts to files, do not open a window
import matplotlib.pyplot as plt

OUTPUT_FOLDER = "output"


def portfolio_allocation_pie(holdings):
    """Pie chart: how the money is split across funds."""
    by_fund = holdings.groupby("FundName")["NetInvested"].sum()
    plt.figure(figsize=(7, 7))
    plt.pie(by_fund, labels=by_fund.index, autopct="%1.1f%%", startangle=90)
    plt.title("Portfolio Allocation by Fund")
    plt.tight_layout()
    plt.savefig(f"{OUTPUT_FOLDER}/portfolio_allocation_pie.png")
    plt.close()


def fund_investment_bar(holdings):
    """Bar chart: total invested in each fund."""
    by_fund = holdings.groupby("FundName")["NetInvested"].sum().sort_values()
    plt.figure(figsize=(9, 5))
    plt.bar(by_fund.index, by_fund.values, color="steelblue")
    plt.title("Fund-wise Investment")
    plt.ylabel("Amount Invested")
    plt.xticks(rotation=30, ha="right")
    plt.tight_layout()
    plt.savefig(f"{OUTPUT_FOLDER}/fund_investment_bar.png")
    plt.close()


def monthly_investment_trend(transactions):
    """Line chart: total investment over time (by date)."""
    trend = (
        transactions.sort_values("TransactionDate")
        .groupby("TransactionDate")["Amount"]
        .sum()
    )
    plt.figure(figsize=(9, 5))
    plt.plot(trend.index, trend.values, marker="o", color="green")
    plt.title("Monthly Investment Trend")
    plt.xlabel("Date")
    plt.ylabel("Total Amount")
    plt.xticks(rotation=30, ha="right")
    plt.tight_layout()
    plt.savefig(f"{OUTPUT_FOLDER}/monthly_investment_trend.png")
    plt.close()


def category_returns_bar(category_returns):
    """Bar chart: average return per fund category.

    `category_returns` is a pandas Series (index = category, value = return %).
    """
    plt.figure(figsize=(8, 5))
    plt.bar(category_returns.index, category_returns.values, color="orange")
    plt.title("Category-wise Returns")
    plt.ylabel("Average Return %")
    plt.xticks(rotation=20, ha="right")
    plt.tight_layout()
    plt.savefig(f"{OUTPUT_FOLDER}/category_returns_bar.png")
    plt.close()


def nav_movement_line(nav_history):
    """Line chart: NAV movement over time for each fund."""
    plt.figure(figsize=(9, 5))
    for fund_id, group in nav_history.sort_values("Date").groupby("FundID"):
        plt.plot(group["Date"], group["NAV"], marker=".", label=fund_id)
    plt.title("NAV Movement")
    plt.xlabel("Date")
    plt.ylabel("NAV")
    plt.legend(title="Fund")
    plt.xticks(rotation=30, ha="right")
    plt.tight_layout()
    plt.savefig(f"{OUTPUT_FOLDER}/nav_movement_line.png")
    plt.close()


def top_investors_barh(top_investors_df):
    """Horizontal bar chart: top 10 investors by portfolio value."""
    top10 = top_investors_df.head(10).sort_values("CurrentValue")
    plt.figure(figsize=(9, 5))
    plt.barh(top10["InvestorName"], top10["CurrentValue"], color="purple")
    plt.title("Top 10 Investors by Portfolio Value")
    plt.xlabel("Portfolio Value")
    plt.tight_layout()
    plt.savefig(f"{OUTPUT_FOLDER}/top_investors_barh.png")
    plt.close()
