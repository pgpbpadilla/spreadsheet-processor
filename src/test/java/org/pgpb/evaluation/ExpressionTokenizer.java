package org.pgpb.evaluation;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class ExpressionTokenizer {
    ImmutableList<Character> OPERATIONS = ImmutableList.of('+', '-', '*', '/');

    public List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();

        Operator operator = nextOperator(expression);
        tokens.add(expression.substring(0, operator.position));
        tokens.add(String.valueOf(operator.value));
        tokens.add(expression.substring(operator.position + 1));

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
