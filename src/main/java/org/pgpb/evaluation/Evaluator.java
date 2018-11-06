package org.pgpb.evaluation;

import com.google.common.collect.ImmutableList;
import org.pgpb.spreadsheet.Spreadsheet;

public interface Evaluator {
    public String evaluateCell(Spreadsheet sheet, String address);

    ImmutableList<String> toTSVLines(Spreadsheet sheet);
}
