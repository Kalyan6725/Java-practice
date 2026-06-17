package UI;

import entity.Customer;
import service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Scanner;

@Component
public class CustomerConsoleController {
	@Autowired
	private Scanner scanner;
	@Autowired
	private CustomerService customerService;

//	@Autowired
//	public CustomerConsoleController(Scanner scanner, CustomerService customerService) {
//		this.scanner = scanner;
//		this.customerService = customerService;
//	}

	public void printWelcomeMessage() {
		System.out.println("Welcome to Customer KYC service..");
	}

	public void showMenu() throws SQLException {
		while (true) {
			System.out.println("Enter Choice:");
			System.out.println("1. Add Customer");
			System.out.println("2. Get All Customers");
			System.out.println("3. Update KYC Status");
			System.out.println("4. Delete Customer");
			System.out.println("5. Exit");
			int choice = scanner.nextInt();
			scanner.nextLine();
			redirectChoice(choice);
		}
	}

	private void redirectChoice(int choice) throws SQLException {
		switch (choice) {
			case 1:
				addCustomer();
				break;
			case 2:
				customerService.getAllCustomers();
				break;
			case 3:
				updateKYCStatus();
				break;
			case 4:
				deleteCustomer();
				break;
			case 5:
				System.out.println("Exiting...");
				scanner.close();
				System.exit(0);
				break;
			default:
				System.out.println("Invalid Choice");
		}
	}

	private void addCustomer() throws SQLException {
		System.out.println("Enter Customer Details");
		System.out.print("Id: ");
		int id = scanner.nextInt();
		scanner.nextLine();

		System.out.print("Name: ");
		String name = scanner.nextLine();

		System.out.print("KYC Method (pan/aadhar): ");
		String kycMethod = scanner.nextLine().trim().toLowerCase();

		System.out.print("PAN: ");
		String pan = scanner.nextLine();

		String aadhaar = null;
		if ("aadhar".equals(kycMethod)) {
			System.out.print("Aadhaar: ");
			aadhaar = scanner.nextLine();
		}

		System.out.print("CIBIL Score: ");
		int cibilScore = scanner.nextInt();
		scanner.nextLine();

		System.out.print("KYC Status: ");
		String kycStatus = scanner.nextLine();

		Customer customer = new Customer(id, name, pan, aadhaar, cibilScore, kycStatus, kycMethod);
		customerService.onboardCustomer(customer);
	}

	private void updateKYCStatus() {
		System.out.print("Enter Customer Id: ");
		int updateId = scanner.nextInt();
		scanner.nextLine();
		System.out.print("Enter New KYC Status: ");
		String updatedStatus = scanner.nextLine();
		customerService.updateKYCStatus(updateId, updatedStatus);
	}

	private void deleteCustomer() {
		System.out.print("Enter Customer Id: ");
		int deleteId = scanner.nextInt();
		scanner.nextLine();
		customerService.deleteCustomerById(deleteId);
	}

}
