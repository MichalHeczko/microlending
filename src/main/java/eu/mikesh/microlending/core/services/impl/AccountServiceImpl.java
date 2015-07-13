package eu.mikesh.microlending.core.services.impl;

import eu.mikesh.microlending.core.repositories.LoanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import eu.mikesh.microlending.core.models.entities.Account;
import eu.mikesh.microlending.core.models.entities.Loan;
import eu.mikesh.microlending.core.repositories.AccountRepo;
import eu.mikesh.microlending.core.services.AccountService;
import eu.mikesh.microlending.core.services.exceptions.AccountDoesNotExistException;
import eu.mikesh.microlending.core.services.exceptions.AccountExistsException;
import eu.mikesh.microlending.core.services.exceptions.LoanExistsException;
import eu.mikesh.microlending.core.services.util.AccountList;
import eu.mikesh.microlending.core.services.util.LoanList;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private LoanRepo loanRepo;

    @Override
    public Account findAccount(Long id) {
        return accountRepo.findAccount(id);
    }

    @Override
    public Account createAccount(Account data) {
        Account account = accountRepo.findAccountByName(data.getName());
        if(account != null)
        {
            throw new AccountExistsException();
        }
        return accountRepo.createAccount(data);
    }

    @Override
    public Loan createLoan(Long accountId, Loan data) {
        Account account = accountRepo.findAccount(accountId);
        if(account == null)
        {
            throw new AccountDoesNotExistException();
        }

        Loan createdLoan = loanRepo.createLoan(data);

        createdLoan.setOwner(account);

        return createdLoan;
    }

    @Override
    public LoanList findLoansByAccount(Long accountId) {
        Account account = accountRepo.findAccount(accountId);
        if(account == null)
        {
            throw new AccountDoesNotExistException();
        }
        return new LoanList(loanRepo.findLoansByAccount(accountId));
    }

    @Override
    public AccountList findAllAccounts() {
        return new AccountList(accountRepo.findAllAccounts());
    }

    @Override
    public Account findByAccountName(String name) {
        return accountRepo.findAccountByName(name);
    }
}
