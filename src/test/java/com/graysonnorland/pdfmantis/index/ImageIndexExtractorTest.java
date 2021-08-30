package com.graysonnorland.pdfmantis.index;

import com.graysonnorland.pdfmantis.core.PdfMantis;
import com.graysonnorland.pdfmantis.util.StreamReader;

import com.graysonnorland.pdfmantis.exception.PdfMantisException;
import com.graysonnorland.pdfmantis.util.ImageComparator;
import org.junit.After;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ImageIndexExtractorTest {

    private static final String IMAGE_INDEX_PDF = "image_index.pdf";
    private static final String EXPECTED_INDEX = "expected_image_index";
    private static final String EXPECTED_IMAGE = "expected_image.png";

    private PdfMantis pdf;

    @After
    public void cleanPdf() throws IOException {
        if (pdf != null) {
            pdf.closePdf();
        }
    }

    @Test
    public void getImageIndex() throws IOException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(IMAGE_INDEX_PDF));
        List<String> actualIndex = pdf.getImageIndex().prettifyIndex(
                pdf.getImageIndex().buildIndex());
        List<String> expectedIndex = new StreamReader().getStringListFromStream(
                this.getClass().getResourceAsStream(EXPECTED_INDEX));
        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void getFromArea() throws IOException, PdfMantisException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(IMAGE_INDEX_PDF));
        BufferedImage actualImage = pdf.getImage().getFromArea(
                1, 48.699981689453125, 688.5, 120.0, 650.0);
        BufferedImage expectedImage = ImageIO.read(this.getClass().getResourceAsStream(EXPECTED_IMAGE));
        assertTrue(new ImageComparator().compareImages(expectedImage, actualImage));
    }
}
