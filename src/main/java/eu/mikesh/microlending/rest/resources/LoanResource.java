package eu.mikesh.microlending.rest.resources;

import org.springframework.hateoas.ResourceSupport;
import eu.mikesh.microlending.core.models.entities.Loan;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.Date;

public class LoanResource extends ResourceSupport {

    private BigInteger amount;

    private Float interestRate;

    private InetAddress addr;

    private Date created;

    private Date term;

    private Long rid;

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public Loan toLoan() {
        Loan loan = new Loan();
        loan.setAmount(amount);
        loan.setInterestRate(interestRate);
        loan.setAddr(addr);
        loan.setCreated(created);
        loan.setTerm(term);
        return loan;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public Float getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Float interestRate) {
        this.interestRate = interestRate;
    }

    public InetAddress getAddr() {
        return addr;
    }

    public void setAddr(InetAddress addr) {
        this.addr = addr;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getTerm() {
        return term;
    }

    public void setTerm(Date term) {
        this.term = term;
    }
}
