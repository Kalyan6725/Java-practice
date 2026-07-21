export type LoanType = 'PERSONAL' | 'HOME' | 'VEHICLE' | 'EDUCATION' | 'BUSINESS';

/** LoanProductResponse from the backend. */
export interface LoanProduct {
  loanCode: string;
  loanName: string;
  loanType: LoanType;
  minimumAmount: number;
  maximumAmount: number;
  interestRate: number;
  minimumTenure: number;
  maximumTenure: number;
  processingFee: number;
  dailyPenaltyRate: number;
  active: boolean;
}

/** POST/PUT /api/loan-products - LoanProductRequest. */
export interface LoanProductRequest {
  loanCode: string;
  loanName: string;
  loanType: LoanType;
  minimumAmount: number;
  maximumAmount: number;
  interestRate: number;
  minimumTenure: number;
  maximumTenure: number;
  processingFee: number;
  dailyPenaltyRate?: number;
  active?: boolean;
}
