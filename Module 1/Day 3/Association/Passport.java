class Passport {
    private String passportNumber;
    private String country;
    private String issueDate;
    private String expiryDate;
    private Person person;

    public Passport(String passportNumber, String country, String issueDate, String expiryDate) {
        this.passportNumber = passportNumber;
        this.country = country;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
    }

    public String getPassportNumber() {
        return passportNumber;
    }
    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getIssueDate() {
        return issueDate;
    }
    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }
    public String getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
        if (person != null && person.getPassport() != this) {
            person.setPassport(this);
        }
    }

    @Override
    public String toString() {
        return "Passport[number=" + passportNumber + ", country=" + country + ", person=" + (person==null?"null":person.fname+" "+person.lname) + "]";
    }

}