package org.orcid.publiclib;

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

    @Option(name = "format", help = "the format of the source file", abbrev = 'f', defaultValue = "xml", converter = InputFormatConverter.class)
    public InputFormat inputFormat;

    @Option(name = "input-file", help = "Location of the input file (optional, can pipe in)", abbrev = 'i', defaultValue = "")
    public String fileName;

    @Option(name = "output-file", help = "Location of the output file (optional, can pipe out)", abbrev = 'o', defaultValue = "")
    public String outputFileName;
}
