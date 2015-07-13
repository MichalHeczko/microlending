package eu.mikesh.microlending.core.services.impl;

import eu.mikesh.microlending.core.models.entities.LoanHistoryEntry;
import eu.mikesh.microlending.core.services.LoanHistoryEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import eu.mikesh.microlending.core.repositories.LoanHistoryEntryRepo;

@Service
@Transactional
public class LoanHistoryEntryServiceImpl implements LoanHistoryEntryService {

    @Autowired
    private LoanHistoryEntryRepo entryRepo;

    @Override
    public LoanHistoryEntry findLoanHistoryEntry(Long id) {
        return entryRepo.findLoanHistoryEntry(id);
    }

    @Override
    public LoanHistoryEntry deleteLoanHistoryEntry(Long id) {
        return entryRepo.deleteLoanHistoryEntry(id);
    }

    @Override
    public LoanHistoryEntry updateLoanHistoryEntry(Long id, LoanHistoryEntry data) {
        return entryRepo.updateLoanHistoryEntry(id, data);
    }
}
