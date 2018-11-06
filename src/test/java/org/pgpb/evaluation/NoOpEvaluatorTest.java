package org.pgpb.evaluation;

import com.google.common.collect.ImmutableList;
import org.pgpb.spreadsheet.Cell;
import org.pgpb.spreadsheet.Spreadsheet;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.*;

public class NoOpEvaluatorTest {

    @Test
    public void testEvaluateCell() {
        Spreadsheet sheet = new Spreadsheet(1, 1);
        Cell [][] cells = new Cell[][] {
            {new Cell("1")}
        };
        sheet.setCells(cells);
        NoOpEvaluator evaluator = new NoOpEvaluator();

        assertThat(evaluator.evaluateCell(sheet, "A1")).isEqualTo("1");
    }

    @Test
    public void testEvaluateSheet() {
        Spreadsheet sheet = new Spreadsheet(1, 1);
        Cell [][] cells = new Cell[][] {
            {new Cell("1"), new Cell("-2")},
            {new Cell("=1*B"), new Cell("A")}
        };
        sheet.setCells(cells);
        NoOpEvaluator evaluator = new NoOpEvaluator();

        ImmutableList<String> tsvLines = ImmutableList.of(
            "1\t-2",
            "=1*B\tA"
        );

        assertThat(evaluator.evaluateSheet(sheet)).isEqualTo(tsvLines);
    }
}