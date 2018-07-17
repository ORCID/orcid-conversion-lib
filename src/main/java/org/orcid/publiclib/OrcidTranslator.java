package org.orcid.publiclib;

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

import org.orcid.jaxb.model.record_v2.Record;
import org.orcid.publiclib.CommandLineOptions.InputFormat;
import org.xml.sax.SAXException;

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
public class OrcidTranslator {

    private ObjectMapper mapper;
    Unmarshaller unmarshaller;
    Marshaller marshaller;

    /**
     * Creates a translator suitable for v2.1 or v2.0 API results. Initialises
     * marshaller and unmarshaler
     * 
     */
    public OrcidTranslator(SchemaVersion location) {
        mapper = new ObjectMapper();
        JaxbAnnotationModule module = new JaxbAnnotationModule();
        mapper.registerModule(module);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        try {
            JAXBContext context = JAXBContext.newInstance(Record.class);
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            URL url = Resources.getResource(location.location);
            Schema schema = sf.newSchema(url);

            unmarshaller = context.createUnmarshaller();
            unmarshaller.setSchema(schema);

            marshaller = context.createMarshaller();
            marshaller.setSchema(schema);

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
    public Record readJsonRecord(Reader reader) throws JsonParseException, JsonMappingException, IOException {
        return mapper.readValue(reader, Record.class);
    }

    /**
     * Parses the provided XML into a Record (JAXB Bean)
     * 
     * @param reader
     *            a reader pointing to a source of XML
     * @return a V2 ORCID record bean
     * @throws JAXBException
     */
    public Record readXmlRecord(Reader reader) throws JAXBException {
        return (Record) unmarshaller.unmarshal(reader);
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
    public void writeJsonRecord(Writer w, Record r) throws JsonGenerationException, JsonMappingException, IOException {
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
    public void writeXmlRecord(Writer w, Record r) throws JAXBException {
        marshaller.marshal(r, w);
    }

}
