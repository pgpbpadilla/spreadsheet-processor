package org.pgpb.evaluation;

import com.google.common.collect.ImmutableList;

class Operator {
    public final int position;
    public final char value;

    public Operator(int position, char value) {
        this.position = position;
        this.value = value;
    }

    private static final ImmutableList<Character> ALLOWED_OPERATORS =
        ImmutableList.of('+', '-', '*', '/');

    public static int evaluate(char operator, int a, int b) {
        if ('+' == operator) {
            return a + b;
        }
        if ('-' == operator) {
            return a - b;
        }
        if ('*' == operator) {
            return a * b;
        }
        if ('/' == operator) {
            return a / b;
        }
        throw new RuntimeException(
            String.valueOf(ValueError.UNSUPPORTED_OPERATION)
        );
    }

    public static boolean isValid(char operator) {
        return ALLOWED_OPERATORS.contains(operator);
    }
}
