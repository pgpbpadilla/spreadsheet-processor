package org.pgpb.evaluation;

public enum EvaluationError {
    INVALID_FORMAT ("Only positive numbers are allowed.");

    private final String message;

    EvaluationError(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return message;
    }
}
