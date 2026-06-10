import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

class LoanDaoImpl implements LoanDao {
    private List<Loan> loans = new ArrayList<>();

    @Override
    public void addLoan(Loan loan4) {
        loans.add(loan4);
    }

    @Override
    public List<Loan> updateLoan(int loanId, Loan loan) { 
        List<Loan> updatedLoans = loans.stream() 
        .filter(l -> l.getLoanId() == loanId)
        .map(l -> new Loan(loanId, loan.getLoanAmount(), loan.getLoanInterest(), loan.getLoanTenure(), loan.getLoanStatus(), loan.getLoanType()))
        .sorted((l1, l2) -> Integer.compare(l1.getLoanAmount(), l2.getLoanAmount()))
        .toList();
        for (int i = 0; i < loans.size(); i++) {
            if (loans.get(i).getLoanId() == loanId) {
                loans.set(i, loan);
                break;
            }
        }
        return updatedLoans;

    }

    @Override
    public List<Loan> updateLoanInterest() {
        class MyOperator implements UnaryOperator<Loan> { //Using inner class
            @Override
            public Loan apply(Loan loan2) {
                loan2.setLoanInterest(loan2.getLoanInterest() + 2);
                return loan2;
            }
        }
        UnaryOperator<Loan> operator = new MyOperator();
        return loans.stream().map(operator).toList();
    }

    @Override
    public void deleteLoan(int loanId) {
        for(Loan l : loans) {
            if (l.getLoanId() == loanId) {
                loans.remove(l);
                break;
            }
        }
    }

    @Override
    public Loan getLoanById(int loanId) {
        for (Loan loan2 : loans) {
            if (loan2.getLoanId() == loanId) {
                return loan2;
            }
        }
        return null;
    }

    @Override
    public List<Loan> getAllLoans() {
        return loans.stream()
        //.sorted((l1, l2) -> Integer.compare(l1.getLoanAmount(), l2.getLoanAmount()))
        .sorted(Comparator.comparing(Loan::getLoanAmount).thenComparing(Loan::getLoanId))
        .toList();
    }

    // @Override
    // public List<Loan> getRejectedLoans() {
    //     List<Loan> rejectedLoans = new ArrayList<>();
    //     for (Loan loan : loans) {
    //         if (loan.getLoanStatus().equalsIgnoreCase("Rejected")) {
    //             rejectedLoans.add(loan);
    //         }
    //     }
    //     return rejectedLoans;
    // }

    //Using Stream API inner class
    // @Override
    // public List<Loan> getRejectedLoans() {
    //     class MyOperator implements Predicate<Loan> { //Using inner class
    //         @Override
    //         public boolean test(Loan loan) {
    //             return loan.getLoanStatus().equalsIgnoreCase("Rejected");
    //         }
    //     }
    //     Predicate<Loan> predicate = new MyOperator();

    //     List<Loan> rejectedLoans = loans.stream()
    //     .filter(predicate).toList();
    //     return rejectedLoans;
    // }

    // //Using Stream API using anonymous class
    // @Override
    // public List<Loan> getRejectedLoans() {
    //     Predicate<Loan> predicate = new Predicate<Loan> (){ //Using anonymous class
    //         @Override
    //         public boolean test(Loan loan) {
    //             return loan.getLoanStatus().equalsIgnoreCase("Rejected");
    //         }
    //     };

    //     List<Loan> rejectedLoans = loans.stream()
    //     .filter(predicate).toList();
    //     return rejectedLoans;
    // }

    //Using Stream API using lamda expression
    @Override
    public List<Loan> getRejectedLoans() {

        List<Loan> rejectedLoans = loans.stream()
        .filter( (loan1)->  //Using lamda expression
                loan1.getLoanStatus().equalsIgnoreCase("Rejected")

        ).toList();
        return rejectedLoans;
    }

    // public void maxLoanAmount() {
    //     Loan maxLoan = loans.stream()
    //     .max((l1,l2) ->l1.getLoanAmount() - l2.getLoanAmount())
    //     System.out.println("Max Loan: " + maxLoan);
    // }

    // public void minLoanAmount() {
    //     Loan minLoan = loans.stream()
    //     .min((l1,l2) ->l1.getLoanAmount() - l2.getLoanAmount())
    //     System.out.println("Min Loan: " + minLoan);
    // }

    // public void avgLoanAmount() {
    //     Loan avgLoan = loans.stream()
    //     .avg((l1, l2) -> {
    //         l1.setLoanAmount(l1.getLoanAmount() + l2.getLoanAmount());
    //         return l1;
    //     })
    //     .orElse(null);
    //     System.out.println("Average Loan Amount: " + avgLoan);
    // }
}