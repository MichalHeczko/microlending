package eu.mikesh.microlending.core.repositories.jpa;

import eu.mikesh.microlending.core.models.entities.Loan;
import eu.mikesh.microlending.core.repositories.LoanRepo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


@Repository
public class JpaLoanRepo implements LoanRepo {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Loan createLoan(Loan data) {
        em.persist(data);
        return data;
    }

    @Override
    public List<Loan> findAllLoans() {
        Query query = em.createQuery("SELECT b from Loan b");
        return query.getResultList();
    }

    @Override
    public Loan findLoan(Long id) {
        return em.find(Loan.class, id);
    }

    @Override
    public Loan findLoanByTitle(String title) {
        Query query = em.createQuery("SELECT b from Loan b where b.title=?1");
        query.setParameter(1, title);
        List<Loan> loans = query.getResultList();
        if(loans.isEmpty()) {
            return null;
        } else {
            return loans.get(0);
        }
    }

    @Override
    public List<Loan> findLoansByAccount(Long accountId) {
        Query query = em.createQuery("SELECT b from Loan b where b.owner.id=?1");
        query.setParameter(1, accountId);
        return query.getResultList();
    }
}
