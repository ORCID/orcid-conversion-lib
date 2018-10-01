package org.orcid.conversionlib;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import javax.xml.bind.JAXBException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.IOUtils;

public class OrcidArchiveTranslator<T> {

    private OrcidTranslator<T> translator;

    public OrcidArchiveTranslator(OrcidTranslator<T> translator) {
        this.translator = translator;
    }

    public void translate(String in, String out) {
        System.out.println(new Date());
        
        //Open files
        ArchiveInputStream inStream = null;
        ArchiveOutputStream outStream = null;
        try {
            InputStream fi = Files.newInputStream(Paths.get(in));
            InputStream bi = new BufferedInputStream(fi);
            InputStream gzi = new GzipCompressorInputStream(bi);
            inStream = new TarArchiveInputStream(gzi);

            OutputStream fo = Files.newOutputStream(Paths.get(out));
            OutputStream gzo = new GzipCompressorOutputStream(fo);
            outStream = new TarArchiveOutputStream(gzo);
        } catch (IOException e) {
            System.err.println("Problem opening files");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            ArchiveEntry entry = null;
            int i = 0;
            int errorCount = 0;
            while ((entry = inStream.getNextEntry()) != null) {
                if (!inStream.canReadEntryData(entry)) {
                    System.err.println("Problem reading " + entry.getName());
                    continue;
                }
                if (entry.isDirectory()) {
                    outStream.putArchiveEntry(entry);
                } else {
                    T r = null;
                    try {
                        r = translator.readXmlRecord(new StringReader(IOUtils.toString(inStream)));
                    } catch (JAXBException e) {
                        errorCount++;
                        //we have <error:error /> 
                        System.err.println("Problem processing file "+errorCount+" "+entry.getName()+" "+e.getMessage());
                        continue;
                    }
                    
                    //write to a byte array
                    ByteArrayOutputStream b = new ByteArrayOutputStream();
                    OutputStreamWriter w = new OutputStreamWriter(b);
                    translator.writeJsonRecord(w, r);
                    byte[] bytes = b.toByteArray();
                    
                    //append to tarfile
                    TarArchiveEntry entry1 = new TarArchiveEntry(entry.getName().replaceAll("xml", "json"));
                    entry1.setSize(b.size());
                    outStream.putArchiveEntry(entry1);
                    outStream.write(bytes);
                    outStream.closeArchiveEntry();
                }
                i++;
                if (i % 10000 == 0) {
                    System.out.println(new Date()+" done "+i);
                    outStream.flush();
                }
            }
            System.out.println(new Date()+" finished "+" errors "+errorCount);
            outStream.close();
        } catch (IOException e) {
            System.err.println("Problem processing files");
            e.printStackTrace();
        } 
    }

}
