package eu.mikesh.microlending.core.services.util;

import eu.mikesh.microlending.core.models.entities.Loan;

import java.util.ArrayList;
import java.util.List;

public class LoanList {

    private List<Loan> loans = new ArrayList<Loan>();

    public LoanList(List resultList) {
        this.loans = resultList;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }
}
