package com.graysonnorland.pdfmantis.image;

import com.graysonnorland.pdfmantis.core.PdfMantis;
import com.graysonnorland.pdfmantis.exception.PdfMantisException;
import com.graysonnorland.pdfmantis.util.ImageComparator;
import org.junit.After;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ImageExtractorTest {

    private static final String IMAGES_PDF = "images.pdf";
    private static final List<String> IMAGES_ALL = Arrays.asList(
            "page_1_X0_1", "page_1_X1_2", "page_1_X2_3",
            "page_2_X0_4", "page_2_X1_5");
    private static final List<String> IMAGES_PAGE_ONE = Arrays.asList(
            "page_1_X0_1", "page_1_X1_2", "page_1_X2_3");

    private PdfMantis pdf;

    @After
    public void cleanPdf() throws IOException {
        if (pdf != null) {
            pdf.closePdf();
        }
    }

    @Test
    public void getAllImages() throws IOException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(IMAGES_PDF));
        Map<String, BufferedImage> images = pdf.getImage().getAllImages();
        saveImagesToPath(images, "target/test-images/all/");

        List<String> expectedNames = new ArrayList<>();
        images.forEach((name, image) -> expectedNames.add(name));

        assertEquals(IMAGES_ALL.size(), images.size());
        assertEquals(IMAGES_ALL, expectedNames);

        List<String> discrepancies = new ArrayList<>();
        ImageComparator imageComparator = new ImageComparator();

        for (Map.Entry<String, BufferedImage> image : images.entrySet()) {
            BufferedImage actualImage = ImageIO.read(
                    this.getClass().getResourceAsStream(image.getKey() + ".png"));
            boolean imageMatches = imageComparator.compareImages(actualImage, image.getValue());
            if (!imageMatches) {
                discrepancies.add(image.getKey());
            }
        }
        assertTrue(discrepancies.isEmpty());
    }

    @Test
    public void getImagesFromPage() throws IOException, PdfMantisException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(IMAGES_PDF));
        Map<String, BufferedImage> images = pdf.getImage().getImagesFromPage(1);
        saveImagesToPath(images, "target/test-images/page/");

        List<String> expectedNames = new ArrayList<>();
        images.forEach((name, image) -> expectedNames.add(name));

        assertEquals(IMAGES_PAGE_ONE.size(), images.size());
        assertEquals(IMAGES_PAGE_ONE, expectedNames);

        List<String> discrepancies = new ArrayList<>();
        ImageComparator imageComparator = new ImageComparator();

        for (Map.Entry<String, BufferedImage> image : images.entrySet()) {
            BufferedImage actualImage = ImageIO.read(
                    this.getClass().getResourceAsStream(image.getKey() + ".png"));
            boolean imageMatches = imageComparator.compareImages(actualImage, image.getValue());
            if (!imageMatches) {
                discrepancies.add(image.getKey());
            }
        }
        assertTrue(discrepancies.isEmpty());
    }

    private void saveImagesToPath(Map<String, BufferedImage> images, String path) throws IOException {
        for (Map.Entry<String, BufferedImage> image : images.entrySet()) {
            File outputFile = new File(path + image.getKey() + ".png");
            IGNORE_RESULT(outputFile.getParentFile().mkdirs());
            ImageIO.write(image.getValue(), "png", outputFile);
        }
    }

    @SuppressWarnings("unused")
    private static void IGNORE_RESULT(boolean b) {
    }
}