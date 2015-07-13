package eu.mikesh.microlending.core.services;

import eu.mikesh.microlending.core.models.entities.Loan;
import eu.mikesh.microlending.core.models.entities.LoanHistoryEntry;
import eu.mikesh.microlending.core.services.util.LoanHistoryEntryList;
import eu.mikesh.microlending.core.services.util.LoanList;

public interface LoanService {
    public LoanHistoryEntry createLoanHistoryEntry(Long loanId, LoanHistoryEntry data);
    public LoanList findAllLoans();
    public LoanHistoryEntryList findAllLoanHistoryEntries(Long loanId);
    public Loan findLoan(Long id);
}
