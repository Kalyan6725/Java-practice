class Student {
    private String name;
    private int PHYmarks;
    private int CHEmarks;
    private int MATHSmarks; 
    private int HistoryMarks;
    private int GeographyMarks;

    Student(String name, int PHYmarks, int CHEmarks, int MATHSmarks, int HistoryMarks, int GeographyMarks) {
        this.name = name;
        this.PHYmarks = PHYmarks;
        this.CHEmarks = CHEmarks;
        this.MATHSmarks = MATHSmarks;
        this.HistoryMarks = HistoryMarks;
        this.GeographyMarks = GeographyMarks;
    }
    // Getters
    public String getName() {
        return name;
    }
    public int getPHYmarks() {
        return PHYmarks;
    }
    public int getCHEmarks() {
        return CHEmarks;
    }
    public int getMATHSmarks() {
        return MATHSmarks;
    }
    public int getHistoryMarks() {
        return HistoryMarks;
    }
    public int getGeographyMarks() {
        return GeographyMarks;
    }

    //setters
    public void setName(String name) {
        this.name = name;
    }
    public void setPHYmarks(int PHYmarks) {
        this.PHYmarks = PHYmarks;
    }
    public void setCHEmarks(int CHEmarks) {
        this.CHEmarks = CHEmarks;
    }
    public void setMATHSmarks(int MATHSmarks) {
        this.MATHSmarks = MATHSmarks;
    }
    public void setHistoryMarks(int historyMarks) {
        HistoryMarks = historyMarks;
    }
    public void setGeographyMarks(int geographyMarks) {
        GeographyMarks = geographyMarks;
    }

    @Override
    public String toString() {
        return "{ Name: " + name + " PHY Marks: " + PHYmarks + " CHE Marks: " + CHEmarks + " MATHS Marks: " + MATHSmarks + " History Marks: " + HistoryMarks + " Geography Marks: " + GeographyMarks + " }";
    }
}