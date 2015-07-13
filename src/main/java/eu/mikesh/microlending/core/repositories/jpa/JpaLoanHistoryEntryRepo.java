package eu.mikesh.microlending.core.repositories.jpa;

import eu.mikesh.microlending.core.models.entities.LoanHistoryEntry;
import org.springframework.stereotype.Repository;
import eu.mikesh.microlending.core.repositories.LoanHistoryEntryRepo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class JpaLoanHistoryEntryRepo implements LoanHistoryEntryRepo {
    @PersistenceContext
    private EntityManager em;

    @Override
    public LoanHistoryEntry findLoanHistoryEntry(Long id) {
        return em.find(LoanHistoryEntry.class, id);
    }

    @Override
    public LoanHistoryEntry deleteLoanHistoryEntry(Long id) {
        LoanHistoryEntry entry = em.find(LoanHistoryEntry.class, id);
        em.remove(entry);
        return entry;
    }

    @Override
    public LoanHistoryEntry updateLoanHistoryEntry(Long id, LoanHistoryEntry data) {
        LoanHistoryEntry entry = em.find(LoanHistoryEntry.class, id);
        entry.setCreated(data.getCreated());
        entry.setType(data.getType());
        return entry;
    }

    @Override
    public LoanHistoryEntry createLoanHistoryEntry(LoanHistoryEntry data) {
        em.persist(data);
        return data;
    }

    @Override
    public List<LoanHistoryEntry> findByLoanId(Long loanId) {
        Query query = em.createQuery("SELECT b FROM LoanHistoryEntry b WHERE b.loan.id=?1");
        query.setParameter(1, loanId);
        return query.getResultList();
    }
}
