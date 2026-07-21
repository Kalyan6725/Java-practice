# loan.py
# A simple class that represents one loan (one customer's loan + its payments).
# This is like the "fund.py" file in the previous case study.

class Loan:
    """Holds the details of one customer's loan and its repayment status."""

    def __init__(self, customer_name, city, loan_type, loan_amount,
                 salary, emi_amount, paid_emis, pending_emis, loan_status):
        self.customer_name = customer_name
        self.city = city
        self.loan_type = loan_type
        self.loan_amount = loan_amount
        self.salary = salary
        self.emi_amount = emi_amount
        self.paid_emis = paid_emis
        self.pending_emis = pending_emis
        self.loan_status = loan_status

    def amount_paid(self):
        """Money already repaid = number of paid EMIs x EMI amount."""
        return self.paid_emis * self.emi_amount

    def emi_due(self):
        """Money still to be paid = number of pending EMIs x EMI amount."""
        return self.pending_emis * self.emi_amount

    def monthly_income(self):
        """Monthly income = yearly salary / 12."""
        return self.salary / 12

    def debt_to_income(self):
        """Debt-to-Income ratio = loan amount / salary."""
        if self.salary == 0:
            return 0
        return self.loan_amount / self.salary

    def payment_completion(self):
        """How much of the loan is repaid, as a percentage."""
        total_emis = self.paid_emis + self.pending_emis
        if total_emis == 0:
            return 0
        return (self.paid_emis / total_emis) * 100

    def payment_status(self):
        """Simple text status based on how many EMIs are still pending."""
        if self.pending_emis == 0:
            return "Paid"
        if self.paid_emis == 0:
            return "Pending"
        return "Partial"

    def __str__(self):
        return (
            f"{self.customer_name} ({self.city}) -> {self.loan_type} | "
            f"Loan: {self.loan_amount:.0f} | Status: {self.loan_status} | "
            f"Paid: {self.payment_completion():.1f}% ({self.payment_status()})"
        )
