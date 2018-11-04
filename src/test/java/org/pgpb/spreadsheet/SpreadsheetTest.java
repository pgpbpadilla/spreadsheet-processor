package org.pgpb.spreadsheet;

import com.google.common.collect.ImmutableList;
import org.testng.annotations.Test;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;


public class SpreadsheetTest {
    @Test
    public void testFromTsvLines() {
        ImmutableList<String> lines = ImmutableList.of(
            "3\t4",
            "12\t=C2\t3\t'Sample",
            "A\tB\tC"
        );

        // Action
        Spreadsheet sheet = Spreadsheet.fromTsvLines(lines);

        assertThat(sheet.getRowCount()).isEqualTo(lines.size());
        int columnCount = lines.stream()
            .map(s -> s.split("\\t"))
            .map(cells -> cells.length)
            .max(Comparator.naturalOrder())
            .get();
        assertThat(sheet.columnCount()).isEqualTo(columnCount);
    }
}
