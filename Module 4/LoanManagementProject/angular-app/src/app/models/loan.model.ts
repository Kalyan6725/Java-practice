export interface Loan {
  id: string;
  loanType: string;
  amount: number;
  outstandingBalance: number;
  emiAmount: number;
  interestRate: number;
  tenure: number;
  startDate: string;
  dueDate: string;
  nextDueDate?: string;
  status: 'ACTIVE' | 'CLOSED' | 'PENDING' | 'REJECTED';
  progress: number;
  customerId?: string;
  customerName?: string;
}

export interface LoanApplication {
  id: string;
  customerName: string;
  loanType: string;
  amount: number;
  purpose: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  appliedDate: string;
  interestRate?: number;
}
