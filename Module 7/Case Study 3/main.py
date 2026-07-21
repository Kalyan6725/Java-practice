# main.py
# This is the file you run. It uses the other files to do the full analysis.
#
# Run it with:  python main.py

import data_loader
import analysis
from loan import Loan


def main():
    print("=" * 60)
    print("Loan Processing & Loan Repayment Analytics")
    print("=" * 60)

    # ---- Part 1: Read data ----
    customers, loans, payments = data_loader.read_data()

    # ---- Part 2: Clean data ----
    customers, loans, payments = data_loader.clean_data(customers, loans, payments)

    # ---- Part 3: Merge data ----
    df = data_loader.merge_data(customers, loans, payments)

    # ---- Part 4: New columns ----
    df = analysis.add_new_columns(df)

    print("\nMerged data (first 5 rows):")
    print(
        df[
            [
                "Customer Name",
                "City",
                "Loan Type",
                "Loan Amount",
                "Loan Status",
                "EMI Amount",
                "Payment Status",
            ]
        ].head()
    )

    # ---- Example of using the Loan class from loan.py ----
    first_row = df.iloc[0]
    example = Loan(
        first_row["Customer Name"],
        first_row["City"],
        first_row["Loan Type"],
        first_row["Loan Amount"],
        first_row["Salary"],
        first_row["EMI Amount"],
        first_row["Paid EMIs"],
        first_row["Pending EMIs"],
        first_row["Loan Status"],
    )
    print("\nExample loan (using Loan class):")
    print(example)

    # ---- Part 5: NumPy tasks ----
    numpy_results = analysis.numpy_tasks(df)
    print("\nNumPy Loan Amount statistics:")
    for name, value in numpy_results.items():
        print(f"  {name}: {value:,.2f}")

    # ---- Part 6: Pandas analysis ----
    pa = analysis.pandas_analysis(df)
    print("\nTop 10 highest loan customers:")
    print(pa["top_10_loans"].to_string(index=False))
    print("\nTop 10 customers by salary:")
    print(pa["top_10_salary"].to_string(index=False))
    print(f"\nLoans above 20 Lakhs: {len(pa['big_loans'])}")
    print(f"Loans with pending payments: {len(pa['pending_payments'])}")
    print(f"Fully paid loans: {len(pa['fully_paid'])}")

    # ---- Part 7: GroupBy ----
    groups = analysis.groupby_analysis(df)
    print("\nCity-wise summary:")
    print(groups["city"])
    print("\nLoan-type summary:")
    print(groups["loan_type"])
    print("\nLoan-status summary:")
    print(groups["loan_status"])
    print("\nPayment-status summary:")
    print(groups["payment_status"])

    # ---- Part 8: Business rules ----
    df, flagged = analysis.business_rules(df)
    print(f"\nFlagged (risky) loans: {len(flagged)} out of {len(df)}")

    # ---- Part 9: Finance metrics ----
    metrics = analysis.finance_metrics(df)
    print("\nPortfolio finance metrics:")
    for name, value in metrics.items():
        print(f"  {name}: {value:,.2f}")

    # ---- Part 10: Export reports ----
    export_reports(df, groups, pa)

    print("\nDone! Reports saved in the 'output' folder.")


def export_reports(df, groups, pandas_results):
    """Part 10 - Export reports to the output folder."""

    # Loan summary (by loan type) -> LoanSummary.xlsx
    groups["loan_type"].to_excel("output/LoanSummary.xlsx")

    # Customer loan report -> CustomerLoanReport.xlsx
    customer_report = df[
        [
            "Customer Name",
            "City",
            "Loan Type",
            "Loan Amount",
            "Salary",
            "EMI Amount",
            "Amount Paid",
            "EMI Due",
            "Payment Completion %",
            "Loan Status",
            "Payment Status",
        ]
    ]
    customer_report.to_excel("output/CustomerLoanReport.xlsx", index=False)

    # Pending payments -> PendingPayments.csv
    pandas_results["pending_payments"][
        [
            "Customer Name",
            "Loan Type",
            "Loan Amount",
            "EMI Amount",
            "EMI Due",
            "Payment Status",
        ]
    ].to_csv("output/PendingPayments.csv", index=False)


if __name__ == "__main__":
    main()
