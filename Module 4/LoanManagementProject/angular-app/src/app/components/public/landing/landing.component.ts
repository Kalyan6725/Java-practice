import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { LoanCardComponent } from '../../shared/loan-card/loan-card.component';
import { LoanProduct } from '../../../models/loan-product.model';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, RouterLink, LoanCardComponent],
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css']
})
export class LandingComponent {
  loanProducts: LoanProduct[] = [
    {
      id: 'PL-2024-001',
      name: 'Personal Loan',
      code: 'PL-2024-001',
      type: 'PERSONAL',
      maxAmount: 1000000,
      minInterestRate: 9.5,
      maxInterestRate: 14,
      minTenure: 12,
      maxTenure: 60,
      processingFee: '1.5% + GST',
      description: 'Perfect for medical emergencies, weddings, travel, or debt consolidation. Quick disbursal with minimal documentation.',
      features: ['Instant Approval', 'No Collateral', 'Flexible EMI'],
      icon: 'bi-person-circle',
      color: 'primary'
    },
    {
      id: 'HL-2024-001',
      name: 'Home Loan',
      code: 'HL-2024-001',
      type: 'HOME',
      maxAmount: 7500000,
      minInterestRate: 7.5,
      maxInterestRate: 9.0,
      minTenure: 12,
      maxTenure: 300,
      processingFee: '0.5% + GST',
      description: 'Make your dream home a reality with our affordable home loan solutions. Tax benefits under Section 80C & 24(b).',
      features: ['Tax Benefits', 'Long Tenure', 'Low EMI'],
      icon: 'bi-house-door-fill',
      color: 'success'
    },
    {
      id: 'VL-2024-001',
      name: 'Vehicle Loan',
      code: 'VL-2024-001',
      type: 'VEHICLE',
      maxAmount: 1500000,
      minInterestRate: 8.5,
      maxInterestRate: 11,
      minTenure: 12,
      maxTenure: 84,
      processingFee: '1.0% + GST',
      description: 'Drive your dream car or bike with easy financing options. Available for both new and pre-owned vehicles.',
      features: ['New & Used', 'Fast Approval', '90% Funding'],
      icon: 'bi-car-front-fill',
      color: 'warning'
    },
    {
      id: 'EL-2024-001',
      name: 'Education Loan',
      code: 'EL-2024-001',
      type: 'EDUCATION',
      maxAmount: 2500000,
      minInterestRate: 8.0,
      maxInterestRate: 12,
      minTenure: 12,
      maxTenure: 180,
      processingFee: 'Nil',
      description: 'Invest in your future with our education loan for Indian and abroad studies. Moratorium period available.',
      features: ['Moratorium', 'Tax Benefits', 'Abroad Study'],
      icon: 'bi-mortarboard-fill',
      color: 'info'
    },
    {
      id: 'BL-2024-001',
      name: 'Business Loan',
      code: 'BL-2024-001',
      type: 'BUSINESS',
      maxAmount: 5000000,
      minInterestRate: 10,
      maxInterestRate: 16,
      minTenure: 12,
      maxTenure: 84,
      processingFee: '2.0% + GST',
      description: 'Grow your business with flexible financing solutions for MSMEs. Working capital and expansion loans available.',
      features: ['MSME Friendly', 'Quick Disbursal', 'Collateral Free'],
      icon: 'bi-briefcase-fill',
      color: 'danger'
    },
    {
      id: 'GL-2024-001',
      name: 'Gold Loan',
      code: 'GL-2024-001',
      type: 'GOLD',
      maxAmount: 10000000,
      minInterestRate: 7.35,
      maxInterestRate: 12,
      minTenure: 6,
      maxTenure: 36,
      processingFee: '0.5% + GST',
      description: 'Unlock the value of your gold with instant loans. Secure storage with full insurance coverage.',
      features: ['Instant Disbursal', 'Secure Vault', 'Best Rates'],
      icon: 'bi-gem',
      color: 'warning'
    }
  ];

  faqs = [
    {
      id: 'faq1',
      question: 'What documents are required to apply for a loan?',
      answer: 'Basic documents include: Identity proof (Aadhaar/PAN/Passport), Address proof, Income proof (salary slips/ITR), Bank statements (last 6 months), and photographs. Additional documents may be required based on the loan type.',
      isOpen: true
    },
    {
      id: 'faq2',
      question: 'How long does loan approval take?',
      answer: 'With our AI-powered credit assessment system, loan approval typically takes 24-48 hours after document submission. In some cases, instant approval is possible for eligible applicants.',
      isOpen: false
    },
    {
      id: 'faq3',
      question: 'Can I prepay my loan without charges?',
      answer: 'Yes, we offer flexible prepayment and part-payment options. There are no prepayment charges for personal loans, education loans, and vehicle loans. Nominal charges may apply for business loans as per RBI guidelines.',
      isOpen: false
    },
    {
      id: 'faq4',
      question: 'What is the minimum credit score required?',
      answer: 'While we prefer applicants with a credit score of 750 or above, we evaluate each application holistically. Lower credit scores may be considered with additional documentation or higher interest rates.',
      isOpen: false
    },
    {
      id: 'faq5',
      question: 'How is my data kept secure?',
      answer: 'We use 256-bit SSL encryption, comply with PCI DSS standards, and conduct regular security audits. Your data is stored in secure, RBI-compliant servers with multi-layer protection. We never share your personal information with third parties without consent.',
      isOpen: false
    },
    {
      id: 'faq6',
      question: 'Are there any hidden charges?',
      answer: 'No, we believe in complete transparency. All charges including processing fees, interest rates, GST, and any other applicable charges are clearly mentioned in the loan agreement before you sign. There are absolutely no hidden charges.',
      isOpen: false
    }
  ];

  toggleFaq(index: number) {
    this.faqs[index].isOpen = !this.faqs[index].isOpen;
  }
}
