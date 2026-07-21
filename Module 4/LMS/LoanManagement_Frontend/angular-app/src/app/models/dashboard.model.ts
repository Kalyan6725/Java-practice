/** DashboardDTO from GET /api/dashboard. */
export interface Dashboard {
  totalCustomers: number;
  totalLoans: number;
  totalLoanAmountDisbursed: number;
  totalPenaltyCollected: number;
  topBranch: string;
  highestLoanCustomer: string;
}
