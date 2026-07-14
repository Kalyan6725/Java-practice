export interface EMIPayment {
  id: string;
  loanId: string;
  amount: number;
  penalty: number;
  paymentType: 'UPI' | 'ONLINE' | 'CARD' | 'NETBANKING' | 'CASH';
  paymentDate: string;
  dueDate: string;
  status: 'SUCCESS' | 'PENDING' | 'FAILED' | 'LATE';
  transactionId?: string;
}

export interface PaymentMethod {
  type: 'UPI' | 'CARD' | 'NETBANKING';
  label: string;
  icon: string;
}
