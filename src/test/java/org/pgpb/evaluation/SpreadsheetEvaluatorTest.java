package org.pgpb.evaluation;

import com.google.common.collect.ImmutableList;
import org.pgpb.spreadsheet.Cell;
import org.pgpb.spreadsheet.CellRow;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SpreadsheetEvaluatorTest {

    @Test
    public void testEvaluate() {
        List<Cell> cells = ImmutableList.of(
            new Cell("1"),
            new Cell("2"),
            new Cell("3"),
            new Cell("-1"),
            new Cell("ABC")
        );
        CellRow row = new CellRow(cells);
        String expected = "1\t2\t3\t#"
            + String.valueOf(EvaluationError.INVALID_FORMAT)
            + "\t#" + String.valueOf(EvaluationError.INVALID_FORMAT);
        assertThat(SpreadsheetEvaluator.evaluate(row))
            .isEqualTo(expected);
    }
}