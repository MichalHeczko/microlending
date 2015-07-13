package eu.mikesh.microlending.rest.resources.asm;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import eu.mikesh.microlending.core.models.entities.Account;
import eu.mikesh.microlending.rest.mvc.AccountController;
import eu.mikesh.microlending.rest.resources.AccountResource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

public class AccountResourceAsm extends ResourceAssemblerSupport<Account, AccountResource> {
    public AccountResourceAsm() {
        super(AccountController.class, AccountResource.class);
    }

    @Override
    public AccountResource toResource(Account account) {
        AccountResource res = new AccountResource();
        res.setName(account.getName());
        res.setPassword(account.getPassword());
        res.setRid(account.getId());
        res.add(linkTo(methodOn(AccountController.class).getAccount(account.getId())).withSelfRel());
        res.add(linkTo(methodOn(AccountController.class).findAllLoans(account.getId())).withRel("loans"));
        return res;
    }
}
