package org.pgpb.spreadsheet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Coordinate {
    public final int row;
    public final int column;

    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public static Coordinate fromAddress(String address) {
        Pattern pattern = Pattern.compile("^([A-Z]+)(\\d+)$");
        Matcher m = pattern.matcher(address);

        if(!m.find()) {
            throw new RuntimeException("Invalid address.");
        }

        int row = Integer.parseInt(m.group(2)) - 1;
        int column = toIndex(m.group(1));

        return new Coordinate(row, column);
    }

    public static int toIndex(String column) {
        int result = 26 * (column.length() - 1);
        result += column.charAt(column.length() - 1) - 'A';
        return result;
    }
}
