package org.pgpb.evaluation;

public enum ValueError {
    INVALID_FORMAT ("Invalid format."),
    NEGATIVE_NUMBER ("Only positive numbers are allowed."),
    INVALID_ADDRESS_FORMAT("Invalid cell address format."),
    CELL_NOT_FOUND ("Could not resolve cell address."),
    INVALID_EXPRESSION("Invalid expression."),
    UNSUPPORTED_OPERATION("Operation not supported.");

    private final String message;

    ValueError(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return message;
    }
}
