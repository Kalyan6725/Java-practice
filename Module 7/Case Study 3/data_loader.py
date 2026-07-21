import pandas as pd


def read_data():
    """Part 1 - Read all three CSV files from the data folder."""
    try:
        customers = pd.read_csv("data/customers.csv")
        loans = pd.read_csv("data/loan_application.csv")
        payments = pd.read_csv("data/loan_payments.csv")
    except FileNotFoundError as error:
        # If a file is missing we stop the program with a clear message.
        print("Could not find a data file:", error)
        raise

    return customers, loans, payments


def clean_data(customers, loans, payments):
    """Part 2 - Clean the three datasets."""

    # 1) Remove duplicate rows from every dataset.
    customers = customers.drop_duplicates()
    loans = loans.drop_duplicates()
    payments = payments.drop_duplicates()

    # 2) Remove duplicate Loan IDs (keep the first record for each loan).
    loans = loans.drop_duplicates(subset="LoanID")
    payments = payments.drop_duplicates(subset="LoanID")

    # 3) Fix the ID mismatch between files so the merge works later.
    #    - customers use "C101" but loan_application uses "101".
    #      We add a "C" so both look the same.
    loans["CustomerID"] = "C" + loans["CustomerID"].astype(str)

    #    - loan_application uses "L1001" but loan_payments uses "L101".
    #      Turning "L101" into "L1001" means inserting a 0 after "L1".
    payments["LoanID"] = "L10" + payments["LoanID"].astype(str).str[2:]

    # 4) Convert the date columns into real datetime values.
    loans["ApplicationDate"] = pd.to_datetime(
        loans["ApplicationDate"], errors="coerce"
    )
    payments["LastPaymentDate"] = pd.to_datetime(
        payments["LastPaymentDate"], errors="coerce"
    )

    # 5) Replace missing Salary with the median salary.
    median_salary = customers["Salary"].median()
    customers["Salary"] = customers["Salary"].fillna(median_salary)

    # NOTE: The case study also mentions "Credit Score", but the
    # customers.csv file in this project has no Credit Score column,
    # so any credit-score steps are skipped.

    # 6) Remove rows with a negative Loan Amount.
    loans = loans[loans["LoanAmount"] >= 0]

    # 7) Remove invalid EMI amounts (zero or negative).
    payments = payments[payments["EMIAmount"] > 0]

    # 8) Remove future payment dates (a payment cannot happen after today).
    today = pd.Timestamp.now()
    payments = payments[payments["LastPaymentDate"] <= today]

    return customers, loans, payments


def merge_data(customers, loans, payments):
    """Part 3 - Merge all three datasets into one DataFrame."""

    # Start from loans, then join customers (on CustomerID) and
    # payments (on LoanID).
    df = loans.merge(customers, on="CustomerID", how="left")
    df = df.merge(payments, on="LoanID", how="left")

    # Give the columns friendly names (like the case study asks for).
    df = df.rename(
        columns={
            "CustomerName": "Customer Name",
            "LoanType": "Loan Type",
            "LoanAmount": "Loan Amount",
            "LoanStatus": "Loan Status",
            "EMIAmount": "EMI Amount",
            "PaidEMIs": "Paid EMIs",
            "PendingEMIs": "Pending EMIs",
            "LastPaymentDate": "Payment Date",
            "BranchID": "Branch",
        }
    )

    return df
