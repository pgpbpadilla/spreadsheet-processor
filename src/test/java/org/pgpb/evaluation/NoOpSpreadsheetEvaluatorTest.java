package org.pgpb.evaluation;

import com.google.common.collect.ImmutableList;
import org.pgpb.spreadsheet.Spreadsheet;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NoOpSpreadsheetEvaluatorTest {

    @Test
    public void testEvaluateCell() {
        Spreadsheet sheet = new Spreadsheet(1, 1);
        String [][] cells = new String[][] {{"1"}};
        sheet.setCells(cells);
        NoOpSpreadsheetEvaluator evaluator = new NoOpSpreadsheetEvaluator();

        assertThat(evaluator.evaluateCell(sheet, "A1")).isEqualTo("1");
    }

    @Test
    public void testToTSVLines() {
        Spreadsheet sheet = new Spreadsheet(1, 1);
        String [][] cells = new String[][] {
            {"1", "-2"},
            {"=1*B", "A"}
        };
        sheet.setCells(cells);
        ImmutableList<String> tsvLines = ImmutableList.of(
            "1\t-2",
            "=1*B\tA"
        );
        NoOpSpreadsheetEvaluator evaluator = new NoOpSpreadsheetEvaluator();
        assertThat(evaluator.toTSVLines(sheet)).isEqualTo(tsvLines);
    }
}