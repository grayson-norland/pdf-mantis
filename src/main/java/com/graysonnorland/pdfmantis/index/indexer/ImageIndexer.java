package com.graysonnorland.pdfmantis.index.indexer;

import com.graysonnorland.pdfmantis.index.object.ImageIndex;
import com.graysonnorland.pdfmantis.index.printer.ImageIndexPrinterCaptor;
import com.graysonnorland.pdfmantis.index.printer.ImageIndexPrinter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageIndexer {

    private final PDDocument pdf;

    public ImageIndexer(PDDocument pdf) {
        this.pdf = pdf;
    }

    public List<ImageIndex> buildImageIndex() throws IOException {

        new ImageIndexPrinter().getImageIndex(pdf);

        ImageIndexPrinterCaptor imageIndex = ImageIndexPrinterCaptor.getInstance();
        List<ImageIndex> index = new ArrayList<>(imageIndex.getIndex());
        imageIndex.clearIndex();

        return index;
    }
}
