# ORCID MODEL

The [orcid-model-2.1.jar](https://github.com/ORCID/orcid-conversion-lib/raw/master/orcid-model/orcid-model-2.1.jar) contains the Java JAXB model that is used by the ORCID registry.  It enables translation between XML, JSON and Java Beans.  It can be used to quickly put together a Java ORCID client.

The top level class for v2.0 and v2.1 is ```org.orcid.jaxb.model.record_v2.Record```.  Other models (e.g. Works) can also be created/unmarshalled/marshalled as required, see the other ```org.orcid.jaxb.model.*```.

| Version | Schema location | Record class |
| --- | --- | --- |
| v2.0 | record_2.0/record-2.0.xsd | org.orcid.jaxb.model.record_v2.Record |
| v2.1 | record_2.1/record-2.1.xsd | org.orcid.jaxb.model.record_v2.Record |
| v3.0rc1 | record_3.0_rc1/record-3.0_rc1.xsd | org.orcid.jaxb.model.record_v2.Record |

 For more examples, please see the [OrcidTranslator](https://github.com/ORCID/orcid-conversion-lib/blob/master/src/main/java/org/orcid/conversionlib/OrcidTranslator.java) demonstrates how to serialize and deserialize XML and Java using JAXB.

## XML v2.1 Example

	//create a JAXB marshaller/unmarshaller. 
	try {
        JAXBContext context = JAXBContext.newInstance(Record.class);
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL url = Resources.getResource("record_2.1/record-2.1.xsd");
        unmarshaller = context.createUnmarshaller();
        marshaller = context.createMarshaller();
        if (schemaValidate){
            Schema schema = sf.newSchema(url);
            unmarshaller.setSchema(schema);
            marshaller.setSchema(schema);                
        }
    } catch (JAXBException | SAXException e) {
        throw new RuntimeException("Unable to create jaxb marshaller/unmarshaller" + e);
    }
    
    //read a record from XML 
	//Note this works for element types returned by the API, e.g. Works
    Reader reader = //some kind of string/file/inputstream reader;
    Record record = (Record) unmarshaller.unmarshal(reader);
    
    //write a record as XML
    Writer writer = //some kind of string/file/outputstream writer;
    marshaller.marshal(record, writer);

## JSON Example

	//create a jackson object mapper
	ObjectMapper mapper = new ObjectMapper();
    JaxbAnnotationModule module = new JaxbAnnotationModule();
    mapper.registerModule(module);
    mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

	//Read a JSON Record 
	//Note this works for element types returned by the API, e.g. Works
	Reader reader = //some kind of string/file/inputstream reader;
	Record record = (Record) mapper.readValue(reader, Record.class);
	
	//write a JSON Record
    Writer writer = //some kind of string/file/outputstream writer;
	mapper.writeValue(writer, record);
	
## Maven

You will have to add the jar to your local repository to use it (it's not on maven central).  The details are:

	<groupId>org.orcid</groupId>
    <artifactId>orcid-model</artifactId>
    <version>1.1.5-SNAPSHOT</version>

## Dependencies

orcid-model depends on the following, which should automatically be imported by maven if you add the orcid-model jar to your local repository:

    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-lang3</artifactId>
        <version>3.2</version>
    </dependency>
	<dependency>
        <groupId>commons-lang</groupId>
        <artifactId>commons-lang</artifactId>
        <version>2.6</version>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.jaxrs</groupId>
        <artifactId>jackson-jaxrs-json-provider</artifactId>
        <version>2.9.3</version>
    </dependency>
    <dependency>
        <groupId>io.swagger</groupId>
        <artifactId>swagger-jersey-jaxrs</artifactId>
        <version>1.5.16</version>
    </dependency>
    <dependency>
        <groupId>commons-io</groupId>
        <artifactId>commons-io</artifactId>
        <version>2.1</version>
    </dependency>
    
More recent versions of Java (9+) require you to explicitly include JAXB.  Like this:

    <dependency>
        <groupId>javax.xml.bind</groupId>
        <artifactId>jaxb-api</artifactId>
    <version>2.3.0</version>
	</dependency>
    <dependency>
        <groupId>com.sun.xml.bind</groupId>
        <artifactId>jaxb-impl</artifactId>
        <version>2.3.0</version>
	</dependency>
    <dependency>
	    <groupId>com.sun.xml.bind</groupId>
	    <artifactId>jaxb-core</artifactId>
	    <version>2.3.0</version>
	</dependency>
	<dependency>
	    <groupId>javax.activation</groupId>
	    <artifactId>activation</artifactId>
	    <version>1.1.1</version>
	</dependency>

## Notes on schema validation

While we strive to ensure records returned by the API are schema valid, this is not always the case.  This is mainly due to new rules and requirements being introduced over time that earlier versions of the schema did not enforce.  We are actively addressing these issues as they appear.  We advise you do not use schema validation unless absolutely necessary. 

## Source

The source for this jar is at https://github.com/ORCID/ORCID-Source/tree/master/orcid-model .  Note the current source may not exactly match this release version.
