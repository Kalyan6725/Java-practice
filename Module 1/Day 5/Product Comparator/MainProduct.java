import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

class MainProduct {
    public static void main(String[] args){
        
        Product[] p={new Product(1000,"nike","shoes",10,"Air Jordan","12A",4.7),
                new Product(1999,"adidas","bag",20,"A1","123B",4.2),
                new Product(1500,"reebok","shoes",15,"Classic","12A",3.1),
                new Product(500,"puma","clothing",5,"T-shirt","123C",4.0),
                new Product(2000,"nike","clothing",25,"Jacket","123D",4.5)
        };

        System.out.println("select the sorting criteria:");
        System.out.println("1. Price (Low to High)\n2. Price (High to Low)\n3. Rating (Low to High)\n4. Rating (High to Low)\n5. Discount (Low to High)\n6. Discount (High to Low)\n7. Brand (A to Z)\n");
        Scanner sc=new Scanner(System.in);
        String choicestr=sc.nextLine();
        int choice=Integer.parseInt(choicestr);
        switch(choice){
            case 1:
                Arrays.sort(p, new Comparator<Product>(){
                    @Override
                    public int compare(Product p1, Product p2) {
                        return p1.price - p2.price; 
                    }
                });
                break;
            case 2:
                Arrays.sort(p, new Comparator<Product>(){
                    @Override
                    public int compare(Product p1, Product p2) {
                        return p2.price - p1.price; 
                    }
                });
                break;
            case 3:
                Arrays.sort(p, new Comparator<Product>(){
                    @Override
                    public int compare(Product p1, Product p2) {
                        return Double.compare(p1.rating, p2.rating);
                    }
                });
                break;
            case 4:
                Arrays.sort(p, new Comparator<Product>(){
                    @Override
                    public int compare(Product p1, Product p2) {
                        return Double.compare(p2.rating, p1.rating);
                    }
                });
                break;
            case 5:
                Arrays.sort(p, new Comparator<Product>(){
                    @Override
                    public int compare(Product p1, Product p2) {
                        return p1.discount - p2.discount;
                    }
                });
                break;
            case 6:
                Arrays.sort(p, new Comparator<Product>(){
                    @Override
                    public int compare(Product p1, Product p2) {
                        return p2.discount - p1.discount;
                    }
                });
                break;
            case 7:
                Arrays.sort(p, new Comparator<Product>(){
                    @Override
                    public int compare(Product p1, Product p2) {
                        if(p1.brand.equals(p2.brand)){  // If brands are the same, sort by name
                            return p1.name.compareTo(p2.name);
                        }
                        else{
                        return p1.brand.compareTo(p2.brand);
                        }
                    }
                });
                break;
            default:
                System.out.println("Invalid choice");
        }
        System.out.println(Arrays.toString(p));
        sc.close();
    }
}