import { LoanType } from './loan-product.model';

export type ApplicationStatus = 'SUBMITTED' | 'UNDER_REVIEW' | 'APPROVED' | 'REJECTED';
export type ReviewDecision = 'UNDER_REVIEW' | 'APPROVED' | 'REJECTED';

/** LoanApplicationResponse from the backend. */
export interface LoanApplication {
  applicationId: number;
  customerId: number;
  customerName: string;
  loanCode: string;
  loanName: string;
  loanType: LoanType;
  requestedAmount: number;
  tenureMonths: number;
  applicationDate: string;
  status: ApplicationStatus;
  remarks?: string;
  loanAccountId?: number;
}

/** POST /api/loan-applications - LoanApplicationRequest. */
export interface LoanApplicationRequest {
  customerId?: number;
  loanCode: string;
  requestedAmount: number;
  tenureMonths: number;
}

/** PUT /api/loan-applications/{id}/review - LoanApplicationReviewRequest. */
export interface LoanApplicationReviewRequest {
  decision: ReviewDecision;
  remarks?: string;
  approvedAmount?: number;
}
