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

    private static String evaluateExpression(
        Spreadsheet sheet,
        String expression
    ) {
        ExpressionTokenizer tokenizer = new ExpressionTokenizer();
        List<String> tokens = tokenizer.tokenize(expression);
        List<String> tokenValues = tokens.stream()
            .map(t -> evaluateToken(sheet, t))
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
                double b = valuesStack.pop();
                double a = valuesStack.pop();
                String operation = operatorsStack.pop();
                valuesStack.push(evaluateOperation(operation, a, b));
                continue;
            }
        }
        return String.valueOf(valuesStack.pop());
    }

    private double evaluateOperation(String operation, double a, double b) {
        if (operation.equals("+")){
            return a+b;
        }
        if (operation.equals("-")){
            return a-b;
        }
        if (operation.equals("*")){
            return a*b;
        }
        if (operation.equals("/")){
            return a/b;
        }
        throw new RuntimeException(
            String.valueOf(ExpressionError.UNSUPPORTED_OPERATION)
        );
    }

    private static boolean isOperator(String token) {
        if (token.length() == 1) {
            return ExpressionTokenizer.OPERATIONS.contains(
                token.toCharArray()[0]
            );
        }
        return false;
    }
    }

    private static String evaluateToken(Spreadsheet sheet, String token) {
        if (isOperator(token)) {
            return token;
        }
        return evaluateTerm(sheet, token);
    }

    private static String evaluateTerm(Spreadsheet sheet, String term) {
        if ("".equals(term)) {
            return ExpressionEvaluator.formatError(
                ExpressionError.INVALID_FORMAT
            );
        }
        if (isReference(term)) {
            return evaluateReference(sheet, term);
        }
        return evaluateNonNegativeInteger(term);
    }

    private static String evaluateNonNegativeInteger(String term) {
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

    private static String evaluateReference(Spreadsheet sheet, String address) {
        String content = sheet.getCell(address).getContent();
        if (content.startsWith("#")) {
            return content;
        }
        return evaluateText(sheet, content);
    }

    public static String formatError(ExpressionError error) {
        return "#" + String.valueOf(error);
    }
}
