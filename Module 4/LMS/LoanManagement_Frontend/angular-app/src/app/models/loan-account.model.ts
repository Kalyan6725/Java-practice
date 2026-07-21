/** LoanAccountResponse from the backend. */
export interface LoanAccount {
  loanAccountId: number;
  loanNumber: string;
  applicationId: number;
  customerId: number;
  customerName: string;
  loanCode: string;
  loanProductName: string;
  loanAmount: number;
  interestRate: number;
  tenureMonths: number;
  emiAmount: number;
  outstandingBalance: number;
  applicationDate: string;
  approvalDate: string;
  disbursementDate: string;
  loanStartDate: string;
  nextEmiDate: string;
  loanCloseDate: string;
  status: string;
}
