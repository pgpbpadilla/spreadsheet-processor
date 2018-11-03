package spreadsheet;

import com.google.common.collect.ImmutableList;
import org.pgpb.spreadsheet.Spreadsheet;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;
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

            ImmutableList<String> lines =
                contents.collect(ImmutableList.toImmutableList());
            sheet = new Spreadsheet(lines);

            List<String> actual = sheet.toTSVRows();
            List<String> expected = lines;

            assertEquals(actual.size(), expected.size());
            IntStream.range(0, actual.size())
                .forEach(
                    i -> assertEquals(actual.get(i), expected.get(i))
                );
        } catch (IOException e) {
            fail();
        }

    }
}
