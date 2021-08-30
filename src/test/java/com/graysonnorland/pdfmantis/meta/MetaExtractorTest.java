package com.graysonnorland.pdfmantis.meta;

import com.graysonnorland.pdfmantis.util.StreamReader;
import com.graysonnorland.pdfmantis.core.PdfMantis;
import org.apache.tika.exception.TikaException;
import org.junit.After;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MetaExtractorTest {

    private static final String META_PDF = "meta.pdf";
    private static final String EXPECTED_META_RESULTS = "expected_meta_results";
    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    private PdfMantis pdf;

    @After
    public void cleanPdf() throws IOException {
        if (pdf != null) {
            pdf.closePdf();
        }
    }

    @Test
    public void getAuthor() throws IOException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(META_PDF));
        assertEquals("Grayson Norland", pdf.getMeta().getAuthor());
    }

    @Test
    public void getCreator() throws IOException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(META_PDF));
        assertEquals("PeekPDF", pdf.getMeta().getCreator());
    }

    @Test
    public void getProducer() throws IOException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(META_PDF));
        assertEquals("https://www.graysonnorland.co.uk",
                pdf.getMeta().getProducer());
    }

    @Test
    public void getSubject() throws IOException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(META_PDF));
        assertEquals("Metadata Example", pdf.getMeta().getSubject());
    }

    @Test
    public void getTitle() throws IOException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(META_PDF));
        assertEquals("Metadata PDF", pdf.getMeta().getTitle());
    }

    @Test
    public void getKeywords() throws IOException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(META_PDF));
        assertEquals("Java, PDF, Metadata", pdf.getMeta().getKeywords());
    }

    @Test
    public void getCreationDate() throws IOException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(META_PDF));
        assertTrue(verifyDateFormat(pdf.getMeta().getCreationDate()));
    }

    @Test
    public void getModifiedDate() throws IOException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(META_PDF));
        assertTrue(verifyDateFormat(pdf.getMeta().getModifiedDate()));
    }

    @Test
    public void getAll() throws IOException, TikaException, SAXException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(META_PDF));
        Map<String, String> actualMetadata = pdf.getMeta().getAll();
        Map<String, String> expectedMetadata =
                new StreamReader().getStringMapFromStream(this.getClass().
                        getResourceAsStream(EXPECTED_META_RESULTS));
        assertEquals(expectedMetadata.keySet(), actualMetadata.keySet());

        actualMetadata.entrySet().removeIf(entry -> entry.getValue().contains("2020"));
        expectedMetadata.entrySet().removeIf(entry -> entry.getValue().contains("2020"));
        assertEquals(expectedMetadata, actualMetadata);
    }

    private boolean verifyDateFormat(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        boolean isValidDateFormat;

        try {
            dateFormat.parse(date);
            dateFormat.setLenient(false);
            isValidDateFormat = true;

        } catch (ParseException e) {
            isValidDateFormat = false;
        }
        return isValidDateFormat;
    }
}