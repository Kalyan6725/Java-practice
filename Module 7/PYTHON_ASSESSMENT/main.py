"""
main.py
-------
The Automated Mutual Fund Performance Dashboard.

This is the file you run. It ties everything together:
  1. Read all CSV files
  2. Clean missing values and remove duplicates
  3. Remove outliers
  4. Merge the data
  5. Calculate NumPy statistics
  6. Calculate portfolio finance metrics (FundPortfolio class)
  7. Rank funds and find high-value investors
  8. Create charts
  9. Export a text report
 10. Log the execution status

Run it with:
    python main.py
"""

import logging
import os

import pandas as pd

import data_loader
import numpy_stats
import analysis
import visualizations
from fund_portfolio import FundPortfolio

OUTPUT_FOLDER = "output"


def setup_logging():
    """Send log messages both to a file and to the screen."""
    os.makedirs(OUTPUT_FOLDER, exist_ok=True)
    logging.basicConfig(
        level=logging.INFO,
        format="%(asctime)s | %(levelname)s | %(message)s",
        handlers=[
            logging.FileHandler(f"{OUTPUT_FOLDER}/dashboard.log", mode="w"),
            logging.StreamHandler(),
        ],
    )


def write_report(lines):
    """Save the report lines to a text file in the output folder."""
    with open(f"{OUTPUT_FOLDER}/report.txt", "w", encoding="utf-8") as report_file:
        report_file.write("\n".join(lines))


def money(value):
    """Format a number as rupees for the report."""
    return f"Rs {value:,.2f}"


def main():
    setup_logging()
    logging.info("Starting the Mutual Fund Performance Dashboard.")
    report = ["MUTUAL FUND PERFORMANCE DASHBOARD REPORT", "=" * 45, ""]

    # 1) Read the data.
    investors, funds, transactions, nav_history = data_loader.read_data()
    logging.info("CSV files loaded successfully.")

    # 2) Clean the data.
    investors, funds, transactions, nav_history = data_loader.clean_data(
        investors, funds, transactions, nav_history
    )
    logging.info("Data cleaned (duplicates removed, missing values filled).")

    # 3) Remove outliers.
    transactions, nav_history = data_loader.remove_outliers(transactions, nav_history)
    logging.info("Outliers removed (Amount > 99th pct, NAV change > 3 std).")

    # 4) Merge the data.
    merged = data_loader.merge_data(investors, funds, transactions, nav_history)
    logging.info("All four datasets merged into one table.")

    # 5) NumPy statistics.
    stats = numpy_stats.calculate_statistics(investors, transactions, nav_history)
    report.append("NUMPY STATISTICS")
    report.append("-" * 45)
    for name, value in stats.items():
        report.append(f"{name}: {value:,.2f}")
    report.append("")
    logging.info("NumPy statistics calculated.")

    # 6) Finance metrics using the FundPortfolio class.
    portfolio = FundPortfolio(merged)
    as_of_date = nav_history["Date"].max()  # value everything at the latest NAV date

    report.append(f"FINANCE METRICS (as of {as_of_date.date()})")
    report.append("-" * 45)
    report.append(f"Total Portfolio Value: {money(portfolio.total_portfolio_value())}")
    report.append(f"Total Invested: {money(portfolio.total_invested())}")
    report.append(f"Absolute Return: {money(portfolio.absolute_return())}")
    report.append(f"Portfolio Return %: {portfolio.portfolio_return_pct():.2f}%")
    report.append(f"CAGR: {portfolio.cagr(as_of_date):.2f}%")
    report.append(f"Annualized Return: {portfolio.annualized_return(as_of_date):.2f}%")
    report.append(f"Average Holding Period (days): {portfolio.average_holding_period(as_of_date):.1f}")
    report.append(f"Diversification Score: {portfolio.diversification_score():.3f}")
    report.append(f"Expense Ratio Impact: {money(portfolio.expense_ratio_impact())}")
    report.append(f"Sharpe Ratio (simplified): {portfolio.sharpe_ratio():.3f}")
    report.append("")

    report.append("Category-wise Investment %")
    for category, pct in portfolio.category_wise_investment_pct().items():
        report.append(f"  {category}: {pct:.2f}%")
    report.append("")

    report.append("Fund Allocation %")
    for fund_name, pct in portfolio.fund_allocation_pct().items():
        report.append(f"  {fund_name}: {pct:.2f}%")
    report.append("")

    report.append("Investor-wise Profit / Loss")
    for name, profit in portfolio.investor_profit_loss().items():
        report.append(f"  {name}: {money(profit)}")
    report.append("")
    logging.info("Finance metrics calculated.")

    # 7) Investor and fund analysis.
    holdings = portfolio.holdings
    top20 = analysis.top_investors(holdings, top_n=20)

    report.append("TOP INVESTORS (by portfolio value)")
    report.append("-" * 45)
    for _, row in top20.iterrows():
        report.append(f"  {row['InvestorName']}: {money(row['CurrentValue'])}")
    report.append("")

    big_investment = analysis.investors_investment_above(holdings)
    report.append(f"Investors with Investment > Rs 10 Lakhs: {len(big_investment)}")
    high_risk = analysis.high_risk_investors(investors)
    report.append(f"Investors with High Risk Profile: {len(high_risk)}")
    many_tx = analysis.investors_many_transactions(transactions)
    report.append(f"Investors with more than 10 Transactions: {len(many_tx)}")
    rich = analysis.investors_income_above(investors)
    report.append(f"Investors with Annual Income > Rs 15 Lakhs: {len(rich)}")
    report.append("")
    logging.info("Investor analysis done.")

    best, worst = analysis.best_and_worst_fund(nav_history, funds)
    highest_expense = analysis.highest_expense_ratio(funds, nav_history)
    top_aum = analysis.highest_aum(holdings)
    popular = analysis.most_popular_fund(transactions, funds)

    report.append("FUND ANALYSIS")
    report.append("-" * 45)
    report.append(f"Best Performing Fund: {best['FundName']} ({best['Return%']:.2f}%)")
    report.append(f"Worst Performing Fund: {worst['FundName']} ({worst['Return%']:.2f}%)")
    report.append(f"Highest Expense Ratio: {highest_expense['FundName']} ({highest_expense['ExpenseRatio']}%)")
    report.append(f"Highest AUM: {top_aum['FundName']} ({money(top_aum['CurrentValue'])})")
    report.append(f"Most Popular Fund: {popular['FundName']} ({popular['TransactionCount']} transactions)")
    report.append("")
    logging.info("Fund analysis done.")

    # 8) Charts.
    fund_returns_table = analysis.fund_return_table(nav_history).merge(
        funds[["FundID", "Category"]], on="FundID", how="left"
    )
    category_returns = fund_returns_table.groupby("Category")["Return%"].mean()

    visualizations.portfolio_allocation_pie(holdings)
    visualizations.fund_investment_bar(holdings)
    visualizations.monthly_investment_trend(transactions)
    visualizations.category_returns_bar(category_returns)
    visualizations.nav_movement_line(nav_history)
    visualizations.top_investors_barh(top20)
    logging.info("All six charts saved to the output folder.")

    # 9) Export the report.
    write_report(report)
    logging.info("Report written to output/report.txt.")
    logging.info("Dashboard finished successfully.")


if __name__ == "__main__":
    main()
