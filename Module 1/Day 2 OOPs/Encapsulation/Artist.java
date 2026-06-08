class Artist {
    private String name;
    private String artStyle;
    private int medium;

    public Artist(String name, String artStyle, int medium) {
        this.name = name;
        this.artStyle = artStyle;
        this.medium = medium;
    }

    public void displayDetails() {
        System.out.println("Artist Name: " + name);
        System.out.println("Art Style: " + artStyle);
        System.out.println("Medium: " + medium);
    }

    public void perform() {
        System.out.println(name + " is performing a " + artStyle + " concert!");
    }
}