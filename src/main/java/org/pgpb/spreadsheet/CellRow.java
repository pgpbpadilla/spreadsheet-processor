package org.pgpb.spreadsheet;

import java.util.List;
import java.util.stream.Collectors;

public class CellRow {
    private List<Cell> cells;

    public CellRow(List<Cell> cells) {
        this.cells = cells;
    }

    @Override
    public String toString() {
        return cells.stream()
            .map(Cell::content)
            .collect(Collectors.joining("\\t"));
    }
}
