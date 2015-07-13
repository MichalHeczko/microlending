package eu.mikesh.microlending.core.services;

import eu.mikesh.microlending.core.models.entities.LoanHistoryEntry;

public interface LoanHistoryEntryService {
    public LoanHistoryEntry findLoanHistoryEntry(Long id);
    public LoanHistoryEntry deleteLoanHistoryEntry(Long id);
    public LoanHistoryEntry updateLoanHistoryEntry(Long id, LoanHistoryEntry data);
}
