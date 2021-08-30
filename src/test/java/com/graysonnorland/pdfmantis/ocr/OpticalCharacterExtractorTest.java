package com.graysonnorland.pdfmantis.ocr;

import static java.lang.System.getProperty;

import com.graysonnorland.pdfmantis.core.PdfMantis;
import com.graysonnorland.pdfmantis.exception.PdfMantisException;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

public class OpticalCharacterExtractorTest {

    private static final String SINGLE_PAGE_PDF = "single_page.pdf";
    private static final String MULTI_PAGE_PDF = "multi_page.pdf";
    private static final String PAGES_2_TO_3_TEXT = "pages_2_to_3_text";
    private static final String PAGES_2_TO_3_TEXT_150_DPI = "pages_2_to_3_text_150_dpi";
    private static final String PAGE_2_TEXT = "page_2_text";
    private static final String PAGE_2_TEXT_150_API = "page_2_text_150_dpi";
    private static final String ALL_TEXT = "all_text";
    private static final String ALL_TEXT_150_DPI = "all_text_150_dpi";

    private PdfMantis pdf;

    @Before
    public void skipCheck() {
        assumeTrue(isWindows());
    }

    @After
    public void cleanPdf() throws IOException {
        if (pdf != null) {
            pdf.closePdf();
        }
    }

    @Test
    public void getAll() throws IOException, TesseractException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(SINGLE_PAGE_PDF));
        String expectedText = normalizeString(IOUtils.toString(
                this.getClass().getResourceAsStream(ALL_TEXT),
                StandardCharsets.UTF_8));
        assertEquals(expectedText, normalizeString(pdf.getOCRText().getAll()));
    }

    @Test
    public void getAllWithDPI() throws IOException, TesseractException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(SINGLE_PAGE_PDF));
        String expectedText = normalizeString(IOUtils.toString(
                this.getClass().getResourceAsStream(ALL_TEXT_150_DPI),
                StandardCharsets.UTF_8));
        assertEquals(expectedText, normalizeString(pdf.getOCRText().getAll(150)));
    }

    @Test
    public void getAllFromPage() throws IOException, TesseractException, PdfMantisException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(MULTI_PAGE_PDF));
        String expectedText = normalizeString(IOUtils.toString(
                this.getClass().getResourceAsStream(PAGE_2_TEXT),
                StandardCharsets.UTF_8));
        assertEquals(expectedText, normalizeString(pdf.getOCRText().getAllFromPage(2)));
    }

    @Test
    public void getAllFromPageWithDPI() throws IOException, TesseractException, PdfMantisException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(MULTI_PAGE_PDF));
        String expectedText = normalizeString(IOUtils.toString(
                this.getClass().getResourceAsStream(PAGE_2_TEXT_150_API),
                StandardCharsets.UTF_8));
        assertEquals(expectedText, normalizeString(pdf.getOCRText().getAllFromPage(2, 150)));
    }

    @Test
    public void getAllFromPageRange() throws IOException, TesseractException, PdfMantisException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(MULTI_PAGE_PDF));
        String expectedText = normalizeString(IOUtils.toString(
                this.getClass().getResourceAsStream(PAGES_2_TO_3_TEXT),
                StandardCharsets.UTF_8));
        assertEquals(expectedText, normalizeString(pdf.getOCRText().getAllFromPageRange(2, 3)));
    }

    @Test
    public void getAllFromPageRangeWithDPI() throws IOException, TesseractException, PdfMantisException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(MULTI_PAGE_PDF));
        String expectedText = normalizeString(IOUtils.toString(
                this.getClass().getResourceAsStream(PAGES_2_TO_3_TEXT_150_DPI),
                StandardCharsets.UTF_8));
        assertEquals(expectedText, normalizeString(pdf.getOCRText().getAllFromPageRange(2, 3, 150)));
    }

    private String normalizeString(String input) {
        return StringUtils.normalizeSpace(input);
    }

    private boolean isWindows() {
        String forceOCRTests = getProperty("forceOCRTests");
        if (forceOCRTests != null && getProperty("forceOCRTests").contentEquals("true")) {
            return true;
        } else {
            return System.getProperty("os.name").toLowerCase().startsWith("windows");
        }
    }
}
