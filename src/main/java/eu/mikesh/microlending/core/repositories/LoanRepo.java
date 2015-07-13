package eu.mikesh.microlending.core.repositories;

import eu.mikesh.microlending.core.models.entities.Loan;

import java.util.List;

public interface LoanRepo {
    public Loan createLoan(Loan data);
    public List<Loan> findAllLoans();
    public Loan findLoan(Long id);
    public Loan findLoanByTitle(String title);
    public List<Loan> findLoansByAccount(Long accountId);
}
