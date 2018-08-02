# ORCID CONVERSION UTIL

Simple application that takes an ORCID XML or JSON file and outputs the other version to the console or a file.  Available as a stand-alone executable jar, which can can be found in the /target directory. The application is designed to work with full ORCID record metadata.  Partial records will not be processed.

Works with V2.0 and V2.1 (default) versions of the schema.

If you wish to use the JAXB beans directly, please use [orcid-model.jar](https://github.com/ORCID/orcid-conversion-lib/raw/master/orcid-model/) as it contains just the model and none of the dependencies.  

## Usage

- Make sure you have Java 1.8 or greater installed on your machine
- Download the [orcid-conversion-lib-0.0.1-full.jar](https://github.com/ORCID/orcid-conversion-lib/raw/master/target/orcid-conversion-lib-0.0.1-full.jar) executable jar and execute: ```java -jar orcid-conversion-lib-0.0.1-full.jar OPTIONS```

Options:

-  --input-file [-i] (a string; default: "")
    Location of the input file (optional, can pipe in)
-  --format [-f] (xml or json; default: "xml")
    the format of the source file
-  --output-file [-o] (a string; default: "")
    Location of the output file (optional, can pipe out)
-  --schema-version [-v] (v2_0, v2_1 or v3_0rc1; default: "v2_1")
    the schema version of the source file
-  --[no]schema-validate [-s] (a boolean; default: "false")
    If true, validate XML input and output against the schema

## Example usage:

- ```java -jar orcid-conversion-lib-0.0.1-full.jar --help```
- ```java -jar orcid-conversion-lib-0.0.1-full.jar < ../src/test/resources/test-publiclib-record-2.1.xml```
- ```java -jar orcid-conversion-lib-0.0.1-full.jar -i ../src/test/resources/test-publiclib-record-2.1.xml```
- ```java -jar orcid-conversion-lib-0.0.1-full.jar -i ../src/test/resources/test-publiclib-record-2.1.json -f json```
- ```java -jar orcid-conversion-lib-0.0.1-full.jar -i ../src/test/resources/test-publiclib-record-2.1.json -f json -o /tmp/record.xml```
- ```java -jar orcid-conversion-lib-0.0.1-full.jar -i ../src/test/resources/test-publiclib-record-2.0.xml -v v2_0```

## Model classes:

The executable jar also includes a simple translation class to parse XML and JSON into JAXB annotated beans and contains the full ORCID 2.0/2.1 model as JAXB annotated beans.
However, as the jar contains all transitive dependencies, it is not advisable to use it as a dependency in your own projects.

If you wish to use the JAXB beans directly, [please use the orcid-model jar](https://github.com/ORCID/orcid-conversion-lib/raw/master/orcid-model/).  This contains just the model and none of the dependencies.  
 
