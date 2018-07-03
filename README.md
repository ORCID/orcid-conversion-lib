# ORCID PUBLIC UTILS

Simple application that takes an XML or JSON file and outputs the other version to the console or a file.  Available as an executable jar.  The jar can be found in the /target directory.

## Usage

Download the executable jar https://github.com/ORCID/orcid-public-lib/raw/master/target/orcid-public-lib-2.1-jar-with-dependencies.jar 
execute: ```java -jar orcid-public-lib.jar OPTIONS```

Options:
-  --input-file [-i] (a string; default: "")
    Location of the input file
-  --format [-f] (xml or json; default: "xml")
    the format of the source file
-  --output-file [-o] (a string; default: "")
    Location of the output file (optional)

## Example usage:

- ```java -jar orcid-public-lib-2.1-jar-with-dependencies.jar -i ../src/test/resources/test-publiclib-record-2.0.xml```
- ```java -jar orcid-public-lib-2.1-SNAPSHOT-jar-with-dependencies.jar -i ../src/test/resources/test-publiclib-record-2.0.json -f json```
- ```java -jar orcid-public-lib-2.1-SNAPSHOT-jar-with-dependencies.jar -i ../src/test/resources/test-publiclib-record-2.0.json -f json -o /tmp/record.xml```
