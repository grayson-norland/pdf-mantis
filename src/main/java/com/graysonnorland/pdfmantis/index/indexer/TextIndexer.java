package com.graysonnorland.pdfmantis.index.indexer;

import com.graysonnorland.pdfmantis.index.object.TextIndex;
import com.graysonnorland.pdfmantis.index.printer.TextIndexPrinter;
import com.graysonnorland.pdfmantis.index.printer.TextIndexPrinterCaptor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class TextIndexer {

    private final PDDocument pdf;

    public TextIndexer(PDDocument pdf) {
        this.pdf = pdf;
    }

    public List<TextIndex> buildTextIndex() throws IOException {

        PDFTextStripper stripper = new TextIndexPrinter();
        stripper.setSortByPosition(true);
        stripper.setStartPage(0);
        stripper.setEndPage(pdf.getNumberOfPages());

        Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
        stripper.writeText(pdf, dummy);

        TextIndexPrinterCaptor textIndex = TextIndexPrinterCaptor.getInstance();
        List<TextIndex> index = new ArrayList<>(textIndex.getIndex());
        textIndex.clearIndex();

        return index;
    }
}
