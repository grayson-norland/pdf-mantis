package com.graysonnorland.pdfmantis.index;

import com.graysonnorland.pdfmantis.index.algo.TextIndexBuilder;
import com.graysonnorland.pdfmantis.index.algo.WordDetector;
import com.graysonnorland.pdfmantis.index.object.TextIndex;
import com.graysonnorland.pdfmantis.index.indexer.TextIndexer;
import com.graysonnorland.pdfmantis.exception.PdfMantisException;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextIndexExtractor {

    private final PDDocument pdf;

    public TextIndexExtractor(PDDocument pdf) {
        this.pdf = pdf;
    }

    public List<TextIndex> buildUnicodeIndex() throws IOException {
        return new TextIndexer(pdf).buildTextIndex();
    }

    public List<TextIndex> buildWordIndex() throws IOException {
        return new WordDetector().buildWordIndex(
                new TextIndexer(pdf).buildTextIndex());
    }

    public List<TextIndex> getForString(String string) throws IOException, PdfMantisException {
        return new TextIndexBuilder().findTextIndex(
                new WordDetector().buildWordIndex(
                        new TextIndexer(pdf).buildTextIndex()), string.trim());
    }

    public List<String> prettifyIndex(List<TextIndex> index) {
        List<String> prettifiedIndex = new ArrayList<>();
        for (TextIndex entry : index) {
            prettifiedIndex.add(
                    "Page Number=" + entry.getPageNumber() + "," +
                            "Word=" + entry.getWord() + "," +
                            "Font=" + entry.getFont() + "," +
                            "Font Size=" + entry.getFontSize() + "," +
                            "Colour=" + entry.getColour() + "," +
                            "X=" + entry.getX() + "," +
                            "Y=" + entry.getY() + "," +
                            "Height=" + entry.getHeight() + "," +
                            "Width=" + entry.getWidth());
        }
        return prettifiedIndex;
    }
}
