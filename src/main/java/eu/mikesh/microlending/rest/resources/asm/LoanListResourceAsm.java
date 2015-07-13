package eu.mikesh.microlending.rest.resources.asm;

import eu.mikesh.microlending.rest.mvc.LoanController;
import eu.mikesh.microlending.rest.resources.LoanListResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import eu.mikesh.microlending.core.services.util.LoanList;

public class LoanListResourceAsm extends ResourceAssemblerSupport<LoanList, LoanListResource> {

    public LoanListResourceAsm()
    {
        super(LoanController.class, LoanListResource.class);
    }

    @Override
    public LoanListResource toResource(LoanList loanList) {
        LoanListResource res = new LoanListResource();
        res.setLoans(new LoanResourceAsm().toResources(loanList.getLoans()));
        return res;
    }
}
