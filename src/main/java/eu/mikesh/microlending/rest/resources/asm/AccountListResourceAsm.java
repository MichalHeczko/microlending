package eu.mikesh.microlending.rest.resources.asm;

import eu.mikesh.microlending.core.services.util.AccountList;
import eu.mikesh.microlending.rest.mvc.AccountController;
import eu.mikesh.microlending.rest.resources.AccountListResource;
import eu.mikesh.microlending.rest.resources.AccountResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.List;

public class AccountListResourceAsm extends ResourceAssemblerSupport<AccountList, AccountListResource> {


    public AccountListResourceAsm() {
        super(AccountController.class, AccountListResource.class);
    }

    @Override
    public AccountListResource toResource(AccountList accountList) {
        List<AccountResource> resList = new AccountResourceAsm().toResources(accountList.getAccounts());
        AccountListResource finalRes = new AccountListResource();
        finalRes.setAccounts(resList);
        return finalRes;
    }
}
