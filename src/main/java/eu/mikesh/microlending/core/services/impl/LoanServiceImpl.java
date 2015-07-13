package eu.mikesh.microlending.core.services.impl;

import eu.mikesh.microlending.core.models.entities.LoanHistoryEntry;
import eu.mikesh.microlending.core.repositories.LoanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import eu.mikesh.microlending.core.models.entities.Loan;
import eu.mikesh.microlending.core.repositories.LoanHistoryEntryRepo;
import eu.mikesh.microlending.core.services.LoanService;
import eu.mikesh.microlending.core.services.exceptions.LoanNotFoundException;
import eu.mikesh.microlending.core.services.util.LoanHistoryEntryList;
import eu.mikesh.microlending.core.services.util.LoanList;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepo loanRepo;

    @Autowired
    private LoanHistoryEntryRepo entryRepo;

    @Override
    public LoanHistoryEntry createLoanHistoryEntry(Long loanId, LoanHistoryEntry data) {
        Loan loan = loanRepo.findLoan(loanId);
        if(loan == null)
        {
            throw new LoanNotFoundException();
        }
        LoanHistoryEntry entry = entryRepo.createLoanHistoryEntry(data);
        entry.setLoan(loan);
        return entry;
    }

    @Override
    public LoanList findAllLoans() {
        return new LoanList(loanRepo.findAllLoans());
    }

    @Override
    public LoanHistoryEntryList findAllLoanHistoryEntries(Long loanId) {
        Loan loan = loanRepo.findLoan(loanId);
        if(loan == null)
        {
            throw new LoanNotFoundException();
        }
        return new LoanHistoryEntryList(loanId, entryRepo.findByLoanId(loanId));
    }

    @Override
    public Loan findLoan(Long id) {
        return loanRepo.findLoan(id);
    }
}
