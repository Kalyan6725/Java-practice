import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

class MainProduct {
    public static void main(String[] args){
        ProductDao productDao = new ProductDaoImpl();
        System.out.println("Enter the choice: \n1. Add Product \n2. Update Product \n3. Delete Product \n4. Get Product By ID \n5. Get All Products \n6. Find By Category \n7. Find By Brand \n8. Find By Name \n9. Exit");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline
        while(choice!=9) {
            switch (choice) {
                case 1:
                    System.out.println("Enter Product Details (price brand category discount name id rating):");
                    int price = sc.nextInt();
                    sc.nextLine();
                    String brand = sc.nextLine();
                    String category = sc.nextLine();
                    int discount = sc.nextInt();
                    sc.nextLine();
                    String name = sc.nextLine();
                    String id = sc.nextLine();
                    double rating = sc.nextDouble();
                    sc.nextLine();
                    productDao.addProduct(new Product(price, brand, category, discount, name, id, rating));
                    break;
                case 2:
                    System.out.println("Enter Product ID to Update:");
                    String updateId = sc.nextLine();
                    System.out.println("Enter New Product Details (price brand category discount name rating):");
                    int newPrice = sc.nextInt();
                    sc.nextLine();
                    String newBrand = sc.nextLine();
                    String newCategory = sc.nextLine();
                    int newDiscount = sc.nextInt();
                    sc.nextLine();
                    String newName = sc.nextLine();
                    double newRating = sc.nextDouble();
                    sc.nextLine();
                    productDao.updateProduct(updateId, new Product(newPrice, newBrand, newCategory, newDiscount, newName, updateId, newRating));
                    break;
                case 3:
                    System.out.println("Enter Product ID to Delete:");
                    String deleteId = sc.nextLine();
                    productDao.deleteProduct(deleteId);
                    break;
                case 4:
                    System.out.println("Enter Product ID to Retrieve:");
                    String getId = sc.nextLine();
                    Product product = productDao.getProductById(getId);
                    if (product != null) {
                        System.out.println(product);
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;
                case 5:
                    System.out.println("All Products:");
                    for (Product p : productDao.getAllProducts()) {
                        System.out.println(p);
                    }
                    break;
                case 6:
                    System.out.println("Enter Category to Search:");
                    String searchCategory = sc.nextLine();
                    for (Product p : productDao.findByCategory(searchCategory)) {
                        System.out.println(p);
                    }
                    break;
                case 7:
                    System.out.println("Enter Brand to Search:");
                    String searchBrand = sc.nextLine();
                    for (Product p : productDao.findByBrand(searchBrand)) {
                        System.out.println(p);
                    }
                    break;
                case 8:
                    System.out.println("Enter Name to Search:");
                    String searchName = sc.nextLine();
                    for (Product p : productDao.findByName(searchName)) {
                        System.out.println(p);
                    }
                    break;
                case 9:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
            System.out.println("Enter the choice: \n1. Add Product \n2. Update Product \n3. Delete Product \n4. Get Product By ID \n5. Get All Products \n6. Find By Category \n7. Find By Brand \n8. Find By Name \n9. Exit");
            choice = sc.nextInt();
            sc.nextLine(); // Consume newline
        }
        
    }
}