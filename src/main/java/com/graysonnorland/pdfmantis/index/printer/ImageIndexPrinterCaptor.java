package com.graysonnorland.pdfmantis.index.printer;

import com.graysonnorland.pdfmantis.index.object.ImageIndex;

import java.util.ArrayList;
import java.util.List;

public class ImageIndexPrinterCaptor {
    private static final ImageIndexPrinterCaptor IMAGE_COORDINATES_PRINTER_CAPTOR = new ImageIndexPrinterCaptor();

    public static ImageIndexPrinterCaptor getInstance() {
        return IMAGE_COORDINATES_PRINTER_CAPTOR;
    }

    private final List<ImageIndex> index = new ArrayList<>();

    private ImageIndexPrinterCaptor() {
    }

    public List<ImageIndex> getIndex() {
        return index;
    }

    public void addIndex(ImageIndex index) {
        this.index.add(index);
    }

    public void clearIndex() {
        index.clear();
    }
}
