package eu.mikesh.microlending.mvc;

import eu.mikesh.microlending.core.models.entities.Account;
import eu.mikesh.microlending.core.models.entities.Loan;
import eu.mikesh.microlending.core.services.AccountService;
import eu.mikesh.microlending.core.services.exceptions.AccountDoesNotExistException;
import eu.mikesh.microlending.core.services.exceptions.AccountExistsException;
import eu.mikesh.microlending.core.services.util.AccountList;
import eu.mikesh.microlending.core.services.util.LoanList;
import eu.mikesh.microlending.rest.mvc.AccountController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import eu.mikesh.microlending.core.services.exceptions.LoanExistsException;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTest {
    @InjectMocks
    private AccountController controller;

    @Mock
    private AccountService service;

    private MockMvc mockMvc;

    private ArgumentCaptor<Account> accountCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        accountCaptor = ArgumentCaptor.forClass(Account.class);
    }

    @Test
    public void findAllLoansForAccount() throws Exception {
        List<Loan> list = new ArrayList<Loan>();

        Loan loanA = new Loan();
        loanA.setId(1L);
        loanA.setAmount(BigInteger.valueOf(100L));
        loanA.setInterestRate(2F);
        loanA.setAddr(InetAddress.getByName("4.2.2.1"));
        loanA.setCreated(new Date());
        loanA.setTerm(new Date());
        list.add(loanA);

        Loan loanB = new Loan();
        loanB.setId(2L);
        loanB.setAmount(BigInteger.valueOf(200L));
        loanB.setInterestRate(3F);
        loanB.setAddr(InetAddress.getByName("4.2.2.2"));
        loanB.setCreated(new Date());
        loanB.setTerm(new Date());
        list.add(loanB);

        LoanList loanList = new LoanList(list);

        when(service.findLoansByAccount(1L)).thenReturn(loanList);

        mockMvc.perform(get("/rest/accounts/1/loans"))
                .andExpect(jsonPath("$.loans[*].rid",
                        hasItems(is(1), is(2))))
                .andExpect(status().isOk());
    }

    @Test
    public void findAllLoansForNonExistingAccount() throws Exception {
        List<Loan> list = new ArrayList<Loan>();

        Loan loanA = new Loan();
        loanA.setId(1L);
        loanA.setAmount(BigInteger.valueOf(100L));
        loanA.setInterestRate(2F);
        loanA.setAddr(InetAddress.getByName("4.2.2.1"));
        loanA.setCreated(new Date());
        loanA.setTerm(new Date());
        list.add(loanA);

        Loan loanB = new Loan();
        loanB.setId(2L);
        loanB.setAmount(BigInteger.valueOf(200L));
        loanB.setInterestRate(3F);
        loanB.setAddr(InetAddress.getByName("4.2.2.2"));
        loanB.setCreated(new Date());
        loanB.setTerm(new Date());
        list.add(loanB);

        LoanList loanList = new LoanList(list);

        when(service.findLoansByAccount(1L)).thenThrow(new AccountDoesNotExistException());

        mockMvc.perform(get("/rest/accounts/1/loans"))
                .andExpect(status().isNotFound());
    }

    //@Test
    public void createLoanExistingAccount() throws Exception {
        Loan createdLoan = new Loan();
        createdLoan.setId(1L);
        createdLoan.setAmount(BigInteger.valueOf(100L));
        createdLoan.setInterestRate(2F);
        createdLoan.setAddr(InetAddress.getByName("4.2.2.1"));
        createdLoan.setCreated(new Date());
        createdLoan.setTerm(new Date());

        when(service.createLoan(eq(1L), any(Loan.class))).thenReturn(createdLoan);

        mockMvc.perform(post("/rest/accounts/1/loans")
                .content("{\"title\":\"Test Title\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.title", is("Test Title")))
                .andExpect(jsonPath("$.links[*].href", hasItem(endsWith("/loans/1"))))
                .andExpect(header().string("Location", endsWith("/loans/1")))
                .andExpect(status().isCreated());
    }

    //@Test
    public void createLoanNonExistingAccount() throws Exception {
        when(service.createLoan(eq(1L), any(Loan.class))).thenThrow(new AccountDoesNotExistException());

        mockMvc.perform(post("/rest/accounts/1/loans")
                .content("{\"title\":\"Test Title\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //@Test
    public void createLoanExistingLoanName() throws Exception {
        when(service.createLoan(eq(1L), any(Loan.class))).thenThrow(new LoanExistsException());

        mockMvc.perform(post("/rest/accounts/1/loans")
                .content("{\"title\":\"Test Title\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void createAccountNonExistingUsername() throws Exception {
        Account createdAccount = new Account();
        createdAccount.setId(1L);
        createdAccount.setPassword("test");
        createdAccount.setName("test");

        when(service.createAccount(any(Account.class))).thenReturn(createdAccount);

        mockMvc.perform(post("/rest/accounts")
                .content("{\"name\":\"test\",\"password\":\"test\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location", endsWith("/rest/accounts/1")))
                .andExpect(jsonPath("$.name", is(createdAccount.getName())))
                .andExpect(status().isCreated());

        verify(service).createAccount(accountCaptor.capture());

        String password = accountCaptor.getValue().getPassword();
        assertEquals("test", password);
    }

    @Test
    public void createAccountExistingUsername() throws Exception {
        Account createdAccount = new Account();
        createdAccount.setId(1L);
        createdAccount.setPassword("test");
        createdAccount.setName("test");

        when(service.createAccount(any(Account.class))).thenThrow(new AccountExistsException());

        mockMvc.perform(post("/rest/accounts")
                .content("{\"name\":\"test\",\"password\":\"test\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void getExistingAccount() throws Exception {
        Account foundAccount = new Account();
        foundAccount.setId(1L);
        foundAccount.setPassword("test");
        foundAccount.setName("test");

        when(service.findAccount(1L)).thenReturn(foundAccount);

        mockMvc.perform(get("/rest/accounts/1"))
                .andDo(print())
                .andExpect(jsonPath("$.password", is(nullValue())))
                .andExpect(jsonPath("$.name", is(foundAccount.getName())))
                .andExpect(jsonPath("$.links[*].rel",
                        hasItems(endsWith("self"), endsWith("loans"))))
                        .andExpect(status().isOk());
    }

    @Test
    public void getNonExistingAccount() throws Exception {
        when(service.findAccount(1L)).thenReturn(null);

        mockMvc.perform(get("/rest/accounts/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findAllAccounts() throws Exception {
        List<Account> accounts = new ArrayList<Account>();

        Account accountA = new Account();
        accountA.setId(1L);
        accountA.setPassword("accountA");
        accountA.setName("accountA");
        accounts.add(accountA);

        Account accountB = new Account();
        accountB.setId(1L);
        accountB.setPassword("accountB");
        accountB.setName("accountB");
        accounts.add(accountB);

        AccountList accountList = new AccountList(accounts);

        when(service.findAllAccounts()).thenReturn(accountList);

        mockMvc.perform(get("/rest/accounts"))
                .andExpect(jsonPath("$.accounts[*].name",
                        hasItems(endsWith("accountA"), endsWith("accountB"))))
                .andExpect(status().isOk());
    }


    @Test
    public void findAccountsByName() throws Exception {
        List<Account> accounts = new ArrayList<Account>();

        Account accountA = new Account();
        accountA.setId(1L);
        accountA.setPassword("accountA");
        accountA.setName("accountA");
        accounts.add(accountA);

        Account accountB = new Account();
        accountB.setId(1L);
        accountB.setPassword("accountB");
        accountB.setName("accountB");
        accounts.add(accountB);

        AccountList accountList = new AccountList(accounts);

        when(service.findAllAccounts()).thenReturn(accountList);

        mockMvc.perform(get("/rest/accounts").param("name", "accountA"))
                .andExpect(jsonPath("$.accounts[*].name",
                        everyItem(endsWith("accountA"))))
                .andExpect(status().isOk());
    }
}
