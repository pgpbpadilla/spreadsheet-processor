package org.pgpb.evaluation;

import com.google.common.collect.ImmutableList;
import org.pgpb.spreadsheet.Cell;
import org.pgpb.spreadsheet.Spreadsheet;

public class ExpressionEvaluator implements Evaluator {

    @Override
    public String evaluateCell(Spreadsheet sheet, String address) {
        Cell cell = sheet.getCell(address);
        return evaluateText(sheet, cell.getContent());
    }

    @Override
    public ImmutableList<String> evaluateSheet(Spreadsheet sheet) {
        return ImmutableList.of();
    }

    private static String evaluateText(Spreadsheet sheet, String text) {
        if ("".equals(text)) {
            return text;
        }

        if (text.charAt(0) == '\'') {
            return text.substring(1);
        }

        if (text.charAt(0) == '=') {
            return ExpressionEvaluator.evaluateExpression(text);
        }

        return ExpressionEvaluator.evaluateTerm(text);
    }

    private static String evaluateExpression(String expression) {
        String e = expression.substring(1);
        return evaluateTerm(e);
    }

    private static String evaluateTerm(String term) {
        try {
            Integer value = Integer.parseInt(term);
            if (value < 0) {
                return formatError(String.valueOf(ExpressionEvaluationError.NEGATIVE_NUMBER));
            }
            return term;
        } catch (NumberFormatException e) {
            return formatError(String.valueOf(ExpressionEvaluationError.INVALID_FORMAT));
        }
    }

    private static String formatError(String message) {
        return "#" + message;
    }
}
