package eu.mikesh.microlending.core.services;

import eu.mikesh.microlending.core.services.util.AccountList;
import eu.mikesh.microlending.core.models.entities.Account;
import eu.mikesh.microlending.core.models.entities.Loan;
import eu.mikesh.microlending.core.services.util.LoanList;

public interface AccountService {
    public Account findAccount(Long id);
    public Account createAccount(Account data);
    public Loan createLoan(Long accountId, Loan data);
    public LoanList findLoansByAccount(Long accountId);
    public AccountList findAllAccounts();
    public Account findByAccountName(String name);
}
