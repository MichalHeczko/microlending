package eu.mikesh.microlending.rest.resources.asm;

import eu.mikesh.microlending.core.services.util.LoanHistoryEntryList;
import eu.mikesh.microlending.rest.mvc.LoanController;
import eu.mikesh.microlending.rest.resources.LoanHistoryEntryListResource;
import eu.mikesh.microlending.rest.resources.LoanHistoryEntryResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

public class LoanHistoryEntryListResourceAsm extends ResourceAssemblerSupport<LoanHistoryEntryList, LoanHistoryEntryListResource> {
    public LoanHistoryEntryListResourceAsm() {
        super(LoanController.class, LoanHistoryEntryListResource.class);
    }

    @Override
    public LoanHistoryEntryListResource toResource(LoanHistoryEntryList list) {
        List<LoanHistoryEntryResource> resources = new LoanHistoryEntryResourceAsm().toResources(list.getEntries());
        LoanHistoryEntryListResource listResource = new LoanHistoryEntryListResource();
        listResource.setEntries(resources);
        listResource.add(linkTo(methodOn(LoanController.class).findAllLoanHistoryEntries(list.getLoanId())).withSelfRel());
        return listResource;
    }
}
