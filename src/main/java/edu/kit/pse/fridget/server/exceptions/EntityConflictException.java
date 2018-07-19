package edu.kit.pse.fridget.server.exceptions;

public class EntityConflictException extends RuntimeException {
    public EntityConflictException(String message) {
        super(message);
    }

    public EntityConflictException(String name, String id) {
        super(String.format("%s id=\"%s\" cannot be deleted, it does not exist.", name, id));
    }
}
