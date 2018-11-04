package org.pgpb.evaluation;

import org.pgpb.spreadsheet.Cell;
import org.pgpb.spreadsheet.CellRow;
import org.pgpb.spreadsheet.Spreadsheet;

import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class SpreadsheetEvaluator {
    public static String evaluate(Cell cell) {
        return ExpressionEvaluator.evaluate(cell.getContent());
    }

    public static String evaluate(CellRow row) {
        return row.getCells().stream()
            .map(SpreadsheetEvaluator::evaluate)
            .collect(joining("\t"));
    }

    public static List<String> evaluate(List<CellRow> range) {
        return range.stream()
            .map(SpreadsheetEvaluator::evaluate)
            .collect(toList());
    }

    public static List<String> evaluate(Spreadsheet sheet) {
        return SpreadsheetEvaluator.evaluate(sheet.getRows());
    }
}
