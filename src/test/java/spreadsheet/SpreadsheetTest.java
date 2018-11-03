package spreadsheet;

import org.pgpb.spreadsheet.Spreadsheet;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class SpreadsheetTest {

    @Test
    public void testGetRows() {
        Spreadsheet sheet = null;

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("sample.tsv").getFile());
        try(Stream<String> contents = Files.lines(Paths.get(file.getPath()))) {

            sheet = new Spreadsheet(contents);
            assertEquals(sheet.getRows(), contents);
        } catch (IOException e) {
            fail();
        }

    }
}
