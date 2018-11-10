package org.pgpb.evaluation;

import com.google.common.collect.ImmutableList;
import org.pgpb.spreadsheet.Cell;
import org.pgpb.spreadsheet.Spreadsheet;

import java.util.*;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class ExpressionEvaluator implements Evaluator {

    @Override
    public String evaluateCell(Spreadsheet sheet, String address) {
        Cell cell = sheet.getCell(address);
        return evaluateText(sheet, cell.getContent());
    }

    @Override
    public ImmutableList<String> toTSVLines(Spreadsheet sheet) {
        List<Cell[]> rows = sheet.getRows();
        return rows.stream()
            .map(cells -> Arrays.asList(cells).stream()
                .map(Cell::getContent)
                .map(content -> evaluateText(sheet, content))
                .collect(toList()))
            .map(strings -> String.join("\t", strings))
            .collect(ImmutableList.toImmutableList());
    }

    private String evaluateText(Spreadsheet sheet, String text) {
        if ("".equals(text)) {
            return text;
        }

        if (isTextLabel(text)) {
            return text.substring(1);
        }

        if (isOperator(text)) {
            return text;
        }

        if (isExpression(text)) {
            return evaluateExpression(sheet, text.substring(1));
        }

        return evaluateTerm(text);
    }

    private boolean isExpression(String text) {
        return text.charAt(0) == '=';
    }

    private boolean isTextLabel(String text) {
        return isTextLabel(text, '\'');
    }

    private boolean isTextLabel(String text, char c) {
        return text.charAt(0) == c;
    }

    private String evaluateExpression(Spreadsheet sheet, String expression) {
        if (isReference(expression)) {
            String content = sheet.getCell(expression).getContent();
            if (content.startsWith("#")) {
                return content;
            }
            return evaluateText(sheet, content);
        }

        ExpressionTokenizer tokenizer = new ExpressionTokenizer();
        List<String> tokens = tokenizer.tokenize(expression);
        List<String> tokenValues = tokens.stream()
            .map(t -> evaluateText(sheet, t))
            .collect(toList());
        Predicate<String> evalError = s -> s.startsWith("#");
        boolean hasErrors = tokenValues.stream()
            .anyMatch(evalError);

        if (hasErrors) {
            return formatError(ExpressionError.INVALID_EXPRESSION);
        }

        Deque<Double> valuesStack = new ArrayDeque<>();
        Deque<String> operatorsStack = new ArrayDeque<>();
        for (String tv : tokenValues) {
            if (isOperator(tv)) {
                operatorsStack.push(tv);
                continue;
            }

            valuesStack.push(Double.parseDouble(tv));

            if (!operatorsStack.isEmpty()) {
                double a = valuesStack.pop();
                double b = valuesStack.pop();
                String operation = operatorsStack.pop();
                valuesStack.push(evaluateOperation(operation, a, b));
                continue;
            }
        }
        return String.valueOf(valuesStack.pop());
    }

    private double evaluateOperation(String operation, double a, double b) {
        return a+b;
    }

    private boolean isOperator(String token) {
        return ExpressionTokenizer.OPERATIONS.contains(token.toCharArray()[0]);
    }

    private boolean isReference(String expression) {
        return Character.isAlphabetic(expression.charAt(0));
    }

    private static String evaluateTerm(String term) {
        try {
            Double value = Double.parseDouble(term);
            if (value < 0) {
                return formatError(ExpressionError.NEGATIVE_NUMBER);
            }
            return term;
        } catch (NumberFormatException e) {
            return formatError(ExpressionError.INVALID_FORMAT);
        }
    }

    public static String formatError(ExpressionError error) {
        return "#" + String.valueOf(error);
    }
}
