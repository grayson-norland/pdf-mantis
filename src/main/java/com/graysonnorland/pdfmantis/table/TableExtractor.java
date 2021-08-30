package com.graysonnorland.pdfmantis.table;

import java.util.ArrayList;
import java.util.List;

import com.graysonnorland.pdfmantis.util.PageNumberValidator;
import com.graysonnorland.pdfmantis.exception.PdfMantisException;
import com.jakewharton.fliptables.FlipTable;
import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.PageIterator;
import technology.tabula.Rectangle;
import technology.tabula.Table;
import technology.tabula.detectors.NurminenDetectionAlgorithm;
import technology.tabula.extractors.BasicExtractionAlgorithm;
import technology.tabula.extractors.ExtractionAlgorithm;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

public class TableExtractor {

    private final PDDocument pdf;

    public TableExtractor(PDDocument pdf) {
        this.pdf = pdf;
    }

    public List<Table> extractAll() throws PdfMantisException {
        return extract(-1);
    }

    public List<Table> extractFromPage(int pageNumber) throws PdfMantisException {
        new PageNumberValidator(pdf).validatePageNumber(pageNumber);
        return extract(pageNumber);
    }

    private List<Table> extract(int pageNumber) throws PdfMantisException {
        SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
        NurminenDetectionAlgorithm nda = new NurminenDetectionAlgorithm();
        ExtractionAlgorithm algExtractor;

        ObjectExtractor extractor = new ObjectExtractor(pdf);
        List<Table> tables = new ArrayList<>();
        PageIterator pages = extractor.extract();

        if (pageNumber == -1) {
            while (pages.hasNext()) {
                Page page = pages.next();
                if (sea.isTabular(page)) {
                    algExtractor = new SpreadsheetExtractionAlgorithm();
                } else {
                    algExtractor = new BasicExtractionAlgorithm();
                }
                List<Rectangle> tablesOnPage = nda.detect(page);
                for (Rectangle guessRect : tablesOnPage) {
                    Page guess = page.getArea(guessRect);
                    tables.addAll(algExtractor.extract(guess));
                }
            }
        } else {
            Page page = extractor.extract(pageNumber);
            if (sea.isTabular(page)) {
                algExtractor = new SpreadsheetExtractionAlgorithm();
            } else {
                algExtractor = new BasicExtractionAlgorithm();
            }
            List<Rectangle> tablesOnPage = nda.detect(page);
            for (Rectangle guessRect : tablesOnPage) {
                Page guess = page.getArea(guessRect);
                tables.addAll(algExtractor.extract(guess));
            }
        }
        if (tables.isEmpty()) {
            throw new PdfMantisException("Could not detect any tables for extraction");
        }
        return tables;
    }

    public String prettifyTable(Table table) {
        return buildPrettyTable(table);
    }

    public List<String> prettifyTables(List<Table> tables) {

        List<String> prettyTables = new ArrayList<>();

        for (Table table : tables) {
            prettyTables.add(buildPrettyTable(table));
        }

        return prettyTables;
    }

    private String buildPrettyTable(Table table) {
        List<String> header = new ArrayList<>();
        List<List<String>> data = new ArrayList<>();

        for (int r = 0; r < table.getRowCount(); r++) {
            List<String> dataRow = new ArrayList<>();
            for (int c = 0; c < table.getColCount(); c++) {
                if (r == 0) {
                    header.add(table.getCell(r, c).getText());
                } else {
                    dataRow.add(table.getCell(r, c).getText());
                }
            }
            if (r != 0) {
                data.add(dataRow);
            }
        }

        String[] prepareHeader = header.toArray(new String[0]);
        String[][] prepareData = new String[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            ArrayList<String> row = (ArrayList<String>) data.get(i);
            prepareData[i] = row.toArray(new String[0]);
        }
        return FlipTable.of(prepareHeader, prepareData);
    }
}
