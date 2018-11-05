package org.pgpb.evaluation;

import org.pgpb.spreadsheet.Cell;
import org.pgpb.spreadsheet.Spreadsheet;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpreadsheetEvaluatorTest {

    @DataProvider(name = "evaluateCellDataSingleTerm")
    public Object[][] evaluateCellDataSingleTerm(){
        return new Object[][] {
            {"", ""},
            {"1", "1"},
            {"=1", "1"},
            {"'Text", "Text"},
            {"-1", "#" + EvaluationError.NEGATIVE_NUMBER},
            {"=-1", "#" + EvaluationError.NEGATIVE_NUMBER},
            {"A", "#" + EvaluationError.INVALID_FORMAT},
            {"=A", "#" + EvaluationError.INVALID_FORMAT},
            {"='Text", "#" + EvaluationError.INVALID_FORMAT},
        };
    }
    @Test(dataProvider = "evaluateCellDataSingleTerm")
    public void testEvaluateCellSingleTerm(String content, String expected) {
        Cell [][] cells = new Cell[][] {
            {new Cell(content)}
        };
        Spreadsheet sheet = new Spreadsheet(1, 1);
        sheet.setCells(cells);

        assertThat(SpreadsheetEvaluator.evaluate(sheet, "A1")).isEqualTo(expected);
    }

    @Test
    public void testEvaluateCellReference() {
        String expected = "123";
        Cell [][] cells = new Cell[][] {
            {new Cell("=B1"), new Cell(expected)}
        };
        Spreadsheet sheet = new Spreadsheet(1, 2);
        sheet.setCells(cells);

        assertThat(SpreadsheetEvaluator.evaluate(sheet, "A1")).isEqualTo(expected);
    }
}