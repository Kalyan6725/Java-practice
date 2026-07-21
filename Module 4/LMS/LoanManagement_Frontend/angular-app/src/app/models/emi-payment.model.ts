export type PaymentType = 'CASH' | 'CARD' | 'ONLINE' | 'UPI';

/** EmiPaymentResponse from the backend. */
export interface EmiPayment {
  paymentId: number;
  loanAccountId: number;
  loanNumber: string;
  installmentNo: number;
  dueDate: string;
  paymentDate: string;
  emiAmount: number;
  principalPaid: number;
  interestPaid: number;
  penaltyPaid: number;
  totalPaid: number;
  paymentType: PaymentType;
  status: string;
}

/** POST /api/emi-payments - EmiPaymentRequest. */
export interface EmiPaymentRequest {
  loanAccountId: number;
  installmentNo?: number;
  amount?: number;
  paymentType: PaymentType;
}
