import pandas as pd

funds = pd.read_csv("data/funds.csv")
investors = pd.read_csv("data/investors.csv")
tx = pd.read_csv("data/transactions.csv")
nav = pd.read_csv("data/nav_history.csv")

for name, df in [("funds", funds), ("investors", investors), ("transactions", tx), ("nav_history", nav)]:
    print("=" * 60)
    print(name, "shape:", df.shape)
    print("columns:", list(df.columns))
    print(df.dtypes)
    print("nulls:\n", df.isna().sum())
    print("duplicates:", df.duplicated().sum())
    print(df.head(3).to_string())

print("=" * 60)
print("funds FundID sample:", sorted(funds["FundID"].unique())[:5], "count", funds["FundID"].nunique())
print("tx FundID sample:", sorted(tx["FundID"].dropna().unique())[:5], "count", tx["FundID"].nunique())
print("nav FundID sample:", sorted(nav["FundID"].dropna().unique())[:5], "count", nav["FundID"].nunique())
print("investors InvestorID sample:", sorted(investors["InvestorID"].unique())[:5], "count", investors["InvestorID"].nunique())
print("tx InvestorID sample:", sorted(tx["InvestorID"].dropna().unique())[:5], "count", tx["InvestorID"].nunique())
print("FundID overlap tx vs funds:", len(set(tx["FundID"]) & set(funds["FundID"])))
print("FundID overlap tx vs nav:", len(set(tx["FundID"]) & set(nav["FundID"])))
print("InvestorID overlap tx vs investors:", len(set(tx["InvestorID"]) & set(investors["InvestorID"])))
print("TransactionType values:", tx["TransactionType"].unique())
print("Category values:", funds["Category"].unique())
print("RiskProfile values:", investors["RiskProfile"].unique())
