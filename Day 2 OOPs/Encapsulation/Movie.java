class Movie{
    private String title;
    private String director;
    private int releaseYear;
    private String actor;

    public Movie(String title,String director,int releaseYear,String actor){
        this.title=title;
        this.director=director;
        this.releaseYear=releaseYear;
        this.actor=actor;
    }
    // public void setTitle(String title){
    //     this.title=title;
    // }
    // public void setDirector(String dierctor){
    //     this.director=director;
    // }
    // public void setYear(int releaseYear){
    //     this.releaseYear=releaseYear;
    // }
    // public void setActor(String actor){
    //     this.actor=actor;
    // }

    public void Details(){
        System.out.println("private String title:"+title+
            "\nprivate String director:"+director+
            "\nprivate String actor:"+actor+
            "\nrelease year:"+releaseYear);
    }
    
}