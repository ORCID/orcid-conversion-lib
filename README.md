# ORCID CONVERSION UTIL

Simple application that takes an ORCID XML or JSON file and outputs the other version to the console or a file.  Available as a stand-alone executable jar, which can can be found in the /target directory. The application is designed to work with full ORCID record metadata.  Partial records will not be processed.  This application can also process convert the ORCID data dump from XML to JSON.

Works with V2.0, V2.1, v3_0rc1 and v3.0 (default) versions of the schema.

If you wish to use the JAXB beans directly, please use the maven model at https://mvnrepository.com/artifact/org.orcid 

## Usage

- Make sure you have Java 1.8 or greater installed on your machine
- Download the [orcid-conversion-lib-3.0.7-full.jar](https://github.com/ORCID/orcid-conversion-lib/raw/master/target/orcid-conversion-lib-3.0.7-full.jar) executable jar and execute: ```java -jar orcid-conversion-lib-3.0.7-full.jar OPTIONS```

Options:

-  --input-file [-i] (a string; default: "")
    Location of the input file (optional, can pipe in)
-  --format [-f] (xml or json; default: "xml")
    the format of the source file
-  --output-file [-o] (a string; default: "")
    Location of the output file (optional, can pipe out)
-  --schema-version [-v] (v2_0, v2_1, v3_0rc1 or v3_0; default: "v3_0")
    the schema version of the source file
-  --[no]schema-validate [-s] (a boolean; default: "false")
    If true, validate XML input and output against the schema
-  --[no]tarball [-t] (a boolean; default: "false")
    If true, input is a tar.gz containing XML, output is tar.gz containing JSON

## Example usage:

- ```java -jar orcid-conversion-lib-3.0.7-full.jar --help```
- ```java -jar orcid-conversion-lib-3.0.7-full.jar < ../src/test/resources/test-publiclib-record-3.0.xml```
- ```java -jar orcid-conversion-lib-3.0.7-full.jar -i ../src/test/resources/test-publiclib-record-3.0.xml```
- ```java -jar orcid-conversion-lib-3.0.7-full.jar -i ../src/test/resources/test-publiclib-record-3.0.json -f json```
- ```java -jar orcid-conversion-lib-3.0.7-full.jar -i ../src/test/resources/test-publiclib-record-3.0.json -f json -o /tmp/record.xml```
- ```java -jar orcid-conversion-lib-3.0.7-full.jar -i ../src/test/resources/test-publiclib-record-2.0.xml -v v2_0```

## Converting the ORCID data dump
The following will convert the entire contents of a tar.gz file from XML to JSON, preserving paths.  Files are renamed from *.xml to *.json.

- ```java -jar orcid-conversion-lib-3.0.7-full.jar --tarball -i ../src/test/resources/test-archive-2.0.tar.gz -v v2_0 -o /tmp/output.tar.gz```
 
