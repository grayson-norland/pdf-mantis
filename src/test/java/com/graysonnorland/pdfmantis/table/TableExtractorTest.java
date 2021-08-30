package com.graysonnorland.pdfmantis.table;

import com.graysonnorland.pdfmantis.core.PdfMantis;
import com.graysonnorland.pdfmantis.exception.PdfMantisException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Test;
import technology.tabula.Table;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TableExtractorTest {

    private static final String TABLES_PDF = "tables.pdf";
    private static final String ALL_TABLES = "all_tables";
    private static final String FIRST_TABLE = "first_table";

    private PdfMantis pdf;

    @After
    public void cleanPdf() throws IOException {
        if (pdf != null) {
            pdf.closePdf();
        }
    }

    @Test
    public void extractPage() throws IOException, PdfMantisException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(TABLES_PDF));
        Table table = pdf.getTable().extractFromPage(1).get(0);
        assertEquals("Number of Coils", table.getCell(0, 0).getText());
        assertEquals("Number of Paperclips", table.getCell(0, 1).getText());
        assertEquals("5", table.getCell(1, 0).getText());
        assertEquals("3, 5, 4", table.getCell(1, 1).getText());
        assertEquals("10", table.getCell(2, 0).getText());
        assertEquals("7, 8, 6", table.getCell(2, 1).getText());
        assertEquals("15", table.getCell(3, 0).getText());
        assertEquals("11, 10, 12", table.getCell(3, 1).getText());
        assertEquals("20", table.getCell(4, 0).getText());
        assertEquals("15, 13, 14", table.getCell(4, 1).getText());
    }

    @Test
    public void extractAll() throws IOException, PdfMantisException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(TABLES_PDF));
        List<String> tables = pdf.getTable().prettifyTables(pdf.getTable().extractAll());
        String expectedTables = normalizeString(IOUtils.toString(
                this.getClass().getResourceAsStream(ALL_TABLES),
                StandardCharsets.UTF_8));
        assertEquals(expectedTables, normalizeString(String.join("", tables)));
    }

    @Test
    public void prettifyTable() throws IOException, PdfMantisException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(TABLES_PDF));
        String table = pdf.getTable().prettifyTable(pdf.getTable().extractFromPage(1).get(0));
        String expectedTable = normalizeString(IOUtils.toString(
                this.getClass().getResourceAsStream(FIRST_TABLE),
                StandardCharsets.UTF_8));
        assertEquals(expectedTable, normalizeString(String.join("", table)));
    }

    private String normalizeString(String input) {
        return StringUtils.normalizeSpace(input);
    }
}
