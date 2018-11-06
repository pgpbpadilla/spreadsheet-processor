package org.pgpb.evaluation;

import com.google.common.collect.ImmutableList;
import org.pgpb.spreadsheet.Cell;
import org.pgpb.spreadsheet.Spreadsheet;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class ExpressionEvaluatorTest {

    @DataProvider(name = "termData")
    public Object[][] evaluateCellWithTerm(){
        return new Object[][] {
            {"", ""},
            {"1", "1"},
            {"=1", "1"},
            {"'Text", "Text"},
            {"-1", "#" + ExpressionEvaluationError.NEGATIVE_NUMBER},
            {"=-1", "#" + ExpressionEvaluationError.NEGATIVE_NUMBER},
            {"A", "#" + ExpressionEvaluationError.INVALID_FORMAT},
            {"=A", "#" + ExpressionEvaluationError.INVALID_FORMAT},
            {"='Text", "#" + ExpressionEvaluationError.INVALID_FORMAT},
        };
    }
    @Test(dataProvider = "termData")
    public void testEvaluateCellWithTerm(String content, String expected) {
        Cell [][] cells = new Cell[][] {
            {new Cell(content)}
        };
        Spreadsheet sheet = new Spreadsheet(1, 1);
        sheet.setCells(cells);

        ExpressionEvaluator evaluator = new ExpressionEvaluator();
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

        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        assertThat(evaluator.evaluateCell(sheet, "A1")).isEqualTo(expected);
    }

    @Test
    public void testEvaluateCellNestedReference(){
        throw new NotImplementedException();
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
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        assertThat(evaluator.toTSVLines(sheet)).isEqualTo(expected);
    }
}