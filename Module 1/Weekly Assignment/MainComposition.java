class MainComposition {
    public static void main(String[] args) {
        KYCRecord record = new KYCRecord("1234-5678-9012", "ABCDE1234F");
        System.out.println("KYC Record created with Aadhaar: " + record.getAadhaarNumber());
        System.out.println("PAN Number: " + record.getPanNumber());
    }
}