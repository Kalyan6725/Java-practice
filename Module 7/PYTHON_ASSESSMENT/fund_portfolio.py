"""
fund_portfolio.py
-----------------
The FundPortfolio class (OOP requirement).

It takes the merged data and works out all the finance metrics such as
total value, returns, CAGR, Sharpe ratio and profit/loss per investor.

To keep things simple we build a small "holdings" table first:
one row per (investor, fund) with net units, money invested and current value.
"""

import numpy as np
import pandas as pd


class FundPortfolio:
    def __init__(self, merged):
        # Save the merged data and build the holdings table once.
        self.merged = merged
        self.holdings = self._build_holdings()

    def _build_holdings(self):
        """Create one row per investor + fund with net units and values.

        Buy adds units and money; Sell removes units and money.
        current value = net units * latest NAV of the fund.
        """
        rows = []
        grouped = self.merged.groupby(["InvestorID", "FundID"])
        for (investor_id, fund_id), group in grouped:
            buys = group[group["TransactionType"] == "Buy"]
            sells = group[group["TransactionType"] == "Sell"]

            net_units = buys["Units"].sum() - sells["Units"].sum()
            net_invested = buys["Amount"].sum() - sells["Amount"].sum()
            latest_nav = group["LatestNAV"].iloc[0]
            current_value = net_units * latest_nav

            rows.append(
                {
                    "InvestorID": investor_id,
                    "InvestorName": group["InvestorName"].iloc[0],
                    "FundID": fund_id,
                    "FundName": group["FundName"].iloc[0],
                    "Category": group["Category"].iloc[0],
                    "ExpenseRatio": group["ExpenseRatio"].iloc[0],
                    "NetUnits": net_units,
                    "NetInvested": net_invested,
                    "CurrentValue": current_value,
                    "Profit": current_value - net_invested,
                }
            )
        return pd.DataFrame(rows)

    # ----- basic totals -----
    def total_portfolio_value(self):
        """Total current value of all holdings."""
        return self.holdings["CurrentValue"].sum()

    def total_invested(self):
        """Total money invested (net of sells)."""
        return self.holdings["NetInvested"].sum()

    def absolute_return(self):
        """Absolute return = current value - invested amount."""
        return self.total_portfolio_value() - self.total_invested()

    def portfolio_return_pct(self):
        """Portfolio return in percent."""
        invested = self.total_invested()
        if invested == 0:
            return float("nan")
        return self.absolute_return() / invested * 100

    # ----- time based returns -----
    def average_holding_period(self, as_of_date):
        """Average number of days the money has been invested."""
        dates = self.merged["TransactionDate"]
        days = (as_of_date - dates).dt.days
        return days.mean()

    def cagr(self, as_of_date):
        """Compound Annual Growth Rate.

        CAGR = (end value / start value) ^ (1 / years) - 1
        """
        invested = self.total_invested()
        end_value = self.total_portfolio_value()
        years = self.average_holding_period(as_of_date) / 365
        if invested <= 0 or years <= 0:
            return float("nan")
        return ((end_value / invested) ** (1 / years) - 1) * 100

    def annualized_return(self, as_of_date):
        """Annualized return based on the average holding period."""
        days = self.average_holding_period(as_of_date)
        if days <= 0:
            return float("nan")
        total_return = self.portfolio_return_pct() / 100
        return ((1 + total_return) ** (365 / days) - 1) * 100

    # ----- risk / quality metrics -----
    def diversification_score(self):
        """How spread out the money is across funds (0 = one fund, close to 1 = many).

        Score = 1 - sum(weight^2), where weight is each fund's share of the money.
        """
        total = self.holdings["CurrentValue"].sum()
        if total == 0:
            return float("nan")
        weights = self.holdings.groupby("FundID")["CurrentValue"].sum() / total
        return 1 - np.sum(weights ** 2)

    def expense_ratio_impact(self):
        """Approximate yearly cost of fees = invested * expense ratio %."""
        impact = self.holdings["NetInvested"] * self.holdings["ExpenseRatio"] / 100
        return impact.sum()

    def sharpe_ratio(self):
        """Simplified Sharpe ratio = mean profit / std of profit across holdings."""
        profits = self.holdings["Profit"]
        if profits.std() == 0:
            return float("nan")
        return profits.mean() / profits.std()

    # ----- allocation breakdowns -----
    def category_wise_investment_pct(self):
        """Percentage of money invested in each fund category."""
        by_category = self.holdings.groupby("Category")["NetInvested"].sum()
        return by_category / by_category.sum() * 100

    def fund_allocation_pct(self):
        """Percentage of money invested in each fund."""
        by_fund = self.holdings.groupby("FundName")["NetInvested"].sum()
        return by_fund / by_fund.sum() * 100

    def investor_profit_loss(self):
        """Profit or loss for each investor."""
        return self.holdings.groupby("InvestorName")["Profit"].sum().sort_values()
