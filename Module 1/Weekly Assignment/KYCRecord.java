class KYCRecord {

    private AadhaarDetails details;
    private PANDetails panDetails;

    KYCRecord(String aadhaar, String pan) {
        details = new AadhaarDetails(aadhaar); // Composition
        panDetails = new PANDetails(pan);
    }
}