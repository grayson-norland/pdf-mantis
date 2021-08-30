package com.graysonnorland.pdfmantis.core;

import com.graysonnorland.pdfmantis.meta.MetaExtractor;
import com.graysonnorland.pdfmantis.index.ImageIndexExtractor;
import com.graysonnorland.pdfmantis.image.ImageExtractor;
import com.graysonnorland.pdfmantis.ocr.OpticalCharacterExtractor;
import com.graysonnorland.pdfmantis.table.TableExtractor;
import com.graysonnorland.pdfmantis.text.TextExtractor;
import com.graysonnorland.pdfmantis.index.TextIndexExtractor;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class PdfMantis {

    static {
        java.util.logging.Logger.getLogger("org.apache.pdfbox").setLevel(java.util.logging.Level.OFF);
    }

    private final PDDocument pdf;

    public void closePdf() throws IOException {
        pdf.close();
    }

    public PdfMantis(String path) throws IOException {
        pdf = PDDocument.load(new File(path), MemoryUsageSetting.setupTempFileOnly());
    }

    public PdfMantis(String path, String password) throws IOException {
        pdf = PDDocument.load(new File(path), password, MemoryUsageSetting.setupTempFileOnly());
    }

    public PdfMantis(File file) throws IOException {
        pdf = PDDocument.load(file, MemoryUsageSetting.setupTempFileOnly());
    }

    public PdfMantis(File file, String password) throws IOException {
        pdf = PDDocument.load(file, password, MemoryUsageSetting.setupTempFileOnly());
    }

    public PdfMantis(InputStream inputStream) throws IOException {
        pdf = PDDocument.load(inputStream, MemoryUsageSetting.setupTempFileOnly());
    }

    public PdfMantis(InputStream inputStream, String password) throws IOException {
        pdf = PDDocument.load(inputStream, password, MemoryUsageSetting.setupTempFileOnly());
    }

    public PdfMantis(URL url) throws IOException {
        pdf = PDDocument.load(url.openStream(), MemoryUsageSetting.setupTempFileOnly());
    }

    public PdfMantis(URL url, String password) throws IOException {
        pdf = PDDocument.load(url.openStream(), password, MemoryUsageSetting.setupTempFileOnly());
    }

    public MetaExtractor getMeta() {
        return new MetaExtractor(pdf);
    }

    public TextExtractor getText() {
        return new TextExtractor(pdf);
    }

    public TextIndexExtractor getTextIndex() {
        return new TextIndexExtractor(pdf);
    }

    public TableExtractor getTable() {
        return new TableExtractor(pdf);
    }

    public ImageExtractor getImage() {
        return new ImageExtractor(pdf);
    }

    public ImageIndexExtractor getImageIndex() {
        return new ImageIndexExtractor(pdf);
    }

    public OpticalCharacterExtractor getOCRText() {
        return new OpticalCharacterExtractor(pdf);
    }

    public int getNumberOfPages() {
        return pdf.getNumberOfPages();
    }
}
