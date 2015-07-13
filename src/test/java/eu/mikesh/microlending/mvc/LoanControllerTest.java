package eu.mikesh.microlending.mvc;

import eu.mikesh.microlending.core.models.entities.LoanHistoryEntry;
import eu.mikesh.microlending.core.models.entities.LoanHistoryEntryType;
import eu.mikesh.microlending.core.services.util.LoanHistoryEntryList;
import eu.mikesh.microlending.rest.mvc.LoanController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import eu.mikesh.microlending.core.models.entities.Account;
import eu.mikesh.microlending.core.models.entities.Loan;
import eu.mikesh.microlending.core.services.LoanService;
import eu.mikesh.microlending.core.services.exceptions.LoanNotFoundException;
import eu.mikesh.microlending.core.services.util.LoanList;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoanControllerTest {
    @InjectMocks
    private LoanController controller;

    @Mock
    private LoanService loanService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void findAllLoans() throws Exception {
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

        LoanList allLoans = new LoanList(list);

        when(loanService.findAllLoans()).thenReturn(allLoans);

        mockMvc.perform(get("/rest/loans"))
                .andExpect(jsonPath("$.loans[*].rid",
                        hasItems(is(1), is(2))))
                .andExpect(status().isOk());
    }

    @Test
    public void getLoan() throws Exception {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setAmount(BigInteger.valueOf(100L));
        loan.setInterestRate(2F);
        loan.setAddr(InetAddress.getByName("4.2.2.1"));
        loan.setCreated(new Date());
        loan.setTerm(new Date());

        Account account = new Account();
        account.setId(1L);
        loan.setOwner(account);

        when(loanService.findLoan(1L)).thenReturn(loan);

        mockMvc.perform(get("/rest/loans/1"))
                .andExpect(jsonPath("$.links[*].href",
                        hasItem(endsWith("/loans/1"))))
                .andExpect(jsonPath("$.links[*].href",
                        hasItem(endsWith("/loans/1/loan-history-entries"))))
                .andExpect(jsonPath("$.links[*].href",
                        hasItem(endsWith("/accounts/1"))))
                .andExpect(jsonPath("$.links[*].rel",
                        hasItems(is("self"), is("owner"), is("entries"))))
                .andExpect(jsonPath("$.rid", is(1)))
                .andExpect(status().isOk());
    }

    @Test
    public void getNonExistingLoan() throws Exception {
        when(loanService.findLoan(1L)).thenReturn(null);

        mockMvc.perform(get("/rest/loans/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createLoanHistoryEntryExistingLoan() throws Exception {
        Loan loan = new Loan();
        loan.setId(1L);

        LoanHistoryEntry entry = new LoanHistoryEntry();
        entry.setCreated(new Date());
        entry.setType(LoanHistoryEntryType.CREATION);
        entry.setId(1L);

        when(loanService.createLoanHistoryEntry(eq(1L), any(LoanHistoryEntry.class))).thenReturn(entry);

        mockMvc.perform(post("/rest/loans/1/loan-history-entries")
                .content("{\"type\":\"CREATION\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type", is(LoanHistoryEntryType.CREATION.toString())))
                .andExpect(jsonPath("$.links[*].href", hasItem(endsWith("rest/loan-history-entries/1"))))
                .andExpect(header().string("Location", endsWith("rest/loan-history-entries/1")))
                .andExpect(status().isCreated());
    }


    @Test
    public void createLoanHistoryEntryNonExistingLoan() throws Exception {
        when(loanService.createLoanHistoryEntry(eq(1L), any(LoanHistoryEntry.class))).thenThrow(new LoanNotFoundException());

        mockMvc.perform(post("/rest/loans/1/loan-history-entries")
                .content("{\"type\":\"CREATION\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void listLoanHistoryEntriesForExistingLoan() throws Exception {

        LoanHistoryEntry entryA = new LoanHistoryEntry();
        entryA.setId(1L);
        entryA.setCreated(new Date());
        entryA.setType(LoanHistoryEntryType.CREATION);

        LoanHistoryEntry entryB = new LoanHistoryEntry();
        entryB.setId(2L);
        entryB.setCreated(new Date());
        entryB.setType(LoanHistoryEntryType.CREATION);

        List<LoanHistoryEntry> loanListings = new ArrayList();
        loanListings.add(entryA);
        loanListings.add(entryB);

        LoanHistoryEntryList list = new LoanHistoryEntryList(1L, loanListings);

        when(loanService.findAllLoanHistoryEntries(1L)).thenReturn(list);

        mockMvc.perform(get("/rest/loans/1/loan-history-entries"))
                .andDo(print())
                .andExpect(jsonPath("$.links[*].href", hasItem(endsWith("/loans/1/loan-history-entries"))))
                .andExpect(jsonPath("$.entries[*].type", hasItem(is(LoanHistoryEntryType.CREATION.toString()))))
                .andExpect(status().isOk());
    }

    @Test
    public void listLoanHistoryEntriesForNonExistingLoan() throws Exception {
        when(loanService.findAllLoanHistoryEntries(1L)).thenThrow(new LoanNotFoundException());

        mockMvc.perform(get("/rest/loans/1/loan-history-entries"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
