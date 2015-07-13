package eu.mikesh.microlending.rest.resources;

import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.List;


public class LoanListResource extends ResourceSupport {
    private List<LoanResource> loans = new ArrayList<LoanResource>();

    public List<LoanResource> getLoans() {
        return loans;
    }

    public void setLoans(List<LoanResource> loans) {
        this.loans = loans;
    }
}
