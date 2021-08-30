package com.graysonnorland.pdfmantis.core;

import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class PdfMantisTest {

    private static final String SAMPLE_PDF = "sample.pdf";
    private static final String ENCRYPTED_PDF = "encrypted.pdf";
    private static final String PASSWORD = "AbC123!";

    private PdfMantis pdf;

    @After
    public void cleanPdf() throws IOException {
        if (pdf != null) {
            pdf.closePdf();
        }
    }

    @Test
    public void loadPdfByStringPath() throws IOException, URISyntaxException {
        URL resource = this.getClass().getResource(SAMPLE_PDF);
        pdf = new PdfMantis(Paths.get(resource.toURI()).toFile().getAbsolutePath());
        assertNotNull(pdf);
    }

    @Test
    public void loadEncryptedPdfByStringPath() throws IOException, URISyntaxException {
        URL resource = this.getClass().getResource(ENCRYPTED_PDF);
        pdf = new PdfMantis(Paths.get(resource.toURI()).toFile().getAbsolutePath(), PASSWORD);
        assertNotNull(pdf);
    }

    @Test
    public void loadPdfByFile() throws IOException, URISyntaxException {
        URL resource = this.getClass().getResource(SAMPLE_PDF);
        pdf = new PdfMantis(Paths.get(resource.toURI()).toFile());
        assertNotNull(pdf);
    }

    @Test
    public void loadEncryptedPdfByFile() throws IOException, URISyntaxException {
        URL resource = this.getClass().getResource(ENCRYPTED_PDF);
        pdf = new PdfMantis(Paths.get(resource.toURI()).toFile(), PASSWORD);
        assertNotNull(pdf);
    }

    @Test
    public void loadPdfByInputStream() throws IOException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(SAMPLE_PDF));
        assertNotNull(pdf);
    }

    @Test
    public void loadEncryptedPdfByInputStream() throws IOException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(ENCRYPTED_PDF), PASSWORD);
        assertNotNull(pdf);
    }

    @Test
    public void loadPdfByUrl() throws IOException {
        pdf = new PdfMantis(this.getClass().getResource(SAMPLE_PDF));
        assertNotNull(pdf);
    }

    @Test
    public void loadEncryptedPdfByUrl() throws IOException {
        pdf = new PdfMantis(this.getClass().getResource(ENCRYPTED_PDF), PASSWORD);
        assertNotNull(pdf);
    }

    @Test
    public void getNumberOfPages() throws IOException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(SAMPLE_PDF));
        assertEquals(1, pdf.getNumberOfPages());
    }

}
