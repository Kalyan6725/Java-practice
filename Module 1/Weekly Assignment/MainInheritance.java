class MainInheritance {
    public static void main(String[] args) {
        InvoiceAsset i = new InvoiceAsset();
        i.display();
        i.displayInvoice();
        
    }

    static class FinancialAsset {
        String assetId;

        void display() {
            System.out.println("Financial Asset");
        }
    }

    static class InvoiceAsset extends FinancialAsset {
        void displayInvoice() {
            System.out.println("Invoice Financing Asset");
        }
    }

    static class BondAsset extends FinancialAsset {
        void displayBond() {
            System.out.println("Corporate Bond Asset");
        }
    }
}