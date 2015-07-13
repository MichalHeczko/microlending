package eu.mikesh.microlending.core.services.util;

import eu.mikesh.microlending.core.models.entities.LoanHistoryEntry;

import java.util.ArrayList;
import java.util.List;

public class LoanHistoryEntryList {
    private List<LoanHistoryEntry> entries = new ArrayList<LoanHistoryEntry>();
    private Long loanId;

    public LoanHistoryEntryList(Long loanId, List<LoanHistoryEntry> entries) {
        this.loanId = loanId;
        this.entries = entries;
    }

    public List<LoanHistoryEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<LoanHistoryEntry> entries) {
        this.entries = entries;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }
}
