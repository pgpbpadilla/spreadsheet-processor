package org.pgpb.evaluation;

import com.google.common.collect.ImmutableList;
import org.pgpb.spreadsheet.Spreadsheet;

public interface SpreadsheetEvaluator {
    String evaluateCell(Spreadsheet sheet, String address);

    ImmutableList<String> toTSVLines(Spreadsheet sheet);
}
