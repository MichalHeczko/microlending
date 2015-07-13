package eu.mikesh.microlending.rest.resources.asm;

import eu.mikesh.microlending.rest.mvc.AccountController;
import eu.mikesh.microlending.rest.mvc.LoanController;
import eu.mikesh.microlending.rest.resources.LoanResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import eu.mikesh.microlending.core.models.entities.Loan;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.Date;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

public class LoanResourceAsm extends ResourceAssemblerSupport<Loan, LoanResource> {
    public LoanResourceAsm() {
        super(LoanController.class, LoanResource.class);
    }

    @Override
    public LoanResource toResource(Loan loan) {
        LoanResource resource = new LoanResource();
        resource.setAmount(loan.getAmount());
        resource.setInterestRate(loan.getInterestRate());
        resource.setAddr(loan.getAddr());
        resource.setCreated(loan.getCreated());
        resource.setTerm(loan.getTerm());
        resource.add(linkTo(LoanController.class).slash(loan.getId()).withSelfRel());
        resource.add(linkTo(LoanController.class).slash(loan.getId()).slash("loan-history-entries").withRel("entries"));
        resource.setRid(loan.getId());
        if(loan.getOwner() != null)
            resource.add(linkTo(AccountController.class).slash(loan.getOwner().getId()).withRel("owner"));
        return resource;
    }
}
