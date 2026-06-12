class Book {
    private int id;
    private String title;
    private String author;
    private String genre;

    public Book() {
    }
    public Book(String title, String author, String genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
    }
    public Book(int id, String title, String author, String genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getGenre() {
        return genre;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public String toString() {
        return "Book [id=" + id + ", title=" + title + ", author=" + author + ", genre=" + genre + "]";
    }
}