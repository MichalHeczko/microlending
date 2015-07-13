package eu.mikesh.microlending.core.services.exceptions;

public class LoanNotFoundException extends RuntimeException {
    public LoanNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoanNotFoundException(String message) {
        super(message);
    }

    public LoanNotFoundException() {
    }
}
