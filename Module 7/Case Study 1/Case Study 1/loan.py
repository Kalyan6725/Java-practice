"""A simple Loan class."""


class Loan:
    def __init__(self, loan_id, customer_id, amount, interest_rate,
                 tenure, emi, paid_emis, default_flag):
        self.loan_id = loan_id
        self.customer_id = customer_id
        self.amount = amount
        self.interest_rate = interest_rate
        self.tenure = tenure
        self.emi = emi
        self.paid_emis = paid_emis
        self.default_flag = default_flag

    def remaining_emis(self):
        # EMIs still left to pay
        left = self.tenure - self.paid_emis
        if left < 0:
            left = 0
        return left

    def outstanding(self):
        # Money still to be paid
        return self.remaining_emis() * self.emi

    def utilisation(self):
        # How much of the loan is still unpaid (0 to 1)
        total = self.emi * self.tenure
        if total == 0:
            return 0
        return self.outstanding() / total

    def dti(self, monthly_income):
        # Debt to income ratio
        if monthly_income <= 0:
            return 0
        return self.emi / monthly_income

    def is_defaulted(self):
        return self.default_flag == 1
