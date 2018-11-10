package org.pgpb.evaluation;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class ExpressionTokenizer {
    public static ImmutableList<Character> OPERATIONS =
        ImmutableList.of('+', '-', '*', '/');

    public List<String> tokenize(String expression) {
        Operator operator = nextOperator(expression);
        if (operator.value == '\0') {
            return ImmutableList.of(expression);
        }

        List<String> tokens = new ArrayList<>();
        tokens.add(expression.substring(0, operator.position));
        tokens.add(String.valueOf(operator.value));
        String remainder = expression.substring(operator.position + 1);
        tokens.addAll(tokenize(remainder));

        return tokens;
    }

    private Operator nextOperator(String expression) {
        for (char c : expression.toCharArray()) {
            if (OPERATIONS.contains(Character.valueOf(c))) {
                return new Operator(expression.indexOf(c), c);
            }
        }
        return new Operator(0, '\0');
    }

    public class Operator {
        private final int position;
        private final char value;

        public Operator(int position, char value) {
            this.position = position;
            this.value = value;
        }
    }
}
