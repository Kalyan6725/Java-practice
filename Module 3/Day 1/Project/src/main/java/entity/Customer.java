package entity;

public class Customer {
    private int id;
    private String name;
    private String pan;
    private String aadhaar;
    private int cibilScore;
    private String KYCstatus;
    private String kycMethod;

    public Customer(){}
    public Customer(int id,String name,String pan,int cibilScore,String KYCstatus){
        this.id=id;
        this.name=name;
        this.pan=pan;
        this.cibilScore=cibilScore;
        this.KYCstatus=KYCstatus;
    }

    public Customer(int id, String name, String pan, String aadhaar, int cibilScore, String KYCstatus, String kycMethod) {
        this.id = id;
        this.name = name;
        this.pan = pan;
        this.aadhaar = aadhaar;
        this.cibilScore = cibilScore;
        this.KYCstatus = KYCstatus;
        this.kycMethod = kycMethod;
    }

    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getPan(){
        return pan;
    }
    public String getAadhaar() {
        return aadhaar;
    }
    public int getCibilScore() {
        return cibilScore;
    }
    public String getKYCstatus(){
        return KYCstatus;
    }
    public String getKycMethod() {
        return kycMethod;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public void setAadhaar(String aadhaar) {
        this.aadhaar = aadhaar;
    }

    public void setCibilScore(int cibilScore){
        this.cibilScore=cibilScore;
    }

    public void setKYCStatus(String KYCstatus) {
        this.KYCstatus = KYCstatus;
    }
    public void setKycMethod(String kycMethod) {
        this.kycMethod = kycMethod;
    }
    public String toString(){
        return "{ id:"+this.getId()+" name:"+this.getName()+" Pan:"+this.getPan()+" Aadhaar:"+this.getAadhaar()+" Cibil Score:"+this.getCibilScore()+
                " KYCMethod:"+this.getKycMethod()+" KYCStatus:"+this.getKYCstatus()+"}";
    }
}
