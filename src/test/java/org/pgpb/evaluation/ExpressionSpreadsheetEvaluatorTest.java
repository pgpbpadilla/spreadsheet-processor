package org.pgpb.evaluation;

import com.google.common.collect.ImmutableList;
import org.pgpb.spreadsheet.Cell;
import org.pgpb.spreadsheet.Spreadsheet;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class ExpressionSpreadsheetEvaluatorTest {

    @DataProvider(name = "termData")
    public Object[][] evaluateCellWithTerm(){
        return new Object[][] {
            {"", ""},
            {"1", "1"},
            {"=1", "1"},
            {"'Text", "Text"},
            {"-1", "#" + ValueError.NEGATIVE_NUMBER},
            {"=-1", "#" + ValueError.INVALID_EXPRESSION},
            {"A", "#" + ValueError.INVALID_FORMAT},
            {"=A", "#" + ValueError.INVALID_EXPRESSION},
            {"='Text", "#" + ValueError.INVALID_EXPRESSION},
        };
    }
    @Test(dataProvider = "termData")
    public void testEvaluateCellWithTerm(String content, String expected) {
        Cell [][] cells = new Cell[][] {
            {new Cell(content)}
        };
        Spreadsheet sheet = new Spreadsheet(1, 1);
        sheet.setCells(cells);

        ExpressionSpreadsheetEvaluator evaluator = new ExpressionSpreadsheetEvaluator();
        assertThat(evaluator.evaluateCell(sheet, "A1")).isEqualTo(expected);
    }

    @Test
    public void testEvaluateCellReference() {
        String expected = "123";
        Cell [][] cells = new Cell[][] {
            {new Cell("=B1"), new Cell(expected)}
        };
        Spreadsheet sheet = new Spreadsheet(1, 2);
        sheet.setCells(cells);

        ExpressionSpreadsheetEvaluator evaluator = new ExpressionSpreadsheetEvaluator();
        assertThat(evaluator.evaluateCell(sheet, "A1")).isEqualTo(expected);
    }

    @Test
    public void testEvaluateCellNestedReference(){
        Cell [][] cells = new Cell[][] {
            {new Cell("=B1"), new Cell("=C1"), new Cell("=1")}
        };
        Spreadsheet sheet = new Spreadsheet(1, 3);
        sheet.setCells(cells);

        ExpressionSpreadsheetEvaluator evaluator = new ExpressionSpreadsheetEvaluator();
        assertThat(evaluator.evaluateCell(sheet, "A1")).isEqualTo("1");
    }

    @DataProvider(name = "operationsData")
    public Object[][] operationsData(){
        return new Object[][] {
            {
                new Cell [][] {
                    {new Cell("=2+4")}
                },
                "6"
            },
            {
                new Cell [][] {
                    {new Cell("=5-4")}
                },
                "1"
            },
            {
                new Cell [][] {
                    {new Cell("=5*4")}
                },
                "20"
            },
            {
                new Cell [][] {
                    {new Cell("=20/4")}
                },
                "5"
            },
            {
                new Cell [][] {
                    {new Cell("=1+2-3*3/3")}
                },
                "0"
            },
            {
                new Cell [][] {
                    {new Cell("=20/5*3+2-1")}
                },
                "13"
            }
        };
    }
    @Test(dataProvider = "operationsData")
    public void testEvaluateCellOperation(Cell [][] cells, String expected) {
        Spreadsheet sheet = new Spreadsheet(1, 1);
        sheet.setCells(cells);

        ExpressionSpreadsheetEvaluator evaluator = new ExpressionSpreadsheetEvaluator();
        assertThat(evaluator.evaluateCell(sheet, "A1")).isEqualTo(expected);
    }

    @DataProvider(name = "toTSVLinesData")
    public Object [][] toTSVLinesData(){
        return new Object[][] {
            {
                ImmutableList.of(
                    "1\t1",
                    "1"
                ),
                ImmutableList.of("1")
            },
            {
                ImmutableList.of(
                    "2\t2",
                    "1\t2",
                    "3\t4"
                ),
                ImmutableList.of(
                    "1\t2",
                    "3\t4"
                )
            },
            {
                ImmutableList.of(
                    "2\t2",
                    "=B1\t10",
                    "30\t=A2"
                ),
                ImmutableList.of(
                    "10\t10",
                    "30\t30"
                )
            }
        };
    }

    @Test(dataProvider = "toTSVLinesData")
    public void testEvaluateSheet(
        List<String> inputLines,
        List<String> expected
    ) {
        Spreadsheet sheet = Spreadsheet.fromTsvLines(inputLines);
        ExpressionSpreadsheetEvaluator evaluator = new ExpressionSpreadsheetEvaluator();
        assertThat(evaluator.toTSVLines(sheet)).isEqualTo(expected);
    }
}