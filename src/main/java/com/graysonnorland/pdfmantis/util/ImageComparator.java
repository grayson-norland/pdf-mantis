package com.graysonnorland.pdfmantis.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ImageComparator {

    public boolean compareImages(BufferedImage actualImage, BufferedImage expectedImage) throws IOException {
        ByteArrayOutputStream baosOriginal = new ByteArrayOutputStream();
        ByteArrayOutputStream baosCurrent = new ByteArrayOutputStream();

        ImageIO.write(actualImage, "png", baosOriginal);
        baosOriginal.flush();

        byte[] imageInByteOriginal = baosOriginal.toByteArray();
        baosOriginal.close();

        ImageIO.write(expectedImage, "png", baosCurrent);
        baosCurrent.flush();

        byte[] imageInByteCurrent = baosCurrent.toByteArray();
        baosCurrent.close();

        return Arrays.equals(imageInByteOriginal, imageInByteCurrent);
    }
}
