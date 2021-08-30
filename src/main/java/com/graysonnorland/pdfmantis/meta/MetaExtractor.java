package com.graysonnorland.pdfmantis.meta;

import com.graysonnorland.pdfmantis.util.TikaParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Map;

public class MetaExtractor {

    private final PDDocument pdf;

    public MetaExtractor(PDDocument pdf) {
        this.pdf = pdf;
    }

    public String getAuthor() {
        return pdf.getDocumentInformation().getAuthor();
    }

    public String getCreator() {
        return pdf.getDocumentInformation().getCreator();
    }

    public String getProducer() {
        return pdf.getDocumentInformation().getProducer();
    }

    public String getSubject() {
        return pdf.getDocumentInformation().getSubject();
    }

    public String getTitle() {
        return pdf.getDocumentInformation().getTitle();
    }

    public String getKeywords() {
        return pdf.getDocumentInformation().getKeywords();
    }

    public String getCreationDate() {
        return formatDate(pdf.getDocumentInformation().getCreationDate());
    }

    public String getModifiedDate() {
        return formatDate(pdf.getDocumentInformation().getModificationDate());
    }

    public Map<String, String> getAll() throws IOException, TikaException, SAXException {
        return new TikaParser(pdf).parsePdf();
    }

    private String formatDate(Calendar calendar) {
        return LocalDateTime.ofInstant(calendar.toInstant(),
                calendar.getTimeZone().toZoneId()).toString();
    }
}
