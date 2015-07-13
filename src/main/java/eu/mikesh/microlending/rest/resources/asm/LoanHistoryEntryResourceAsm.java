package eu.mikesh.microlending.rest.resources.asm;

import eu.mikesh.microlending.rest.mvc.LoanHistoryEntryController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import eu.mikesh.microlending.core.models.entities.LoanHistoryEntry;
import eu.mikesh.microlending.rest.mvc.LoanController;
import eu.mikesh.microlending.rest.resources.LoanHistoryEntryResource;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

public class LoanHistoryEntryResourceAsm extends ResourceAssemblerSupport<LoanHistoryEntry, LoanHistoryEntryResource> {

    public LoanHistoryEntryResourceAsm()
    {
        super(LoanHistoryEntryController.class, LoanHistoryEntryResource.class);
    }

    @Override
    public LoanHistoryEntryResource toResource(LoanHistoryEntry loanHistoryEntry) {
        LoanHistoryEntryResource res = new LoanHistoryEntryResource();
        res.setCreated(loanHistoryEntry.getCreated());
        res.setType(loanHistoryEntry.getType());
        res.setRid(loanHistoryEntry.getId());
        Link self = linkTo(LoanHistoryEntryController.class).slash(loanHistoryEntry.getId()).withSelfRel();
        res.add(self);
        if(loanHistoryEntry.getLoan() != null)
        {
            res.add((linkTo(LoanController.class).slash(loanHistoryEntry.getLoan().getId()).withRel("loan")));
        }
        return res;
    }
}
