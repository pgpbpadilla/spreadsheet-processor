package org.pgpb.evaluation;

import com.google.common.collect.ImmutableList;
import org.pgpb.spreadsheet.Cell;
import org.pgpb.spreadsheet.Spreadsheet;

import java.util.*;

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

    private String evaluateExpression(Spreadsheet sheet, String expression) {
        ExpressionTokenizer tokenizer = new ExpressionTokenizer();
        List<String> tokens = tokenizer.tokenize(expression);

        if (isReference(expression)) {
            String content = sheet.getCell(expression).getContent();
            if (content.startsWith("#")) {
                return content;
            }
            return evaluateText(sheet, content);
        }
        return evaluateTerm(expression);
    }

    private boolean isReference(String expression) {
        return Character.isAlphabetic(expression.charAt(0));
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
