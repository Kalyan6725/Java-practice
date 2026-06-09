class KYCRecord {

    private AadhaarDetails details;

    KYCRecord(String aadhaar) {
        details = new AadhaarDetails(aadhaar); // Composition
    }
}