package com.graysonnorland.pdfmantis.text;

import com.graysonnorland.pdfmantis.util.PageNumberValidator;
import com.graysonnorland.pdfmantis.index.object.TextIndex;
import com.graysonnorland.pdfmantis.exception.PdfMantisException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.awt.geom.Rectangle2D;
import java.io.IOException;

public class TextExtractor {

    private final PDDocument pdf;

    public TextExtractor(PDDocument pdf) {
        this.pdf = pdf;
    }

    public String getAll() throws IOException {
        return buildTextStripper(-1, -1).getText(pdf);
    }

    public String getAllFromPage(int page) throws IOException, PdfMantisException {
        new PageNumberValidator(pdf).validatePageNumber(page);
        return buildTextStripper(page, page).getText(pdf);
    }

    public String getAllFromPageRange(int startPage, int endPage) throws IOException, PdfMantisException {
        PageNumberValidator pageNumberValidator = new PageNumberValidator(pdf);
        pageNumberValidator.validatePageNumber(startPage);
        pageNumberValidator.validatePageNumber(endPage);
        pageNumberValidator.validatePageRange(startPage, endPage);
        return buildTextStripper(startPage, endPage).getText(pdf);
    }

    public String getFromArea(int page, double x, double y, double h, double w) throws IOException {
        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);
        Rectangle2D textArea = new Rectangle2D.Double(x, y, w, h);
        stripper.addRegion("text_area", textArea);
        PDPage extractPage = pdf.getPage(page - 1);
        stripper.extractRegions(extractPage);
        return stripper.getTextForRegion("text_area").trim();
    }

    public String getFromArea(TextIndex index) throws IOException {
        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);
        Rectangle2D textArea = new Rectangle2D.Double(index.getX(), index.getY(), index.getWidth(), index.getHeight());
        stripper.addRegion("text_area", textArea);
        PDPage extractPage = pdf.getPage(index.getPageNumber() - 1);
        stripper.extractRegions(extractPage);
        return stripper.getTextForRegion("text_area").trim();
    }

    private PDFTextStripper buildTextStripper(int startPage, int endPage) throws IOException {
        PDFTextStripper textStripper = new PDFTextStripper();
        if (startPage != -1) {
            textStripper.setStartPage(startPage);
            textStripper.setEndPage(endPage);
        }
        textStripper.setSortByPosition(true);
        return textStripper;
    }
}
