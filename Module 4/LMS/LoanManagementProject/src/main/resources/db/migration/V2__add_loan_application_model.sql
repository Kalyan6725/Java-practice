CREATE TABLE IF NOT EXISTS loan_applications (
    application_id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    loan_code VARCHAR(20) NOT NULL,
    requested_amount DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    tenure_months INTEGER NOT NULL DEFAULT 1,
    application_date DATE NOT NULL DEFAULT CURRENT_DATE,
    status VARCHAR(30) NOT NULL DEFAULT 'SUBMITTED',
    remarks VARCHAR(500),
    CONSTRAINT fk_loan_applications_customer
        FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    CONSTRAINT fk_loan_applications_product
        FOREIGN KEY (loan_code) REFERENCES loan_products(loan_code)
);

ALTER TABLE customers ADD COLUMN IF NOT EXISTS phone VARCHAR(16) DEFAULT '+910000000000';
ALTER TABLE customers ADD COLUMN IF NOT EXISTS address VARCHAR(255) DEFAULT 'Address not provided';
ALTER TABLE customers ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'ACTIVE';
UPDATE customers SET phone = '+910000000000' WHERE phone IS NULL;
UPDATE customers SET address = 'Address not provided' WHERE address IS NULL;
UPDATE customers SET status = 'ACTIVE' WHERE status IS NULL;
ALTER TABLE customers ALTER COLUMN phone SET NOT NULL;
ALTER TABLE customers ALTER COLUMN address SET NOT NULL;
ALTER TABLE customers ALTER COLUMN status SET NOT NULL;
ALTER TABLE customers ALTER COLUMN phone DROP DEFAULT;
ALTER TABLE customers ALTER COLUMN address DROP DEFAULT;
ALTER TABLE customers ALTER COLUMN status DROP DEFAULT;

ALTER TABLE loan_products ADD COLUMN IF NOT EXISTS minimum_amount DOUBLE PRECISION DEFAULT 10000.0;
ALTER TABLE loan_products ADD COLUMN IF NOT EXISTS maximum_amount DOUBLE PRECISION DEFAULT 1000000.0;
ALTER TABLE loan_products ADD COLUMN IF NOT EXISTS interest_rate DOUBLE PRECISION DEFAULT 10.0;
ALTER TABLE loan_products ADD COLUMN IF NOT EXISTS minimum_tenure INTEGER DEFAULT 12;
ALTER TABLE loan_products ADD COLUMN IF NOT EXISTS maximum_tenure INTEGER DEFAULT 60;
ALTER TABLE loan_products ADD COLUMN IF NOT EXISTS processing_fee DOUBLE PRECISION DEFAULT 0.0;
ALTER TABLE loan_products ADD COLUMN IF NOT EXISTS active BOOLEAN DEFAULT TRUE;
UPDATE loan_products SET minimum_amount = 10000.0 WHERE minimum_amount IS NULL;
UPDATE loan_products SET maximum_amount = 1000000.0 WHERE maximum_amount IS NULL;
UPDATE loan_products SET interest_rate = 10.0 WHERE interest_rate IS NULL;
UPDATE loan_products SET minimum_tenure = 12 WHERE minimum_tenure IS NULL;
UPDATE loan_products SET maximum_tenure = 60 WHERE maximum_tenure IS NULL;
UPDATE loan_products SET processing_fee = 0.0 WHERE processing_fee IS NULL;
UPDATE loan_products SET active = TRUE WHERE active IS NULL;
ALTER TABLE loan_products ALTER COLUMN minimum_amount SET NOT NULL;
ALTER TABLE loan_products ALTER COLUMN maximum_amount SET NOT NULL;
ALTER TABLE loan_products ALTER COLUMN interest_rate SET NOT NULL;
ALTER TABLE loan_products ALTER COLUMN minimum_tenure SET NOT NULL;
ALTER TABLE loan_products ALTER COLUMN maximum_tenure SET NOT NULL;
ALTER TABLE loan_products ALTER COLUMN processing_fee SET NOT NULL;
ALTER TABLE loan_products ALTER COLUMN active SET NOT NULL;
ALTER TABLE loan_products ALTER COLUMN minimum_amount DROP DEFAULT;
ALTER TABLE loan_products ALTER COLUMN maximum_amount DROP DEFAULT;
ALTER TABLE loan_products ALTER COLUMN interest_rate DROP DEFAULT;
ALTER TABLE loan_products ALTER COLUMN minimum_tenure DROP DEFAULT;
ALTER TABLE loan_products ALTER COLUMN maximum_tenure DROP DEFAULT;
ALTER TABLE loan_products ALTER COLUMN processing_fee DROP DEFAULT;
ALTER TABLE loan_products ALTER COLUMN active DROP DEFAULT;

