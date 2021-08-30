package com.graysonnorland.pdfmantis.exception;

import com.graysonnorland.pdfmantis.core.PdfMantis;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ExceptionTest {

    private static final String EXCEPTION_PDF = "exception.pdf";
    private static final String INVALID_PAGE_RANGE_PDF = "invalid_page_range.pdf";

    private PdfMantis pdf;

    @After
    public void cleanPdf() throws IOException {
        if (pdf != null) {
            pdf.closePdf();
        }
    }

    @Test
    public void extractTableException() throws IOException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(EXCEPTION_PDF));
        PdfMantisException exception = assertThrows(
                PdfMantisException.class,
                () -> pdf.getTable().extractFromPage(1)
        );
        assertEquals("Could not detect any tables for extraction", exception.getMessage());
    }

    @Test
    public void getImageFromAreaException() throws IOException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(EXCEPTION_PDF));
        PdfMantisException exception = assertThrows(
                PdfMantisException.class,
                () -> pdf.getImage().getFromArea(1, 1.1, 2.2, 3.3, 4.4)
        );
        assertEquals("Cannot find image within provided area: " + System.lineSeparator()
                + "Page Number=1|X=1.1|Y=2.2|Height=3.3|Width=4.4", exception.getMessage());
    }

    @Test
    public void getForStringException() throws IOException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(EXCEPTION_PDF));
        PdfMantisException exception = assertThrows(
                PdfMantisException.class,
                () -> pdf.getTextIndex().getForString("Grayson Norland")
        );
        assertEquals("Cannot find any occurrence of String 'Grayson Norland'", exception.getMessage());
    }

    @Test
    public void invalidPageNumberException() throws IOException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(EXCEPTION_PDF));
        PdfMantisException exception = assertThrows(
                PdfMantisException.class,
                () -> pdf.getText().getAllFromPage(5)
        );
        assertEquals("Page number 5 does not exist. Available pages are 1 to 1", exception.getMessage());
    }

    @Test
    public void invalidPageRangeException() throws IOException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(INVALID_PAGE_RANGE_PDF));
        PdfMantisException exception = assertThrows(
                PdfMantisException.class,
                () -> pdf.getText().getAllFromPageRange(2, 1)
        );
        assertEquals("Invalid page range. Start page 2 is greater than end page 1", exception.getMessage());
    }
}
