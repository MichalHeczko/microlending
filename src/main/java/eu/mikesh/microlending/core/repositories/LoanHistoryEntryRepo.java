package eu.mikesh.microlending.core.repositories;

import eu.mikesh.microlending.core.models.entities.LoanHistoryEntry;

import java.util.List;

public interface LoanHistoryEntryRepo {
    public LoanHistoryEntry findLoanHistoryEntry(Long id); // Returns the LoanHistoryEntry or null if it can't be found
    public LoanHistoryEntry deleteLoanHistoryEntry(Long id); // Deletes the found LoanHistoryEntry or returns null if it can't be found

    /**
     * @param id the id of the LoanHistoryEntry to updateLoanHistoryEntry
     * @param data the LoanHistoryEntry containing the data to be used for the updateLoanHistoryEntry
     * @return the updated LoanHistoryEntry or null if the LoanHistoryEntry with the id cannot be found
     */
    public LoanHistoryEntry updateLoanHistoryEntry(Long id, LoanHistoryEntry data);

    public LoanHistoryEntry createLoanHistoryEntry(LoanHistoryEntry data);

    public List<LoanHistoryEntry> findByLoanId(Long loanId);
}
