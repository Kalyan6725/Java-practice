"""Read the three input CSV files from the data folder."""

import os
import pandas as pd

# Folder where this file is, and the data folder next to it
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
DATA_DIR = os.path.join(BASE_DIR, "data")


def read_csv(filename):
    # Build the full path and read the file
    path = os.path.join(DATA_DIR, filename)
    try:
        df = pd.read_csv(path)
    except FileNotFoundError:
        print("File not found:", path)
        raise
    return df


def load_data():
    print("Reading input files ...")
    customers = read_csv("customers.csv")
    loans = read_csv("loans.csv")
    credit = read_csv("credit_score.csv")
    print("  customers:", len(customers), "rows")
    print("  loans    :", len(loans), "rows")
    print("  credit   :", len(credit), "rows")
    return customers, loans, credit
