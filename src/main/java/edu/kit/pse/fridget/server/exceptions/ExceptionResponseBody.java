package edu.kit.pse.fridget.server.exceptions;

public class ExceptionResponseBody {
    private String errorMessage;

    ExceptionResponseBody() {
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
