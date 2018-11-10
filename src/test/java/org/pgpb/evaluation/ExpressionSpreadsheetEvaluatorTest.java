package org.pgpb.evaluation;

import com.google.common.collect.ImmutableList;
import org.pgpb.spreadsheet.Spreadsheet;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpressionSpreadsheetEvaluatorTest {

    @DataProvider(name = "termData")
    public Object[][] evaluateCellWithTerm(){
        return new Object[][] {
            {"", ""},
            {"1", "1"},
            {"=1", "1"},
            {"'Text", "Text"},
            {"-1", ValueError.NEGATIVE_NUMBER.toString()},
            {"=-1", ValueError.INVALID_EXPRESSION.toString()},
            {"A", ValueError.INVALID_FORMAT.toString()},
            {"=A", ValueError.INVALID_EXPRESSION.toString()},
            {"='Text", ValueError.INVALID_EXPRESSION.toString()},
        };
    }
    @Test(dataProvider = "termData")
    public void testEvaluateCellWithTerm(String content, String expected) {
        String [][] cells = new String[][] {{content}};
        Spreadsheet sheet = new Spreadsheet(1, 1);
        sheet.setCells(cells);

        SpreadsheetEvaluator evaluator = new ExpressionSpreadsheetEvaluator();
        assertThat(evaluator.evaluateCell(sheet, "A1")).isEqualTo(expected);
    }

    @Test
    public void testEvaluateCellReference() {
        String expected = "123";
        String [][] cells = new String[][] {{"=B1", expected}};
        Spreadsheet sheet = new Spreadsheet(1, 2);
        sheet.setCells(cells);

        SpreadsheetEvaluator evaluator = new ExpressionSpreadsheetEvaluator();
        assertThat(evaluator.evaluateCell(sheet, "A1")).isEqualTo(expected);
    }

    @Test
    public void testEvaluateCellNestedReference(){
        String [][] cells = new String[][] {{"=B1", "=C1", "=1"}};
        Spreadsheet sheet = new Spreadsheet(1, 3);
        sheet.setCells(cells);

        SpreadsheetEvaluator evaluator = new ExpressionSpreadsheetEvaluator();
        assertThat(evaluator.evaluateCell(sheet, "A1")).isEqualTo("1");
    }

    @DataProvider(name = "operationsData")
    public Object[][] operationsData(){
        return new Object[][] {
            {
                new String [][] {
                    {"=2+4"}
                },
                "6"
            },
            {
                new String [][] {
                    {"=5-4"}
                },
                "1"
            },
            {
                new String [][] {
                    {"=5*4"}
                },
                "20"
            },
            {
                new String [][] {
                    {"=20/4"}
                },
                "5"
            },
            {
                new String [][] {
                    {"=1+2-3*3/3"}
                },
                "0"
            },
            {
                new String [][] {
                    {"=20/5*3+2-1"}
                },
                "13"
            }
        };
    }
    @Test(dataProvider = "operationsData")
    public void testEvaluateCellOperation(String [][] cells, String expected) {
        Spreadsheet sheet = new Spreadsheet(1, 1);
        sheet.setCells(cells);

        SpreadsheetEvaluator evaluator = new ExpressionSpreadsheetEvaluator();
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
        SpreadsheetEvaluator evaluator = new ExpressionSpreadsheetEvaluator();
        assertThat(evaluator.toTSVLines(sheet)).isEqualTo(expected);
    }
}