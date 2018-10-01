package org.orcid.conversionlib;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.junit.Test;
import org.orcid.jaxb.model.record_v2.Record;

import com.google.common.io.Resources;

public class OrcidArchiveTranslatorTest {

    @Test
    public void testReadXMLWriteJSON() throws IOException {
        OrcidArchiveTranslator<Record> t = new OrcidArchiveTranslator<Record>(OrcidTranslator.v2_0(false));
        String filename = System.getProperty("java.io.tmpdir")+"test"+new Date().getTime();
        t.translate(Resources.getResource("test-archive-2.0.tar.gz").getPath(),filename);
        
        InputStream fi = Files.newInputStream(Paths.get(filename));
        InputStream bi = new BufferedInputStream(fi);
        InputStream gzi = new GzipCompressorInputStream(bi);
        ArchiveInputStream inStream = new TarArchiveInputStream(gzi);
        
        assertEquals("summaries/",inStream.getNextEntry().getName());
        assertEquals("summaries/file1.json",inStream.getNextEntry().getName());
        assertEquals("summaries/file2.json",inStream.getNextEntry().getName());
        inStream.close();
    }
}
