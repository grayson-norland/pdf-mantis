package com.graysonnorland.pdfmantis.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TikaParser {

    private final PDDocument pdf;
    private static final String TIKA_CONFIG = "tika-config.xml";

    public TikaParser(PDDocument pdf) {
        this.pdf = pdf;
    }

    public Map<String, String> parsePdf() throws IOException, TikaException, SAXException {

        URL url = this.getClass().getResource(TIKA_CONFIG);
        Parser parser = new AutoDetectParser(new TikaConfig(url));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        pdf.save(baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        Metadata metadata = new Metadata();

        parser.parse(
                bais,
                new BodyContentHandler(),
                metadata,
                new ParseContext());

        bais.close();
        baos.close();

        Map<String, String> metadataMap = new LinkedHashMap<>();

        List<String> metadataNames = Arrays.asList(metadata.names());
        metadataNames.forEach(name -> metadataMap.put(name, metadata.get(name)));

        return metadataMap;
    }
}
