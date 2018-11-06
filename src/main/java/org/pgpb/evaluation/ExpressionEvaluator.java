package org.pgpb.evaluation;

import com.google.common.collect.ImmutableList;
import org.pgpb.spreadsheet.Cell;
import org.pgpb.spreadsheet.Spreadsheet;

import java.util.Arrays;
import java.util.List;

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

        if (text.charAt(0) == '\'') {
            return text.substring(1);
        }

        if (text.charAt(0) == '=') {
            return evaluateExpression(sheet, text.substring(1));
        }

        return evaluateTerm(text);
    }

    private String evaluateExpression(
        Spreadsheet sheet,
        String expression
    ) {
        if (Character.isAlphabetic(expression.charAt(0))){
            return sheet.getCell(expression).getContent();
        }
        return evaluateTerm(expression);
    }

    private static String evaluateTerm(String term) {
        try {
            Integer value = Integer.parseInt(term);
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
