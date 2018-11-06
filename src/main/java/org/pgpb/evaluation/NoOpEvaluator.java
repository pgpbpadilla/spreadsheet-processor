package org.pgpb.evaluation;

import com.google.common.collect.ImmutableList;
import org.pgpb.spreadsheet.Spreadsheet;

import java.util.Collections;

public class NoOpEvaluator implements Evaluator {
    @Override
    public String evaluateCell(Spreadsheet sheet, String address) {
        return sheet.getCell(address).getContent();
    }

    @Override
    public ImmutableList<String> evaluateSheet(Spreadsheet sheet) {
        return sheet.toTSVLines();
    }
}
