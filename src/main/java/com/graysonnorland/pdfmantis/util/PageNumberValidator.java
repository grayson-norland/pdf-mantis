package com.graysonnorland.pdfmantis.util;

import com.graysonnorland.pdfmantis.exception.PdfMantisException;
import org.apache.pdfbox.pdmodel.PDDocument;

public class PageNumberValidator {

    private final PDDocument pdf;

    public PageNumberValidator(PDDocument pdf) {
        this.pdf = pdf;
    }

    public void validatePageNumber(int pageNumber) throws PdfMantisException {
        int numberOfPages = pdf.getNumberOfPages();
        if (pageNumber > numberOfPages || pageNumber < 1) {
            throw new PdfMantisException("Page number " + pageNumber + " does not exist. Available pages are 1 to "
                    + numberOfPages);
        }
    }

    public void validatePageRange(int startPage, int endPage) throws PdfMantisException {
        if (startPage > endPage) {
            throw new PdfMantisException("Invalid page range. Start page " + startPage + " is greater than end page "
                    + endPage);
        }
    }
}
