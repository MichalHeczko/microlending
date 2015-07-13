package eu.mikesh.microlending.core.repositories;

import eu.mikesh.microlending.core.models.entities.Account;

import java.util.List;

public interface AccountRepo {
    public List<Account> findAllAccounts();
    public Account findAccount(Long id);
    public Account findAccountByName(String name);
    public Account createAccount(Account data);
}
