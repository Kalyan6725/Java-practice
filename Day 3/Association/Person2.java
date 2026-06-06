class Person2 {
    String fname;
    String lname;
    Address address;

    public Person2(String fname, String lname, Address adress) {
        this.fname = fname;
        this.lname = lname;
        this.address = adress;
    }

    @Override
    public String toString() {
        return fname + " " + lname + " lives at " + (address==null?"null":address.toString());
    }
}