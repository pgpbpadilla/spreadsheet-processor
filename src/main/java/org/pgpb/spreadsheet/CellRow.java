package org.pgpb.spreadsheet;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CellRow {
    private List<Cell> cells;

    public CellRow(List<Cell> cells) {
        this.cells = cells;
    }

    public static CellRow fromTsvLine(String tsvLine) {
        String[] cellContents = tsvLine.split("\\t");
        List<Cell> cells = Arrays.stream(cellContents)
            .map(Cell::new)
            .collect(toList());
        return new CellRow(cells);
    }

    public List<Cell> getCells() {
        return cells;
    }
}
