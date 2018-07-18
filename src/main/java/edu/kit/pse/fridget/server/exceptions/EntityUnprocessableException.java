package edu.kit.pse.fridget.server.exceptions;

public class EntityUnprocessableException extends RuntimeException {
    public EntityUnprocessableException(String message) {
        super(message);
    }

    public EntityUnprocessableException() {
        super("Request contains invalid data that cannot be processed.");
    }
}
