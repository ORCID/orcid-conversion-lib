package org.orcid.publiclib;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import javax.xml.bind.JAXBException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.devtools.common.options.OptionsParser;

/**
 * Simple app that takes an XML or JSON file and outputs the other version
 * Example usage:
 * 
 * - `java -jar orcid-public-lib-2.1-jar-with-dependencies.jar -i
 * ../src/test/resources/test-publiclib-record-2.0.xml`
 * 
 * - `java -jar orcid-public-lib-2.1-SNAPSHOT-jar-with-dependencies.jar -i
 * ../src/test/resources/test-publiclib-record-2.0.json -f json`
 * 
 * - `java -jar orcid-public-lib-2.1-SNAPSHOT-jar-with-dependencies.jar -i
 * ../src/test/resources/test-publiclib-record-2.0.json -f json -o
 * /tmp/record.xml`
 *
 */
public class App {
    private static OptionsParser optionsParser = OptionsParser.newOptionsParser(CommandLineOptions.class);

    /**
     * Example use of OrcidTranslatorV2. This will be slow if processing
     * multiple input files. Better to write a utility that parses them without
     * intialising OrcidTranslatorV2 more than once.
     * 
     * @param args
     */
    public static void main(String[] args) {
        if (args == null) {
            printUse();
            return;
        }
        optionsParser.parseAndExitUponError(args);
        CommandLineOptions options = optionsParser.getOptions(CommandLineOptions.class);
        try {
            OrcidTranslator t = new OrcidTranslator(options.schemaVersion);
            t.translate(Optional.ofNullable(options.fileName), Optional.ofNullable(options.outputFileName), options.inputFormat);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + options.fileName);
            return;
        } catch (JsonGenerationException e) {
            System.err.println("Could not write Json " + e);
            return;
        } catch (JsonMappingException e) {
            System.err.println("Could not read Json " + e);
            return;
        } catch (IOException e) {
            System.err.println("Could not read or write file " + e);
            return;
        } catch (JAXBException e) {
            System.err.println("Could not parse file " + e);
            return;
        }

    }

    public static void printUse() {
        System.out.println("Usage: java -jar orcid-public-lib.jar OPTIONS");
        System.out.println(optionsParser.describeOptions(Collections.<String, String> emptyMap(), OptionsParser.HelpVerbosity.LONG));
    }
}
