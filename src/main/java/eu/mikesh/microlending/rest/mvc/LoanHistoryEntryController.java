package eu.mikesh.microlending.rest.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import eu.mikesh.microlending.core.models.entities.LoanHistoryEntry;
import eu.mikesh.microlending.core.services.LoanHistoryEntryService;
import eu.mikesh.microlending.rest.resources.LoanHistoryEntryResource;
import eu.mikesh.microlending.rest.resources.asm.LoanHistoryEntryResourceAsm;

@Controller
@RequestMapping("/rest/loan-history-entries")
public class LoanHistoryEntryController {
    private LoanHistoryEntryService service;

    @Autowired
    public LoanHistoryEntryController(LoanHistoryEntryService service)
    {
        this.service = service;
    }

    @RequestMapping(value="/{loanHistoryEntryId}",
            method = RequestMethod.GET)
    public ResponseEntity<LoanHistoryEntryResource> getLoanHistoryEntry(
            @PathVariable Long loanHistoryEntryId) {
        LoanHistoryEntry entry = service.findLoanHistoryEntry(loanHistoryEntryId);
        if(entry != null)
        {
            LoanHistoryEntryResource res = new LoanHistoryEntryResourceAsm().toResource(entry);
            return new ResponseEntity<LoanHistoryEntryResource>(res, HttpStatus.OK);
        } else {
            return new ResponseEntity<LoanHistoryEntryResource>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value="/{loanHistoryEntryId}",
            method = RequestMethod.DELETE)
    public ResponseEntity<LoanHistoryEntryResource> deleteLoanHistoryEntry(
            @PathVariable Long loanHistoryEntryId) {
        LoanHistoryEntry entry = service.deleteLoanHistoryEntry(loanHistoryEntryId);
        if(entry != null)
        {
            LoanHistoryEntryResource res = new LoanHistoryEntryResourceAsm().toResource(entry);
            return new ResponseEntity<LoanHistoryEntryResource>(res, HttpStatus.OK);
        } else {
            return new ResponseEntity<LoanHistoryEntryResource>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value="/{loanHistoryEntryId}",
            method = RequestMethod.PUT)
    public ResponseEntity<LoanHistoryEntryResource> updateLoanHistoryEntry(
            @PathVariable Long loanHistoryEntryId, @RequestBody LoanHistoryEntryResource sentLoanHistoryEntry) {
        LoanHistoryEntry updatedEntry = service.updateLoanHistoryEntry(loanHistoryEntryId, sentLoanHistoryEntry.toLoanHistoryEntry());
        if(updatedEntry != null)
        {
            LoanHistoryEntryResource res = new LoanHistoryEntryResourceAsm().toResource(updatedEntry);
            return new ResponseEntity<LoanHistoryEntryResource>(res, HttpStatus.OK);
        } else {
            return new ResponseEntity<LoanHistoryEntryResource>(HttpStatus.NOT_FOUND);
        }
    }
}
