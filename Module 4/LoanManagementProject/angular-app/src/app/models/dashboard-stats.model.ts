export interface DashboardStats {
  totalLoans: number;
  activeLoans: number;
  closedLoans: number;
  outstandingBalance: number;
  totalPaid: number;
  nextEmi: number;
  nextEmiDate: string;
  creditScore: number;
  interestPaid: number;
  emisRemaining: number;
  loanHealthScore: number;
}

export interface StatCard {
  label: string;
  value: string | number;
  subValue?: string;
  icon: string;
  color: string;
  change?: {
    value: string;
    positive: boolean;
  };
}
