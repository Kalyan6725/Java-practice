# fund.py
# A simple class that represents one investment (one transaction in a fund).
# This is like the "loan.py" file in the previous project.

class FundInvestment:
    """Holds the details of one investor's investment in one fund."""

    def __init__(self, investor_name, fund_name, category, units, purchase_nav, latest_nav):
        self.investor_name = investor_name
        self.fund_name = fund_name
        self.category = category
        self.units = units
        self.purchase_nav = purchase_nav
        self.latest_nav = latest_nav

    def investment_amount(self):
        """Money the investor put in = units x purchase NAV."""
        return self.units * self.purchase_nav

    def current_value(self):
        """Money the investment is worth now = units x latest NAV."""
        return self.units * self.latest_nav

    def profit(self):
        """Profit = current value - investment amount."""
        return self.current_value() - self.investment_amount()

    def roi_percent(self):
        """Return on investment as a percentage."""
        invested = self.investment_amount()
        if invested == 0:
            return 0
        return (self.profit() / invested) * 100

    def __str__(self):
        return (
            f"{self.investor_name} -> {self.fund_name} ({self.category}) | "
            f"Profit: {self.profit():.2f} | ROI: {self.roi_percent():.2f}%"
        )
