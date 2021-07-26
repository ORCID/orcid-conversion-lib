package org.orcid.conversionlib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.Optional;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.orcid.conversionlib.CommandLineOptions.InputFormat;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.google.common.io.Resources;

/**
 * Utility class for serialising/deserialising ORCID records. Provides simple
 * access to parsers for Bean Creation
 * 
 * @author tom
 *
 */
public class OrcidTranslator<T> {

    private ObjectMapper mapper;
    Unmarshaller unmarshaller;
    Marshaller marshaller;
    Class<?> modelClass;
    Class<?> errorClass;
    
    /**
     * @return a new v2.0 OrcidTranslator
     */
    public static OrcidTranslator<org.orcid.jaxb.model.record_v2.Record> v2_0(boolean schemaValidate){
        return new OrcidTranslator<org.orcid.jaxb.model.record_v2.Record>(SchemaVersion.V2_0,schemaValidate);
    }
    
    /**
     * @return a new v2.1 OrcidTranslator
     */
    public static OrcidTranslator<org.orcid.jaxb.model.record_v2.Record> v2_1(boolean schemaValidate){
        return new OrcidTranslator<org.orcid.jaxb.model.record_v2.Record>(SchemaVersion.V2_1,schemaValidate);
    }

    /**
     * @return a new v3.0rc1 OrcidTranslator
     */
    public static OrcidTranslator<org.orcid.jaxb.model.v3.rc1.record.Record> v3_0RC1(boolean schemaValidate){
        return new OrcidTranslator<org.orcid.jaxb.model.v3.rc1.record.Record>(SchemaVersion.V3_0RC1,schemaValidate);
    }

    /**
     * @return a new v3.0 OrcidTranslator
     */
    public static OrcidTranslator<org.orcid.jaxb.model.v3.release.record.Record> v3_0(boolean schemaValidate){
        return new OrcidTranslator<org.orcid.jaxb.model.v3.release.record.Record>(SchemaVersion.V3_0,schemaValidate);
    }

    
    /**
     * Creates a translator suitable for v2.1 or v2.0 API results. Initialises
     * marshaller and unmarshaler
     * 
     */
    private OrcidTranslator(SchemaVersion location,boolean schemaValidate) {
        mapper = new ObjectMapper();
        JaxbAnnotationModule module = new JaxbAnnotationModule();
        mapper.registerModule(module);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        modelClass = location.modelClass;
        errorClass = location.errorClass;
        try {
            JAXBContext context = JAXBContext.newInstance(modelClass,errorClass);
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            URL url = Resources.getResource(location.location);
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
    }

    /**
     * Translate one version to another using files on the file system
     * 
     * @param inputFilename
     *            The input file to read.  If missing will read from System.in
     * @param outputFilename
     *            The output file to write to. If missing, will output to
     *            System.out
     * @param inputFormat
     *            The format of the input file.
     * @throws FileNotFoundException
     * @throws IOException
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws JAXBException
     * @throws JsonParseException
     */
    public void translate(Optional<String> inputFilename, Optional<String> outputFilename, InputFormat inputFormat)
            throws FileNotFoundException, IOException, JsonGenerationException, JsonMappingException, JAXBException, JsonParseException {
        
        // Read the file
        Reader r;
        if (inputFilename.isPresent() && !inputFilename.get().isEmpty()) {
            File file = new File(inputFilename.get());
            r = new FileReader(file);
        } else {
            r = new InputStreamReader(System.in);
        }

        // Work out where to write the file
        Writer w;
        if (outputFilename.isPresent() && !outputFilename.get().isEmpty()) {
            File output = new File(outputFilename.get());
            w = new FileWriter(output);
        } else {
            w = new PrintWriter(System.out);
        }

        //Do the translation
        if (inputFormat.equals(InputFormat.XML)) {
            writeJsonRecord(w, readXmlRecord(r));
        } else {
            writeXmlRecord(w, readJsonRecord(r));
        }
    }

    /**
     * Parses the provided JSON into a Record (JAXB Bean)
     * 
     * @param reader
     *            a reader pointing to a source of JSON
     * @return a V2 ORCID record bean
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public T readJsonRecord(Reader reader) throws JsonParseException, JsonMappingException, IOException {
        return (T) mapper.readValue(reader, modelClass);
    }

    /**
     * Parses the provided XML into a Record (JAXB Bean)
     * 
     * @param reader
     *            a reader pointing to a source of XML
     * @return a V2 ORCID record bean
     * @throws JAXBException
     */
    @SuppressWarnings("unchecked")
    public T readXmlRecord(Reader reader) throws JAXBException {
        return (T) unmarshaller.unmarshal(reader);
    }

    /**
     * Writes the provided Record (JAXB Bean) as JSON using provided writer
     * 
     * @param w
     *            where to write the JSON
     * @param r
     *            the record to read
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public void writeJsonRecord(Writer w, T r) throws JsonGenerationException, JsonMappingException, IOException {
        mapper.writeValue(w, r);
    }

    /**
     * Writes the provided Record (JAXB Bean) as XML using provided writer
     * 
     * @param w
     *            where to write the XML
     * @param r
     *            the record to read
     * @throws JAXBException
     */
    public void writeXmlRecord(Writer w, T r) throws JAXBException {
        marshaller.marshal(r, w);
    }

}
