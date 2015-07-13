package eu.mikesh.microlending.rest.mvc;

import eu.mikesh.microlending.core.services.exceptions.AccountDoesNotExistException;
import eu.mikesh.microlending.core.services.exceptions.AccountExistsException;
import eu.mikesh.microlending.core.services.util.AccountList;
import eu.mikesh.microlending.rest.exceptions.ConflictException;
import eu.mikesh.microlending.rest.exceptions.ForbiddenException;
import eu.mikesh.microlending.rest.resources.AccountListResource;
import eu.mikesh.microlending.rest.resources.LoanListResource;
import eu.mikesh.microlending.rest.resources.LoanResource;
import eu.mikesh.microlending.rest.resources.asm.AccountResourceAsm;
import eu.mikesh.microlending.rest.resources.asm.LoanListResourceAsm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import eu.mikesh.microlending.core.models.entities.Account;
import eu.mikesh.microlending.core.models.entities.Loan;
import eu.mikesh.microlending.core.services.AccountService;
import eu.mikesh.microlending.core.services.exceptions.LoanExistsException;
import eu.mikesh.microlending.core.services.util.LoanList;
import eu.mikesh.microlending.rest.exceptions.NotFoundException;
import eu.mikesh.microlending.rest.resources.AccountResource;
import eu.mikesh.microlending.rest.resources.asm.AccountListResourceAsm;
import eu.mikesh.microlending.rest.resources.asm.LoanResourceAsm;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

@Controller
@RequestMapping("/rest/accounts")
public class AccountController {
    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("permitAll")
    public ResponseEntity<AccountListResource> findAllAccounts(@RequestParam(value="name", required = false) String name, @RequestParam(value="password", required = false) String password) {
        AccountList list = null;
        if(name == null) {
            list = accountService.findAllAccounts();
        } else {
            Account account = accountService.findByAccountName(name);
            list = new AccountList(new ArrayList<Account>());
            if(account != null) {
                if(password != null) {
                    if(account.getPassword().equals(password)) {
                        list = new AccountList(Arrays.asList(account));
                    }
                } else {
                    list = new AccountList(Arrays.asList(account));
                }
            }
        }
        AccountListResource res = new AccountListResourceAsm().toResource(list);
        return new ResponseEntity<AccountListResource>(res, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("permitAll")
    public ResponseEntity<AccountResource> createAccount(
            @RequestBody AccountResource sentAccount
    ) {
        try {
            Account createdAccount = accountService.createAccount(sentAccount.toAccount());
            AccountResource res = new AccountResourceAsm().toResource(createdAccount);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(res.getLink("self").getHref()));
            return new ResponseEntity<AccountResource>(res, headers, HttpStatus.CREATED);
        } catch(AccountExistsException exception) {
            throw new ConflictException(exception);
        }
    }

    @RequestMapping( value="/{accountId}",
                method = RequestMethod.GET)
    @PreAuthorize("permitAll")
    public ResponseEntity<AccountResource> getAccount(
            @PathVariable Long accountId
    ) {
        Account account = accountService.findAccount(accountId);
        if(account != null)
        {
            AccountResource res = new AccountResourceAsm().toResource(account);
            return new ResponseEntity<AccountResource>(res, HttpStatus.OK);
        } else {
            return new ResponseEntity<AccountResource>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value="/{accountId}/loans",
        method = RequestMethod.POST)
    @PreAuthorize("permitAll")
    public ResponseEntity<LoanResource> createLoan(
            @PathVariable Long accountId,
            @RequestBody LoanResource res)
    {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            UserDetails details = (UserDetails)principal;
            System.out.print(details);
            Account loggedIn = accountService.findByAccountName(details.getUsername());
            if(loggedIn.getId() == accountId) {
                try {
                    Loan createdLoan = accountService.createLoan(accountId, res.toLoan());
                    LoanResource createdLoanRes = new LoanResourceAsm().toResource(createdLoan);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setLocation(URI.create(createdLoanRes.getLink("self").getHref()));
                    return new ResponseEntity<LoanResource>(createdLoanRes, headers, HttpStatus.CREATED);
                } catch(AccountDoesNotExistException exception)
                {
                    throw new NotFoundException(exception);
                } catch(LoanExistsException exception)
                {
                    throw new ConflictException(exception);
                }
            } else {
                throw new ForbiddenException();
            }
        } else {
            throw new ForbiddenException();
        }
    }

    @RequestMapping(value="/{accountId}/loans",
            method = RequestMethod.GET)
    @PreAuthorize("permitAll")
    public ResponseEntity<LoanListResource> findAllLoans(
            @PathVariable Long accountId) {
        try {
            LoanList loanList = accountService.findLoansByAccount(accountId);
            LoanListResource loanListRes = new LoanListResourceAsm().toResource(loanList);
            return new ResponseEntity<LoanListResource>(loanListRes, HttpStatus.OK);
        } catch(AccountDoesNotExistException exception)
        {
            throw new NotFoundException(exception);
        }
    }



}
