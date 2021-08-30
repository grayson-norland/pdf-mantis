package com.graysonnorland.pdfmantis.ocr;

import com.graysonnorland.pdfmantis.exception.PdfMantisException;
import com.graysonnorland.pdfmantis.util.PageNumberValidator;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.io.IOException;

public class OpticalCharacterExtractor {

    private final PDDocument pdf;
    private static final int DEFAULT_DPI = 300;

    public OpticalCharacterExtractor(PDDocument pdf) {
        this.pdf = pdf;
    }

    public String getAll() throws IOException, TesseractException {
        return executeGetAll(-1);
    }

    public String getAll(int DPI) throws IOException, TesseractException {
        return executeGetAll(DPI);
    }

    public String getAllFromPage(int page) throws IOException, TesseractException, PdfMantisException {
        new PageNumberValidator(pdf).validatePageNumber(page);
        return doOCR(page - 1, DEFAULT_DPI);
    }

    public String getAllFromPage(int page, int DPI) throws IOException, TesseractException, PdfMantisException {
        new PageNumberValidator(pdf).validatePageNumber(page);
        return doOCR(page - 1, DPI);
    }

    public String getAllFromPageRange(int startPage, int endPage) throws IOException, TesseractException, PdfMantisException {
        return executeGetAllFromPageRange(startPage, endPage, -1);
    }

    public String getAllFromPageRange(int startPage, int endPage, int DPI) throws IOException, TesseractException, PdfMantisException {
        return executeGetAllFromPageRange(startPage, endPage, DPI);
    }

    private String executeGetAllFromPageRange(int startPage, int endPage, int DPI) throws IOException, TesseractException, PdfMantisException {
        PageNumberValidator pageNumberValidator = new PageNumberValidator(pdf);
        pageNumberValidator.validatePageNumber(startPage);
        pageNumberValidator.validatePageNumber(endPage);
        StringBuilder result = new StringBuilder();
        for (int i = startPage - 1; i < endPage; i++) {
            if (DPI == -1) {
                result.append(doOCR(i, DEFAULT_DPI)).append(System.lineSeparator());
            } else {
                result.append(doOCR(i, DPI)).append(System.lineSeparator());
            }
        }
        return result.toString();
    }

    private String executeGetAll(int DPI) throws IOException, TesseractException {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < pdf.getNumberOfPages(); i++) {
            if (DPI == -1) {
                result.append(doOCR(i, DEFAULT_DPI)).append(System.lineSeparator());
            } else {
                result.append(doOCR(i, DPI)).append(System.lineSeparator());
            }
        }
        return result.toString();
    }

    private String doOCR(int page, int DPI) throws IOException, TesseractException {
        Tesseract tesseract = new Tesseract();
        tesseract.setTessVariable("user_defined_dpi", String.valueOf(DPI));
        tesseract.setDatapath(LoadLibs.extractTessResources("tessdata").getAbsolutePath());
        return tesseract.doOCR(new PDFRenderer(pdf).renderImageWithDPI(page, DPI, ImageType.RGB));
    }
}
