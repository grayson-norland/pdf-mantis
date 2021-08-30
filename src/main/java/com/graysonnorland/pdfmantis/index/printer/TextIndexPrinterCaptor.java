package com.graysonnorland.pdfmantis.index.printer;

import com.graysonnorland.pdfmantis.index.object.TextIndex;

import java.util.ArrayList;
import java.util.List;

public class TextIndexPrinterCaptor {
    private static final TextIndexPrinterCaptor TEXT_INDEX_PRINTER_CAPTOR = new TextIndexPrinterCaptor();

    public static TextIndexPrinterCaptor getInstance() {
        return TEXT_INDEX_PRINTER_CAPTOR;
    }

    private List<TextIndex> index = new ArrayList<>();

    private TextIndexPrinterCaptor() {
    }

    public List<TextIndex> getIndex() {
        return index;
    }

    public void setIndex(List<TextIndex> index) {
        this.index = index;
    }

    public void clearIndex() {
        index.clear();
    }
}
