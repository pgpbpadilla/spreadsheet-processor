package org.pgpb.spreadsheet;

import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CellRowTest {

    @Test
    public void testFromTsvLine() {
        CellRow row = CellRow.fromTsvLine("1\t2\t3");
        assertThat(row.getCells().size()).isEqualTo(3);
    }
}