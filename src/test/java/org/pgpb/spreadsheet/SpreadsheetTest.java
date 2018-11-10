package org.pgpb.spreadsheet;

import com.google.common.collect.ImmutableList;
import org.pgpb.evaluation.ValueError;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class SpreadsheetTest {

    @DataProvider(name = "dimensionsData")
    public Object [][] dimensionsData() {
        return new Object[][]{
            {0, 0, new Spreadsheet.Dimensions(0, 0)},
            {0, 1, new Spreadsheet.Dimensions(0, 0)},
            {1, 0, new Spreadsheet.Dimensions(0, 0)},
            {1, 1, new Spreadsheet.Dimensions(1, 1)}
        };
    }

    @Test(dataProvider = "dimensionsData")
    public void testSpreadsheet(
        int rows,
        int columns,
        Spreadsheet.Dimensions expected
    ) {
        Spreadsheet sheet = new Spreadsheet(rows, columns);

        assertThat(sheet.getRowCount()).isEqualTo(expected.rows);
        assertThat(sheet.getColumnCount()).isEqualTo(expected.columns);
    }

    @Test
    public void testGetCell() {
        Spreadsheet sheet = new Spreadsheet(1, 1);
        String[][] cells = {
            {"1", "2"},
            {"3", "4"},
            {"5", "6"}
        };
        sheet.setCells(cells);

        assertThat(sheet.getContent("A1")).isEqualTo("1");
        assertThat(sheet.getContent("B3")).isEqualTo("6");
    }

    @Test
    public void testGetCellInvalidAddress() {
        Spreadsheet sheet = new Spreadsheet(0, 0);
        String content = sheet.getContent("InvalidAddress");
        String expected = ValueError.INVALID_ADDRESS_FORMAT.toString();
        assertThat(content).isEqualTo(expected);
    }

    @Test
    public void testGetCellNotFound() {
        Spreadsheet sheet = new Spreadsheet(0, 0);
        String actual = sheet.getContent("A1");
        assertThat(actual).isEqualTo(ValueError.CELL_NOT_FOUND.toString());
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


    @DataProvider(name = "cellData")
    public Object [][] cellData(){
        return new Object[][]{
            {
                new String[][]{{"1"}},
                ImmutableList.of("1")
            },
            {
                new String[][]{{"1", "=1*A"}},
                ImmutableList.of("1\t=1*A")
            },
            {
                new String[][]{
                    {"1", "=1*A"},
                    {"-10", "B3"}
                },
                ImmutableList.of(
                    "1\t=1*A",
                    "-10\tB3"
                )
            }
        };
    }
    @Test(dataProvider = "cellData")
    public void testToTSVLines(String [][] cells, List<String> expected) {
        Spreadsheet sheet = new Spreadsheet(1, 1);
        sheet.setCells(cells);
        assertThat(sheet.toTSVLines()).isEqualTo(expected);
    }
}
