package eu.mikesh.microlending.rest.mvc;

import eu.mikesh.microlending.core.models.entities.LoanHistoryEntry;
import eu.mikesh.microlending.rest.resources.LoanListResource;
import eu.mikesh.microlending.rest.resources.LoanResource;
import eu.mikesh.microlending.rest.resources.asm.LoanHistoryEntryResourceAsm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import eu.mikesh.microlending.core.models.entities.Loan;
import eu.mikesh.microlending.core.services.util.LoanHistoryEntryList;
import eu.mikesh.microlending.core.services.LoanService;
import eu.mikesh.microlending.core.services.exceptions.LoanNotFoundException;
import eu.mikesh.microlending.core.services.util.LoanList;
import eu.mikesh.microlending.rest.exceptions.NotFoundException;
import eu.mikesh.microlending.rest.resources.LoanHistoryEntryListResource;
import eu.mikesh.microlending.rest.resources.LoanHistoryEntryResource;
import eu.mikesh.microlending.rest.resources.asm.LoanHistoryEntryListResourceAsm;
import eu.mikesh.microlending.rest.resources.asm.LoanListResourceAsm;
import eu.mikesh.microlending.rest.resources.asm.LoanResourceAsm;

import java.net.URI;

@Controller
@RequestMapping("/rest/loans")
public class LoanController {
    private LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("permitAll")
    public ResponseEntity<LoanListResource> findAllLoans()
    {
        LoanList loanList = loanService.findAllLoans();
        LoanListResource loanListRes = new LoanListResourceAsm().toResource(loanList);
        return new ResponseEntity<LoanListResource>(loanListRes, HttpStatus.OK);
    }

    @RequestMapping(value="/{loanId}",
        method = RequestMethod.GET)
    public ResponseEntity<LoanResource> getLoan(@PathVariable Long loanId)
    {
        Loan loan = loanService.findLoan(loanId);
        if(loan != null) {
            LoanResource res = new LoanResourceAsm().toResource(loan);
            return new ResponseEntity<LoanResource>(res, HttpStatus.OK);
        } else {
            return new ResponseEntity<LoanResource>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value="/{loanId}/loan-history-entries",
            method = RequestMethod.POST)
    public ResponseEntity<LoanHistoryEntryResource> createLoanHistoryEntry(
            @PathVariable Long loanId,
            @RequestBody LoanHistoryEntryResource sentLoanHistoryEntry
    ) {
        LoanHistoryEntry createdLoanHistoryEntry = null;
        try {
            createdLoanHistoryEntry = loanService.createLoanHistoryEntry(loanId, sentLoanHistoryEntry.toLoanHistoryEntry());
            LoanHistoryEntryResource createdResource = new LoanHistoryEntryResourceAsm().toResource(createdLoanHistoryEntry);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(createdResource.getLink("self").getHref()));
            return new ResponseEntity<LoanHistoryEntryResource>(createdResource, headers, HttpStatus.CREATED);
        } catch (LoanNotFoundException e) {
            throw new NotFoundException(e);
        }
    }

    @RequestMapping(value="/{loanId}/loan-history-entries")
    public ResponseEntity<LoanHistoryEntryListResource> findAllLoanHistoryEntries(
            @PathVariable Long loanId)
    {
        try {
            LoanHistoryEntryList list = loanService.findAllLoanHistoryEntries(loanId);
            LoanHistoryEntryListResource res = new LoanHistoryEntryListResourceAsm().toResource(list);
            return new ResponseEntity<LoanHistoryEntryListResource>(res, HttpStatus.OK);
        } catch(LoanNotFoundException exception)
        {
            throw new NotFoundException(exception);
        }
    }

}