ALTER TABLE loan_accounts ADD COLUMN IF NOT EXISTS loan_number VARCHAR(30) DEFAULT CONCAT('LN-', EXTRACT(EPOCH FROM now()) * 1000);
ALTER TABLE loan_accounts ADD COLUMN IF NOT EXISTS application_id BIGINT;
ALTER TABLE loan_accounts ADD COLUMN IF NOT EXISTS application_date DATE DEFAULT CURRENT_DATE;
ALTER TABLE loan_accounts ADD COLUMN IF NOT EXISTS approval_date DATE;
ALTER TABLE loan_accounts ADD COLUMN IF NOT EXISTS disbursement_date DATE;
ALTER TABLE loan_accounts ADD COLUMN IF NOT EXISTS interest_rate DOUBLE PRECISION DEFAULT 10.0;
ALTER TABLE loan_accounts ADD COLUMN IF NOT EXISTS tenure_months INTEGER DEFAULT 12;
ALTER TABLE loan_accounts ADD COLUMN IF NOT EXISTS next_emi_date DATE;
UPDATE loan_accounts SET loan_number = CONCAT('LN-', loan_account_id) WHERE loan_number IS NULL;
UPDATE loan_accounts SET application_date = COALESCE(application_date, loan_start_date, CURRENT_DATE) WHERE application_date IS NULL;
UPDATE loan_accounts SET interest_rate = 10.0 WHERE interest_rate IS NULL;
UPDATE loan_accounts SET tenure_months = 12 WHERE tenure_months IS NULL;
UPDATE loan_accounts SET next_emi_date = emi_due_date WHERE next_emi_date IS NULL;
ALTER TABLE loan_accounts ALTER COLUMN loan_number SET NOT NULL;
ALTER TABLE loan_accounts ALTER COLUMN application_date SET NOT NULL;
ALTER TABLE loan_accounts ALTER COLUMN interest_rate SET NOT NULL;
ALTER TABLE loan_accounts ALTER COLUMN tenure_months SET NOT NULL;
ALTER TABLE loan_accounts ALTER COLUMN loan_number DROP DEFAULT;
ALTER TABLE loan_accounts ALTER COLUMN application_date DROP DEFAULT;
ALTER TABLE loan_accounts ALTER COLUMN interest_rate DROP DEFAULT;
ALTER TABLE loan_accounts ALTER COLUMN tenure_months DROP DEFAULT;
ALTER TABLE loan_accounts ADD CONSTRAINT uq_loan_accounts_loan_number UNIQUE (loan_number);
ALTER TABLE loan_accounts ADD CONSTRAINT fk_loan_accounts_application
    FOREIGN KEY (application_id) REFERENCES loan_applications(application_id);

ALTER TABLE emi_payments ADD COLUMN IF NOT EXISTS installment_no INTEGER DEFAULT 1;
ALTER TABLE emi_payments ADD COLUMN IF NOT EXISTS due_date DATE DEFAULT CURRENT_DATE;
ALTER TABLE emi_payments ADD COLUMN IF NOT EXISTS emi_amount DOUBLE PRECISION DEFAULT 0.0;
ALTER TABLE emi_payments ADD COLUMN IF NOT EXISTS principal_paid DOUBLE PRECISION DEFAULT 0.0;
ALTER TABLE emi_payments ADD COLUMN IF NOT EXISTS interest_paid DOUBLE PRECISION DEFAULT 0.0;
ALTER TABLE emi_payments ADD COLUMN IF NOT EXISTS total_paid DOUBLE PRECISION DEFAULT 0.0;
ALTER TABLE emi_payments ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'PAID';
UPDATE emi_payments SET installment_no = 1 WHERE installment_no IS NULL;
UPDATE emi_payments SET due_date = COALESCE(due_date, payment_date, CURRENT_DATE) WHERE due_date IS NULL;
UPDATE emi_payments SET emi_amount = 0.0 WHERE emi_amount IS NULL;
UPDATE emi_payments SET principal_paid = 0.0 WHERE principal_paid IS NULL;
UPDATE emi_payments SET interest_paid = 0.0 WHERE interest_paid IS NULL;
UPDATE emi_payments SET total_paid = COALESCE(total_paid, amount_paid, 0.0) WHERE total_paid IS NULL;
UPDATE emi_payments SET status = 'PAID' WHERE status IS NULL;
ALTER TABLE emi_payments ALTER COLUMN installment_no SET NOT NULL;
ALTER TABLE emi_payments ALTER COLUMN due_date SET NOT NULL;
ALTER TABLE emi_payments ALTER COLUMN emi_amount SET NOT NULL;
ALTER TABLE emi_payments ALTER COLUMN principal_paid SET NOT NULL;
ALTER TABLE emi_payments ALTER COLUMN interest_paid SET NOT NULL;
ALTER TABLE emi_payments ALTER COLUMN total_paid SET NOT NULL;
ALTER TABLE emi_payments ALTER COLUMN status SET NOT NULL;
ALTER TABLE emi_payments ALTER COLUMN installment_no DROP DEFAULT;
ALTER TABLE emi_payments ALTER COLUMN due_date DROP DEFAULT;
ALTER TABLE emi_payments ALTER COLUMN emi_amount DROP DEFAULT;
ALTER TABLE emi_payments ALTER COLUMN principal_paid DROP DEFAULT;
ALTER TABLE emi_payments ALTER COLUMN interest_paid DROP DEFAULT;
ALTER TABLE emi_payments ALTER COLUMN total_paid DROP DEFAULT;
ALTER TABLE emi_payments ALTER COLUMN status DROP DEFAULT;
