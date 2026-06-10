import java.util.List;

public interface LoanDao {
    void addLoan(Loan loan);
    List<Loan> updateLoan(int loanId, Loan loan);
    List<Loan> updateLoanInterest();
    void deleteLoan(int loanId);
    Loan getLoanById(int loanId);
    List<Loan> getAllLoans();
    List<Loan> getRejectedLoans();
}