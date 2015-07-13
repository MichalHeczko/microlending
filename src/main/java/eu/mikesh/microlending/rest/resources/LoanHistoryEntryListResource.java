package eu.mikesh.microlending.rest.resources;

import org.springframework.hateoas.ResourceSupport;

import java.util.List;

public class LoanHistoryEntryListResource extends ResourceSupport {
    private List<LoanHistoryEntryResource> entries;

    public List<LoanHistoryEntryResource> getEntries() {
        return entries;
    }

    public void setEntries(List<LoanHistoryEntryResource> entries) {
        this.entries = entries;
    }
}
