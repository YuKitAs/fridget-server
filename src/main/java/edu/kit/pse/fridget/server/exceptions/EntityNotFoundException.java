package edu.kit.pse.fridget.server.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String name, String id) {
        super(String.format("%s id=\"%s\" not found.", name, id));
    }
}
