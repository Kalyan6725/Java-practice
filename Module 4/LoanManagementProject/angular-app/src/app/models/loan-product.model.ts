export interface LoanProduct {
  id: string;
  name: string;
  code: string;
  type: string;
  maxAmount: number;
  minInterestRate: number;
  maxInterestRate: number;
  minTenure: number;
  maxTenure: number;
  processingFee: string;
  description: string;
  features: string[];
  icon: string;
  color: string;
}
