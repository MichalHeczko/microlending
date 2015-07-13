package eu.mikesh.microlending.core.models.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class LoanHistoryEntry {

    @Id
    @GeneratedValue
    private Long id;

    private Date created;

    private LoanHistoryEntryType type;

    @ManyToOne
    private Loan loan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public LoanHistoryEntryType getType() {
        return type;
    }

    public void setType(LoanHistoryEntryType type) {
        this.type = type;
    }
}
