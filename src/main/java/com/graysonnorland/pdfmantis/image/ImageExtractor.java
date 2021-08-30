package com.graysonnorland.pdfmantis.image;

import com.graysonnorland.pdfmantis.util.PageNumberValidator;
import com.graysonnorland.pdfmantis.index.object.ImageIndex;
import com.graysonnorland.pdfmantis.index.indexer.ImageIndexer;
import com.graysonnorland.pdfmantis.exception.PdfMantisException;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ImageExtractor {

    private final PDDocument pdf;

    public ImageExtractor(PDDocument pdf) {
        this.pdf = pdf;
    }

    private int imageCount = 0;

    public Map<String, BufferedImage> getAllImages() throws IOException {
        Map<String, BufferedImage> images = new LinkedHashMap<>();
        for (PDPage page : pdf.getPages()) {
            images.putAll(getImagesFromResources(page.getResources(),
                    pdf.getPages().indexOf(page) + 1));
        }
        return images;
    }

    public Map<String, BufferedImage> getImagesFromPage(int page) throws IOException, PdfMantisException {
        new PageNumberValidator(pdf).validatePageNumber(page);
        return new LinkedHashMap<>(getImagesFromResources(pdf.getPage(page - 1).getResources(), page));
    }

    public BufferedImage getFromArea(int page, double x, double y, double h, double w) throws IOException, PdfMantisException {
        for (ImageIndex index : new ImageIndexer(pdf).buildImageIndex()) {
            if (index.getPageNumber() == page &&
                    index.getX() == x &&
                    index.getY() == y &&
                    index.getHeight() == h &&
                    index.getWidth() == w) {
                return index.getImage();
            }
        }
        throw new PdfMantisException("Cannot find image within provided area: " + System.lineSeparator() +
                "Page Number=" + page + "|X=" + x + "|" + "Y=" + y + "|Height=" + h + "|Width=" + w);
    }

    private Map<String, BufferedImage> getImagesFromResources(PDResources resources, int pageNumber) throws IOException {
        Map<String, BufferedImage> images = new LinkedHashMap<>();

        for (COSName xObjectName : resources.getXObjectNames()) {
            imageCount++;

            PDXObject xObject = resources.getXObject(xObjectName);
            String uniqueName = "page_" + pageNumber + "_" + xObjectName.getName() + "_" + imageCount;

            if (xObject instanceof PDImageXObject) {
                images.put(uniqueName, ((PDImageXObject) xObject).getImage());
            }
        }
        return images;
    }
}