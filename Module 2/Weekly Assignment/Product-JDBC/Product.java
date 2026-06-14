class Product {
    int price;
    String brand;
    String category;
    int discount;
    String name;
    String id;
    double rating;
    Product() {
    }
    Product(int price,String brand,String category,int discount,String name,String id,double rating){
        this.price=price;
        this.brand=brand;
        this.category=category;
        this.discount=discount;
        this.name=name;
        this.id=id;
        this.rating=rating;
    }
    @Override
    public String toString(){
        return "{ Price: "+price+" Brand: "+brand+" Category: "+category+" Discount: "+discount+"% Name: "+name+" Id: "+id+" Rating: "+rating+" }";
    }

    public String getName(){
        return this.name;
    }

    public String getCategory(){
        return this.category;
    }

    public String getBrand(){
        return this.brand;
    }

    public int getPrice(){
        return this.price;
    }

    public String getId(){
        return this.id;
    }
    public double getRating(){
        return this.rating;
    }
    public int getDiscount(){
        return this.discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}