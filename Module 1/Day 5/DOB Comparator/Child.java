class Child {
    String fname;
    String lname;
    String DOB;

    Child(String fname, String lname, String DOB){
        this.fname=fname;
        this.lname=lname;
        this.DOB=DOB;
    }
    String getDOB(){
        return DOB;
    }

    @Override
    public String toString() {
        return "Child{fname='" + fname + "', lname='" + lname + "', DOB='" + DOB + "'}";
    }
}