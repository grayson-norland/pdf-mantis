package com.graysonnorland.pdfmantis.text;

import com.graysonnorland.pdfmantis.core.PdfMantis;
import com.graysonnorland.pdfmantis.exception.PdfMantisException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class TextExtractorTest {

    private static final String TEXT_PDF = "text.pdf";
    private static final String ALL_TEXT = "all_text";
    private static final String PAGE_2_TEXT = "page_2_text";
    private static final String PAGES_2_TO_3_TEXT = "pages_2_to_3_text";

    private PdfMantis pdf;

    @After
    public void cleanPdf() throws IOException {
        if (pdf != null) {
            pdf.closePdf();
        }
    }

    @Test
    public void getAll() throws IOException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(TEXT_PDF));
        String expectedText = normalizeString(IOUtils.toString(
                this.getClass().getResourceAsStream(ALL_TEXT),
                StandardCharsets.UTF_8));
        assertEquals(expectedText, normalizeString(pdf.getText().getAll()));
    }

    @Test
    public void getAllFromPage() throws IOException, PdfMantisException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(TEXT_PDF));
        String expectedText = normalizeString(IOUtils.toString(
                this.getClass().getResourceAsStream(PAGE_2_TEXT),
                StandardCharsets.UTF_8));
        assertEquals(expectedText, normalizeString(pdf.getText().getAllFromPage(2)));
    }

    @Test
    public void getAllFromPageRange() throws IOException, PdfMantisException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(TEXT_PDF));
        String expectedText = normalizeString(IOUtils.toString(
                this.getClass().getResourceAsStream(PAGES_2_TO_3_TEXT),
                StandardCharsets.UTF_8));
        assertEquals(expectedText, normalizeString(pdf.getText().getAllFromPageRange(2, 3)));
    }

    private String normalizeString(String input) {
        return StringUtils.normalizeSpace(input);
    }
}
