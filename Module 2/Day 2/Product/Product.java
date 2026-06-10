class Product {
    int price;
    String brand;
    String category;
    int discount;
    String name;
    String id;
    double rating;

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
}