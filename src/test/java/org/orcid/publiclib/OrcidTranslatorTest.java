package org.orcid.publiclib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.orcid.jaxb.model.record_v2.Record;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.io.Resources;

public class OrcidTranslatorTest {

    @Test
    public void testReadJsonWriteXmlV2_0() throws JsonParseException, JsonMappingException, IOException, JAXBException {
        OrcidTranslator<Record> t = OrcidTranslator.v2_0();
        URL url = Resources.getResource("test-publiclib-record-2.0.json");
        InputStream is = url.openStream();
        Record r = t.readJsonRecord(new InputStreamReader(is));
        assertEquals("0000-0003-0902-4386", r.getOrcidIdentifier().getPath());
        StringWriter sw = new StringWriter();
        t.writeXmlRecord(sw, r);
        assertTrue(sw.toString().contains("<common:path>0000-0003-0902-4386</common:path>"));
    }
    
    @Test
    public void testReadJsonWriteXmlV2_1() throws JsonParseException, JsonMappingException, IOException, JAXBException {
        OrcidTranslator<Record> t = OrcidTranslator.v2_1();
        URL url = Resources.getResource("test-publiclib-record-2.1.json");
        InputStream is = url.openStream();
        Record r = t.readJsonRecord(new InputStreamReader(is));
        assertEquals("0000-0003-0902-4386", r.getOrcidIdentifier().getPath());
        StringWriter sw = new StringWriter();
        t.writeXmlRecord(sw, r);
        assertTrue(sw.toString().contains("<common:path>0000-0003-0902-4386</common:path>"));
    }
    
    @Test
    public void testReadJsonWriteXmlV3_0RC1() throws JsonParseException, JsonMappingException, IOException, JAXBException {
        OrcidTranslator<org.orcid.jaxb.model.v3.rc1.record.Record> t = OrcidTranslator.v3_0RC1();
        URL url = Resources.getResource("test-publiclib-record-3.0_rc1.json");
        InputStream is = url.openStream();
        org.orcid.jaxb.model.v3.rc1.record.Record r = t.readJsonRecord(new InputStreamReader(is));
        assertEquals("0000-0003-0902-4386", r.getOrcidIdentifier().getPath());
        StringWriter sw = new StringWriter();
        t.writeXmlRecord(sw, r);
        assertTrue(sw.toString().contains("<common:path>0000-0003-0902-4386</common:path>"));
    }

    @Test
    public void testReadXmlWriteJsonV2_0() throws JAXBException, IOException {
        OrcidTranslator<Record> t = OrcidTranslator.v2_0();
        URL url = Resources.getResource("test-publiclib-record-2.0.xml");
        InputStream is = url.openStream();
        Record r = t.readXmlRecord(new InputStreamReader(is));
        assertEquals("0000-0003-0902-4386", r.getOrcidIdentifier().getPath());
        StringWriter sw = new StringWriter();
        t.writeJsonRecord(sw, r);
        assertTrue(sw.toString().contains("\"path\" : \"0000-0003-0902-4386\","));
    }
    
    @Test
    public void testReadXmlWriteJsonV2_1() throws JAXBException, IOException {
        OrcidTranslator<Record> t = OrcidTranslator.v2_1();
        URL url = Resources.getResource("test-publiclib-record-2.1.xml");
        InputStream is = url.openStream();
        Record r = t.readXmlRecord(new InputStreamReader(is));
        assertEquals("0000-0003-0902-4386", r.getOrcidIdentifier().getPath());
        StringWriter sw = new StringWriter();
        t.writeJsonRecord(sw, r);
        assertTrue(sw.toString().contains("\"path\" : \"0000-0003-0902-4386\","));
    }
    
    @Test
    public void testReadXmlWriteJsonV3_0RC1() throws JAXBException, IOException {
        OrcidTranslator<org.orcid.jaxb.model.v3.rc1.record.Record> t = OrcidTranslator.v3_0RC1();
        URL url = Resources.getResource("test-publiclib-record-3.0_rc1.xml");
        InputStream is = url.openStream();
        org.orcid.jaxb.model.v3.rc1.record.Record r = t.readXmlRecord(new InputStreamReader(is));
        assertEquals("0000-0003-0902-4386", r.getOrcidIdentifier().getPath());
        StringWriter sw = new StringWriter();
        t.writeJsonRecord(sw, r);
        assertTrue(sw.toString().contains("\"path\" : \"0000-0003-0902-4386\","));
    }

}
