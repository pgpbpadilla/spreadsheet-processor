package spreadsheet;

import org.pgpb.spreadsheet.Spreadsheet;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class SpreadsheetTest {
    @Test
    public void testSomeLibraryMethod() {
        Spreadsheet classUnderTest = new Spreadsheet();
        assertTrue(classUnderTest.someLibraryMethod(), "Message");
    }
}
