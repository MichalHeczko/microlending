package eu.mikesh.microlending.rest.resources;

import eu.mikesh.microlending.core.models.entities.LoanHistoryEntry;
import eu.mikesh.microlending.core.models.entities.LoanHistoryEntryType;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

public class LoanHistoryEntryResource extends ResourceSupport {
    private Date created;

    private LoanHistoryEntryType type;

    private Long rid;

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
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

    public LoanHistoryEntry toLoanHistoryEntry() {
        LoanHistoryEntry entry = new LoanHistoryEntry();
        entry.setCreated(created);
        entry.setType(type);
        return entry;
    }
}
