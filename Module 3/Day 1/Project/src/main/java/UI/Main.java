package UI;

import config.MySpringConfiguration;
import entity.Customer;
import service.CustomerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {
        //MySpringConfiguration config = new MySpringConfiguration();
        ApplicationContext context =
                new AnnotationConfigApplicationContext(MySpringConfiguration.class);

        //CustomerService customerService = config.customerService();
        //CustomerService customerService = context.getBean(CustomerService.class);


//        CustomerDao customerDao = new CustomerDaoImpl();
//        KYCService kycService = new PanKYCService();
//        CustomerService customerService =
//                new CustomerServiceImpl(customerDao, kycService);

//        Scanner sc = new Scanner(System.in);
//        while (true) {
//            System.out.println("Enter Choice:");
//            System.out.println("1. Add Customer");
//            System.out.println("2. Get All Customers");
//            System.out.println("3. Update KYC Status");
//            System.out.println("4. Delete Customer");
//            System.out.println("5. Exit");
//            int choice = sc.nextInt();
//            sc.nextLine();
//
//            switch (choice) {
//                case 1:
//                    System.out.println("Enter Customer Details");
//                    System.out.print("Id: ");
//                    int id = sc.nextInt();
//                    sc.nextLine();
//                    System.out.print("Name: ");
//                    String name = sc.nextLine();
//                        System.out.print("KYC Method (pan/aadhar): ");
//                        String kycMethod = sc.nextLine().trim().toLowerCase();
//                    System.out.print("PAN: ");
//                    String pan = sc.nextLine();
//                        String aadhaar = null;
//                        if ("aadhar".equals(kycMethod)) {
//                        System.out.print("Aadhaar: ");
//                        aadhaar = sc.nextLine();
//                        }
//                    System.out.print("CIBIL Score: ");
//                    int cibilScore = sc.nextInt();
//                    sc.nextLine();
//                    System.out.print("KYC Status: ");
//                    String kycStatus = sc.nextLine();
//                    Customer customer =
//                            new Customer(id, name, pan, aadhaar,
//                                cibilScore, kycStatus, kycMethod);
//                    customerService.onboardCustomer(customer);
//                    break;
//                case 2:
//                    customerService.getAllCustomers();
//                    break;
//                case 3:
//                    System.out.print("Enter Customer Id: ");
//                    int updateId = sc.nextInt();
//                    sc.nextLine();
//                    System.out.print("Enter New KYC Status: ");
//                    String updatedStatus = sc.nextLine();
//                    customerService.updateKYCStatus(
//                            updateId,
//                            updatedStatus
//                    );
//                    break;
//                case 4:
//                    System.out.print("Enter Customer Id: ");
//                    int deleteId = sc.nextInt();
//                    customerService.deleteCustomerById(deleteId);
//                    break;
//                case 5:
//                    System.out.println("Exiting...");
//                    sc.close();
//                    return;
//                default:
//                    System.out.println("Invalid Choice");
//            }
//        }
        CustomerConsoleController consoleController = context.getBean(CustomerConsoleController.class);
        consoleController.printWelcomeMessage();
        consoleController.showMenu();
    }
}