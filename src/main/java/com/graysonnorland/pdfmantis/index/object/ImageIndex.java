package com.graysonnorland.pdfmantis.index.object;

import java.awt.image.BufferedImage;

public class ImageIndex {

    private final int pageNumber;
    private final String imageName;
    private final double x;
    private final double y;
    private final double h;
    private final double w;
    private final BufferedImage image;

    public ImageIndex(int pageNumber, String imageName, double x, double y, double h, double w,
                      BufferedImage image) {
        this.pageNumber = pageNumber;
        this.imageName = imageName;
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
        this.image = image;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public String getImageName() {
        return imageName;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getHeight() {
        return h;
    }

    public double getWidth() {
        return w;
    }

    public BufferedImage getImage() { return image; }
}
