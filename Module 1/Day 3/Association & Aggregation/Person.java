class Person {
    String fname;
    String lname;
    Address address;
    Passport passport;

    public Person(String fname, String lname, Address adress) {
        this.fname = fname;
        this.lname = lname;
        this.address = adress;
    }
    public Passport getPassport() {
        return passport;
    }

    public void setPassport(Passport passport) {
        this.passport = passport;
        if (passport != null && passport.getPerson() != this) {
            passport.setPerson(this);
        }
    }

    @Override
    public String toString() {
        return "Person[fname=" + fname + ", lname=" + lname + ", address=" + (address==null?"null":address.toString()) + ", passport=" + (passport==null?"null":passport.getPassportNumber()) + "]";
    }
}