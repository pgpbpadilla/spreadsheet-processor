package org.pgpb.spreadsheet;

import com.google.common.collect.ImmutableList;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class SpreadsheetTest {

    @Test
    public void testSpreadsheet() {
        Spreadsheet sheet = new Spreadsheet(1, 1);

        assertThat(sheet.getRowCount()).isEqualTo(1);
        assertThat(sheet.getColumnCount()).isEqualTo(1);
    }

    @Test
    public void testGetCell() {
        Spreadsheet sheet = new Spreadsheet(1, 1);
        Cell[][] cells = {
            {new Cell("1"), new Cell("2")},
            {new Cell("3"), new Cell("4")},
            {new Cell("5"), new Cell("6")}
        };
        sheet.setCells(cells);

        assertThat(sheet.getCell("A1").getContent()).isEqualTo("1");
        assertThat(sheet.getCell("B3").getContent()).isEqualTo("6");
    }

    @DataProvider(name = "tsvLines")
    public Object [][] tsvLines() {
        return new Object[][] {
            {
                1, 1, // Row-, Column- count
                ImmutableList.of(
                    "1\t1",
                    "12"
                )
            },
            {
                2, 4, // Row-, Column- count
                ImmutableList.of(
                    "2\t4",
                    "12\t=C2\t3\t'Sample",
                    "A\tB\tC\tD"
                )
            },
//            {   // TODO: Input with empty cells
//                2, 2, // Row-, Column- count
//                ImmutableList.of(
//                    "2\t2",
//                    "12",
//                    "\t10"
//                )
//            }
        };
    }

    @Test(dataProvider = "tsvLines")
    public void testFromTsvLines(
        int expectedRowCount,
        int expectedColumnCount,
        List<String> lines
    ) {
        // Action
        Spreadsheet sheet = Spreadsheet.fromTsvLines(lines);

        assertThat(sheet.getRowCount()).isEqualTo(expectedRowCount);
        assertThat(sheet.getColumnCount()).isEqualTo(expectedColumnCount);
    }

    @DataProvider(name = "tsvLinesBadHeader")
    public Object[][] tsvLinesBadHeader() {
        return new Object[][] {
            {ImmutableList.of("-2\t2")},
            {ImmutableList.of("2\tA")}
        };
    }

    @Test(
        dataProvider = "tsvLinesBadHeader",
        expectedExceptions = RuntimeException.class
    )
    public void testFromTsvLinesBadHeader(ImmutableList<String> header) {
        Spreadsheet.fromTsvLines(header);
    }
}
