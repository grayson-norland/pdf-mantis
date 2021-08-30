package com.graysonnorland.pdfmantis.index;

import com.graysonnorland.pdfmantis.index.object.ImageIndex;
import com.graysonnorland.pdfmantis.index.indexer.ImageIndexer;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageIndexExtractor {

    private final PDDocument pdf;

    public ImageIndexExtractor(PDDocument pdf) {
        this.pdf = pdf;
    }

    public List<ImageIndex> buildIndex() throws IOException {
        return new ImageIndexer(pdf).buildImageIndex();
    }

    public List<String> prettifyIndex(List<ImageIndex> imageCoordinatesIndex) {
        List<String> prettifiedIndex = new ArrayList<>();
        for (ImageIndex index : imageCoordinatesIndex) {
            prettifiedIndex.add(
                    "Page Number=" + index.getPageNumber() + "," +
                            "Image Name=" + index.getImageName() + "," +
                            "X=" + index.getX() + "," +
                            "Y=" + index.getY() + "," +
                            "Height=" + index.getHeight() + "," +
                            "Width=" + index.getWidth());
        }
        return prettifiedIndex;
    }
}
