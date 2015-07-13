package eu.mikesh.microlending.core.services.exceptions;

public class LoanExistsException extends RuntimeException {
    public LoanExistsException() {
    }

    public LoanExistsException(String message) {
        super(message);
    }

    public LoanExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoanExistsException(Throwable cause) {
        super(cause);
    }
}
