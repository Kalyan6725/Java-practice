"""
Credit Risk & Loan Portfolio Analysis
=====================================

Reads customers.csv, loans.csv and credit_score.csv from the data folder,
does some simple analysis, and writes the results to the output folder.

Run it with:  python main.py
"""

from data_loader import load_data
from risk_analysis import (
    numpy_statistics,
    handle_missing_values,
    remove_outliers,
    merge_data,
    add_finance_metrics,
    find_high_risk_customers,
    top_risky_customers,
    portfolio_metrics,
    generate_outputs,
)


def main():
    print("Credit Risk & Loan Portfolio Analysis")
    print("-" * 40)

    # 1. Read the files
    customers, loans, credit = load_data()

    # 2. Join them together
    print("\nMerging datasets ...")
    merged = merge_data(customers, loans, credit)

    # 3. Clean the data
    print("\nCleaning data ...")
    merged = handle_missing_values(merged)
    merged = remove_outliers(merged)

    # 4. Add the loan calculations
    merged = add_finance_metrics(merged)

    # 5. Numpy statistics
    print("\nNumPy statistics:")
    stats = numpy_statistics(merged)
    for key in stats:
        print("  ", key, ":", stats[key])

    # 6. Risk analysis
    print("\nRisk analysis ...")
    top_risky = top_risky_customers(merged, 20)
    high_risk = find_high_risk_customers(merged)
    print("  Top risky selected :", len(top_risky))
    print("  High-risk customers:", len(high_risk))

    # 7. Portfolio numbers
    print("\nPortfolio finance metrics:")
    metrics = portfolio_metrics(merged)
    for key in metrics:
        print("  ", key, ":", metrics[key])

    # 8. Write the output files
    print("\nGenerating output files ...")
    generate_outputs(merged, top_risky, high_risk, stats, metrics)

    print("\nDone.")


main()
