import java.util.Scanner;

class UILoan {
    public static void main(String[] args) {
        LoanDao loanDao = new LoanDaoImpl();
        Scanner sc = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.println("Enter the choice: \n1. Add Loan \n2. Update Loan \n3.UpdateInterest \n4. Delete Loan \n5. Get Loan By ID \n6. Get All Loans \n7. Get Rejected Loans \n8. Exit");
            choice = sc.nextInt();
            sc.nextLine();

            if (choice == 8) {
                System.out.println("Exiting...");
                break;
            }

            switch (choice) {
                case 1:
                    System.out.println("Enter Loan Details (loanId loanAmount loanInterest loanTenure loanStatus loanType):");
                    int loanId = sc.nextInt();
                    int amount = sc.nextInt();
                    int interest = sc.nextInt();
                    int tenure = sc.nextInt();
                    sc.nextLine();
                    String loanStatus = sc.nextLine();
                    String loanType = sc.nextLine();
                    loanDao.addLoan(new Loan(loanId, amount, interest, tenure, loanStatus, loanType));
                    break;
                case 2:
                    System.out.println("Enter Loan ID to Update:");
                    int updateId = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter New Loan Details (loanAmount loanInterest loanTenure loanStatus loanType):");
                    int newAmount = sc.nextInt();
                    int newInterest = sc.nextInt();
                    int newTenure = sc.nextInt();
                    sc.nextLine();
                    String newLoanStatus = sc.nextLine();
                    String newLoanType = sc.nextLine();
                    System.out.println("Updated Loan:");
                    System.out.println(loanDao.updateLoan(updateId, new Loan(updateId, newAmount, newInterest, newTenure, newLoanStatus, newLoanType)));
                    break;
                case 3:
                    System.out.println(loanDao.updateLoanInterest());
                    break;
                case 4:
                    System.out.println("Enter Loan ID to Delete:");
                    int deleteId = sc.nextInt();
                    sc.nextLine();
                    loanDao.deleteLoan(deleteId);
                    break;
                case 5:
                    System.out.println("Enter Loan ID to Retrieve:");
                    int getId = sc.nextInt();
                    sc.nextLine();
                    Loan loan = loanDao.getLoanById(getId);
                    if (loan != null) {
                        System.out.println(loan);
                    } else {
                        System.out.println("Loan not found.");
                    }
                    break;
                case 6:
                    System.out.println("All Loans:");
                    for (Loan allLoan : loanDao.getAllLoans()) {
                        System.out.println(allLoan);
                    }
                    break;
                case 7:
                    System.out.println("Rejected Loans:");
                    for (Loan rejectedLoan : loanDao.getRejectedLoans()) {
                        System.out.println(rejectedLoan);
                    }
                    break;
                case 8:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }
}