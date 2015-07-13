package eu.mikesh.microlending.mvc;

import eu.mikesh.microlending.core.models.entities.Loan;
import eu.mikesh.microlending.core.models.entities.LoanHistoryEntry;
import eu.mikesh.microlending.core.models.entities.LoanHistoryEntryType;
import eu.mikesh.microlending.core.services.LoanHistoryEntryService;
import eu.mikesh.microlending.rest.mvc.LoanHistoryEntryController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class LoanHistoryEntryControllerTest {
    @InjectMocks
    private LoanHistoryEntryController controller;

    @Mock
    private LoanHistoryEntryService service;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getExistingLoanHistoryEntry() throws Exception {
        LoanHistoryEntry entry = new LoanHistoryEntry();
        entry.setId(1L);
        entry.setCreated(new Date());
        entry.setType(LoanHistoryEntryType.CREATION);

        Loan loan = new Loan();
        loan.setId(1L);

        entry.setLoan(loan);

        when(service.findLoanHistoryEntry(1L)).thenReturn(entry);

        mockMvc.perform(get("/rest/loan-history-entries/1"))
                .andExpect(jsonPath("$.type", is(LoanHistoryEntryType.CREATION.toString())))
                .andExpect(jsonPath("$.links[*].href",
                        hasItems(endsWith("/loans/1"), endsWith("/loan-history-entries/1"))))
                .andExpect(jsonPath("$.links[*].rel",
                        hasItems(is("self"), is("loan"))))
                .andExpect(status().isOk());
    }

    @Test
    public void getNonExistingLoanHistoryEntry() throws Exception {
        when(service.findLoanHistoryEntry(1L)).thenReturn(null);

        mockMvc.perform(get("/rest/loan-history-entries/1"))
           .andExpect(status().isNotFound());
    }


    @Test
    public void deleteExistingLoanHistoryEntry() throws Exception {
        LoanHistoryEntry deletedLoanHistoryEntry = new LoanHistoryEntry();
        deletedLoanHistoryEntry.setId(1L);
        deletedLoanHistoryEntry.setCreated(new Date());
        deletedLoanHistoryEntry.setType(LoanHistoryEntryType.CREATION);

        when(service.deleteLoanHistoryEntry(1L)).thenReturn(deletedLoanHistoryEntry);

        mockMvc.perform(delete("/rest/loan-history-entries/1"))
                .andExpect(jsonPath("$.type", is(deletedLoanHistoryEntry.getType().toString())))
                .andExpect(jsonPath("$.links[*].href", hasItem(endsWith("/loan-history-entries/1"))))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteNonExistingLoanHistoryEntry() throws Exception {
        when(service.deleteLoanHistoryEntry(1L)).thenReturn(null);

        mockMvc.perform(delete("/rest/loan-history-entries/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateExistingLoanHistoryEntry() throws Exception {
        LoanHistoryEntry updatedEntry = new LoanHistoryEntry();
        updatedEntry.setId(1L);
        updatedEntry.setCreated(new Date());
        updatedEntry.setType(LoanHistoryEntryType.CREATION);

        when(service.updateLoanHistoryEntry(eq(1L), any(LoanHistoryEntry.class)))
                .thenReturn(updatedEntry);

        mockMvc.perform(put("/rest/loan-history-entries/1")
                .content("{\"type\":\"CREATION\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type", is(updatedEntry.getType().toString())))
                .andExpect(jsonPath("$.links[*].href", hasItem(endsWith("/loan-history-entries/1"))))
                .andExpect(status().isOk());
    }

    @Test
    public void updateNonExistingLoanHistoryEntry() throws Exception {
        when(service.updateLoanHistoryEntry(eq(1L), any(LoanHistoryEntry.class)))
                .thenReturn(null);

        mockMvc.perform(put("/rest/loan-history-entries/1")
                .content("{\"type\":\"CREATION\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
