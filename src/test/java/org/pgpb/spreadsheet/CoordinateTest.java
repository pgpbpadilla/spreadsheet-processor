package org.pgpb.spreadsheet;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CoordinateTest {

    @DataProvider(name = "addresses")
    public Object [][] addressData() {
        return new Object[][] {
            {"A1", new Coordinate(0, 0)},
            {"B2", new Coordinate(1, 1)},
            {"Z1", new Coordinate(0, 25)},
            {"AB1", new Coordinate(0, 27)}
        };
    }
    @Test(dataProvider = "addresses")
    public void testFromAddress(String address, Coordinate expected) {
        Coordinate coordinate = Coordinate.fromAddress(address);
        assertThat(coordinate.row).isEqualTo(expected.row);
        assertThat(coordinate.column).isEqualTo(expected.column);
    }

    @DataProvider(name = "columns")
    public Object [][] columnData() {
        return new Object[][] {
            {"A", 0},
            {"B", 1},
            {"Z", 25},
            {"AA", 26},
            {"ABC", 54}
        };
    }
    @Test(dataProvider = "columns")
    public void testConvertToIndex(String name, int expected) {
        assertThat(Coordinate.toIndex(name)).isEqualTo(expected);
    }
}