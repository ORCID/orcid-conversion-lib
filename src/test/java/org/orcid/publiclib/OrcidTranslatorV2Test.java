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

public class OrcidTranslatorV2Test {

    @Test
    public void testReadJsonWriteXml() throws JsonParseException, JsonMappingException, IOException, JAXBException {
        OrcidTranslatorV2 t = new OrcidTranslatorV2();
        URL url = Resources.getResource("test-publiclib-record-2.0.json");
        InputStream is = url.openStream();
        Record r = t.readJsonRecord(new InputStreamReader(is));
        assertEquals("0000-0003-0902-4386", r.getOrcidIdentifier().getPath());
        StringWriter sw = new StringWriter();
        t.writeXmlRecord(sw, r);
        assertTrue(sw.toString().contains("<common:path>0000-0003-0902-4386</common:path>"));
    }

    @Test
    public void testReadXmlWriteJson() throws JAXBException, IOException {
        OrcidTranslatorV2 t = new OrcidTranslatorV2();
        URL url = Resources.getResource("test-publiclib-record-2.0.xml");
        InputStream is = url.openStream();
        Record r = t.readXmlRecord(new InputStreamReader(is));
        assertEquals("0000-0003-0902-4386", r.getOrcidIdentifier().getPath());
        StringWriter sw = new StringWriter();
        t.writeJsonRecord(sw, r);
        assertTrue(sw.toString().contains("\"path\" : \"0000-0003-0902-4386\","));
    }

}
