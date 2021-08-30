package com.graysonnorland.pdfmantis.index;

import com.graysonnorland.pdfmantis.index.object.TextIndex;
import com.graysonnorland.pdfmantis.core.PdfMantis;
import com.graysonnorland.pdfmantis.exception.PdfMantisException;
import com.graysonnorland.pdfmantis.util.StreamReader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TextIndexExtractorTest {

    private static final String FONT_PDF = "font.pdf";
    private static final String TEXT_INDEX_PDF = "text_index.pdf";
    private static final String MULTILINE_STRING = "multiline_string";
    private static final String EXPECTED_WORD_INDEX = "expected_word_index";
    private static final String EXPECTED_UNICODE_INDEX = "expected_unicode_index";

    private PdfMantis pdf;

    @After
    public void cleanPdf() throws IOException {
        if (pdf != null) {
            pdf.closePdf();
        }
    }

    @Test
    public void buildUnicodeIndex() throws IOException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(TEXT_INDEX_PDF));
        List<String> actualIndex = pdf.getTextIndex().prettifyIndex(
                pdf.getTextIndex().buildUnicodeIndex());
        List<String> expectedIndex = new StreamReader().getStringListFromStream(
                this.getClass().getResourceAsStream(EXPECTED_UNICODE_INDEX));
        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void buildWordIndex() throws IOException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(TEXT_INDEX_PDF));
        List<String> actualIndex = pdf.getTextIndex().prettifyIndex(
                pdf.getTextIndex().buildWordIndex());
        List<String> expectedIndex = new StreamReader().getStringListFromStream(
                this.getClass().getResourceAsStream(EXPECTED_WORD_INDEX));
        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void getForStringOneLiner() throws IOException, PdfMantisException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(TEXT_INDEX_PDF));
        List<String> resultsIndex = pdf.getTextIndex().prettifyIndex(
                        pdf.getTextIndex().getForString("Phone: 1-807-476-8329"));
        List<String> expectedIndex = new ArrayList<>();
        expectedIndex.add("Page Number=1,Word=Phone: 1-807-476-8329,Font=ABCDEE+Calibri,Font Size=9.0," +
                "Colour=FILL:GRAY 0.0;,X=72.02400207519531,Y=717.4559936523438,Height=4.980000019073486," +
                "Width=95.64569091796875");
        assertEquals(expectedIndex, resultsIndex);
    }

    @Test
    public void getFromAreaOneLiner() throws IOException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(TEXT_INDEX_PDF));
        assertEquals("Phone: 1-807-476-8329", pdf.getText().getFromArea(
                1, 72.02400207519531, 717.4559936523438,
                4.980000019073486, 95.64569091796875));
    }

    @Test
    public void getForStringMultiLine() throws IOException, PdfMantisException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(TEXT_INDEX_PDF));
        List<String> resultsIndex = pdf.getTextIndex().prettifyIndex(
                pdf.getTextIndex().getForString("candidate molten salts"));
        List<String> expectedIndex = new ArrayList<>();
        expectedIndex.add("Page Number=2,Word=candidate molten salts,Font=ABCDEE+Calibri,Font Size=11.0," +
                "Colour=FILL:GRAY 0.0;,X=72.02400207519531,Y=235.34002685546875,Height=20.039958477020264," +
                "Width=466.1418914794922");
        assertEquals(expectedIndex, resultsIndex);
    }

    @Test
    public void getFromAreaMultiLine() throws IOException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(TEXT_INDEX_PDF));
        String result = convertToOneLiner(pdf.getText().getFromArea(
                2, 72.02400207519531, 235.34002685546875,
                20.039958477020264, 466.1418914794922));
        String expected = convertToOneLiner(IOUtils.toString(
                this.getClass().getResourceAsStream(MULTILINE_STRING),
                StandardCharsets.UTF_8));
        assertEquals(expected, result);
    }

    @Test
    public void getFromAreaByTextIndex() throws IOException, PdfMantisException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(TEXT_INDEX_PDF));
        String expectedString = "If molten salts were used as intermediate heat transfer";
        List<TextIndex> resultsIndex = pdf.getTextIndex()
                .getForString(expectedString);
        String actualString = pdf.getText().getFromArea(resultsIndex.get(0));
        assertEquals(expectedString, actualString);
    }

    @Test
    public void getFont() throws IOException, PdfMantisException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(FONT_PDF));
        TextIndex result = pdf.getTextIndex().getForString("Figure SPM.7").get(0);
        assertEquals("DQHJSU+FrutigerLTPro-BoldCn", result.getFont());
    }

    @Test
    public void getMultiFont() throws IOException, PdfMantisException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(FONT_PDF));
        TextIndex result = pdf.getTextIndex().getForString("Figure SPM.7 | CMIP5").get(0);
        assertEquals("DQHJSU+FrutigerLTPro-BoldCn||UWVHWM+FrutigerLTPro-LightCn", result.getFont());
    }

    @Test
    public void getFontSize() throws IOException, PdfMantisException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(FONT_PDF));
        TextIndex result = pdf.getTextIndex().getForString("Summary for Policymakers").get(0);
        assertEquals("8.0", result.getFontSize());
    }

    @Test
    public void getFontMultiSize() throws IOException, PdfMantisException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(FONT_PDF));
        TextIndex result = pdf.getTextIndex().getForString("(a) Global").get(0);
        assertEquals("13.0||11.0", result.getFontSize());
    }

    @Test
    public void getColour() throws IOException, PdfMantisException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(FONT_PDF));
        TextIndex result = pdf.getTextIndex().getForString("Summary for Policymakers").get(0);
        assertEquals("FILL:CMYK 0.2 0.56 1.0 0.05;", result.getColour());
    }

    @Test
    public void getMultiColour() throws IOException, PdfMantisException {
        pdf = new PdfMantis(this.getClass().getResourceAsStream(FONT_PDF));
        TextIndex result = pdf.getTextIndex().getForString("Summary for Policymakers (a)").get(0);
        assertEquals("FILL:CMYK 0.2 0.56 1.0 0.05;||FILL:CMYK 0.698 0.675 0.639 0.74;", result.getColour());
    }

    private String convertToOneLiner(String input) {
        return StringUtils.normalizeSpace(input);
    }

}
