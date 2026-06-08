class Book{
    String title;
    String author;
    int pages;

    public void read(){
        System.out.println(" I an reading "+title+" book and author is "+author);
    }
    public void getSummary(){
        System.out.println(title+" book has "+pages+" pages");
    }
}