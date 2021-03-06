package org.orcid.conversionlib;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;
import org.orcid.conversionlib.App;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void testOptions() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        App.main(null);
        assertTrue(outContent.toString().contains("java -jar orcid-public-lib.jar OPTIONS"));
    }

}
