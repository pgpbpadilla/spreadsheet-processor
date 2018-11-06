package org.pgpb.evaluation;

public enum ExpressionEvaluationError {
    INVALID_FORMAT ("Invalid format."),
    NEGATIVE_NUMBER ("Only positive numbers are allowed."),
    CELL_NOT_FOUND ("Could not resolve cell address.");

    private final String message;

    ExpressionEvaluationError(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return message;
    }
}
