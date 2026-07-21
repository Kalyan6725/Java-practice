# main.py
# This is the file you run. It uses the other files to do the full analysis.

import pandas as pd

import data_loader
import analysis
from fund import FundInvestment


def main():
    print("=" * 60)
    print("Mutual Fund Performance Analytics")
    print("=" * 60)

    # ---- Part 1: Read data ----
    funds, investors, transactions, nav_history = data_loader.read_data()

    # ---- Part 2: Clean data ----
    funds, investors, transactions, nav_history = data_loader.clean_data(
        funds, investors, transactions, nav_history
    )

    # ---- Part 3: Merge data ----
    df = data_loader.merge_data(funds, investors, transactions, nav_history)

    # ---- Part 4: New columns ----
    df = analysis.add_new_columns(df)

    print("\nMerged data (first 5 rows):")
    print(
        df[
            [
                "Investor Name",
                "Fund Name",
                "Category",
                "Investment Amount",
                "Current Value",
                "Profit",
                "ROI %",
            ]
        ].head()
    )

    # ---- Example of using the FundInvestment class from fund.py ----
    first_row = df.iloc[0]
    example = FundInvestment(
        first_row["Investor Name"],
        first_row["Fund Name"],
        first_row["Category"],
        first_row["Units Purchased"],
        first_row["Purchase NAV"],
        first_row["Latest NAV"],
    )
    print("\nExample investment (using FundInvestment class):")
    print(example)

    # ---- Part 5: NumPy tasks ----
    numpy_results, rolling_avg = analysis.numpy_tasks(nav_history)
    print("\nNumPy NAV statistics:")
    for name, value in numpy_results.items():
        print(f"  {name}: {value:.2f}")

    # ---- Part 6: Pandas analysis ----
    pa = analysis.pandas_analysis(df)
    print("\nTop 5 investors by investment amount:")
    print(pa["top_5_investors"])
    print("\nTop 5 profitable funds:")
    print(pa["top_5_funds"])
    print("\nWorst performing fund:", pa["worst_fund"])
    print("Highest NAV fund:", pa["highest_nav_fund"])
    print("Lowest NAV fund:", pa["lowest_nav_fund"])

    # ---- Part 7: GroupBy ----
    groups = analysis.groupby_analysis(df)
    print("\nCategory-wise summary:")
    print(groups["category"])
    print("\nAMC-wise summary:")
    print(groups["amc"])
    print("\nState-wise summary:")
    print(groups["state"])
    print("\nInvestor-type summary:")
    print(groups["investor_type"])

    # ---- Part 8: Detect issues ----
    issues = analysis.detect_issues(df, nav_history, transactions)
    print("\nData issue check:")
    for name, count in issues.items():
        print(f"  {name}: {count}")

    # ---- Part 9: Finance metrics ----
    df, volatility, sharpe_ratio = analysis.finance_metrics(df, nav_history)
    print(f"\nVolatility (NAV std): {volatility:.2f}")
    print(f"Sharpe Ratio: {sharpe_ratio:.2f}")

    # ---- Part 10: Export reports ----
    export_reports(df, pa, groups)

    print("\nDone! Reports saved in the 'output' folder.")


def export_reports(df, pandas_results, groups):
    """Part 10 - Export reports to the output folder."""

    # Top funds by profit -> TopFunds.xlsx
    top_funds = (
        df.groupby("Fund Name")
        .agg(
            Total_Profit=("Profit", "sum"),
            Average_ROI=("ROI %", "mean"),
            Total_Investment=("Investment Amount", "sum"),
        )
        .sort_values("Total_Profit", ascending=False)
    )
    top_funds.to_excel("output/TopFunds.xlsx")

    # Investor summary -> InvestorSummary.xlsx
    investor_summary = df.groupby("Investor Name").agg(
        Total_Investment=("Investment Amount", "sum"),
        Total_Profit=("Profit", "sum"),
        Average_ROI=("ROI %", "mean"),
    )
    investor_summary.to_excel("output/InvestorSummary.xlsx")

    # Category summary -> CategorySummary.csv
    groups["category"].to_csv("output/CategorySummary.csv")


if __name__ == "__main__":
    main()
