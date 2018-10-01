package org.orcid.conversionlib;

import com.google.devtools.common.options.EnumConverter;
import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;

public class CommandLineOptions extends OptionsBase {
    public enum InputFormat {
        XML, JSON
    }

    public static class InputFormatConverter extends EnumConverter<InputFormat> {

        public InputFormatConverter() {
            super(InputFormat.class, "input format");
        }
    }
    
    public static class SchemaVersionConverter extends EnumConverter<SchemaVersion> {

        public SchemaVersionConverter() {
            super(SchemaVersion.class, "schema version");
        }
    }

    @Option(name = "schema-version", help = "the schema version of the source file", abbrev = 'v', defaultValue = "v2_1", converter = SchemaVersionConverter.class)
    public SchemaVersion schemaVersion;

    @Option(name = "format", help = "the format of the source file", abbrev = 'f', defaultValue = "xml", converter = InputFormatConverter.class)
    public InputFormat inputFormat;

    @Option(name = "input-file", help = "Location of the input file (optional, can pipe in)", abbrev = 'i', defaultValue = "")
    public String fileName;

    @Option(name = "output-file", help = "Location of the output file (optional, can pipe out)", abbrev = 'o', defaultValue = "")
    public String outputFileName;
    
    @Option(name = "schema-validate", help = "If true, validate XML input and output against the schema", abbrev = 's', defaultValue = "false")
    public boolean schemaValidate;
    
    @Option(name = "tarball", help = "If true, input is a tar.gz containing XML, output is tar.gz containing JSON", abbrev = 't', defaultValue = "false")
    public boolean tarball;

}
