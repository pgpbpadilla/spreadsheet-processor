package org.pgpb.evaluation;

import com.google.common.collect.ImmutableList;
import org.pgpb.spreadsheet.Spreadsheet;

public class NoOpSpreadsheetEvaluator implements SpreadsheetEvaluator {
    @Override
    public String evaluateCell(Spreadsheet sheet, String address) {
        return sheet.getCell(address).getContent();
    }

    @Override
    public ImmutableList<String> toTSVLines(Spreadsheet sheet) {
        return sheet.toTSVLines();
    }
}
