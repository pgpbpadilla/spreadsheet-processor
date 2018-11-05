package org.pgpb.evaluation;

import org.pgpb.spreadsheet.Cell;
import org.pgpb.spreadsheet.Spreadsheet;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class SpreadsheetEvaluator {
    public static String evaluate(Cell cell) {
        return ExpressionEvaluator.evaluate(cell.getContent());
    }

    public static List<String> evaluate(Spreadsheet sheet) {
        return Collections.emptyList();
    }

    public static String evaluate(Spreadsheet sheet, String address) {
        String content = sheet.getCell(address).getContent();
        return ExpressionEvaluator.evaluate(content);
    }
}
