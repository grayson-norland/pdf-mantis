package com.graysonnorland.pdfmantis.index.printer;

import com.graysonnorland.pdfmantis.index.object.TextIndex;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TextIndexPrinter extends PDFTextStripper {

    public TextIndexPrinter() throws IOException {
        super();
        setSuppressDuplicateOverlappingText(false);

        addOperator(new org.apache.pdfbox.contentstream.operator.color.SetStrokingColorSpace());
        addOperator(new org.apache.pdfbox.contentstream.operator.color.SetNonStrokingColorSpace());
        addOperator(new org.apache.pdfbox.contentstream.operator.color.SetStrokingColor());
        addOperator(new org.apache.pdfbox.contentstream.operator.color.SetNonStrokingColor());
        addOperator(new org.apache.pdfbox.contentstream.operator.color.SetStrokingColorN());
        addOperator(new org.apache.pdfbox.contentstream.operator.color.SetNonStrokingColorN());
        addOperator(new org.apache.pdfbox.contentstream.operator.color.SetStrokingDeviceGrayColor());
        addOperator(new org.apache.pdfbox.contentstream.operator.color.SetNonStrokingDeviceGrayColor());
        addOperator(new org.apache.pdfbox.contentstream.operator.color.SetStrokingDeviceRGBColor());
        addOperator(new org.apache.pdfbox.contentstream.operator.color.SetNonStrokingDeviceRGBColor());
        addOperator(new org.apache.pdfbox.contentstream.operator.color.SetStrokingDeviceCMYKColor());
        addOperator(new org.apache.pdfbox.contentstream.operator.color.SetNonStrokingDeviceCMYKColor());
    }

    private final List<TextIndex> index = new ArrayList<>();
    private final Map<TextPosition, RenderingMode> renderingMode = new HashMap<>();
    private final Map<TextPosition, float[]> strokingColor = new HashMap<>();
    private final Map<TextPosition, float[]> nonStrokingColor = new HashMap<>();

    final static List<RenderingMode> FILLING_MODES = Arrays.asList(RenderingMode.FILL, RenderingMode.FILL_STROKE, RenderingMode.FILL_CLIP, RenderingMode.FILL_STROKE_CLIP);
    final static List<RenderingMode> STROKING_MODES = Arrays.asList(RenderingMode.STROKE, RenderingMode.FILL_STROKE, RenderingMode.STROKE_CLIP, RenderingMode.FILL_STROKE_CLIP);
    final static List<RenderingMode> CLIPPING_MODES = Arrays.asList(RenderingMode.FILL_CLIP, RenderingMode.STROKE_CLIP, RenderingMode.FILL_STROKE_CLIP, RenderingMode.NEITHER_CLIP);


    @Override
    protected void processTextPosition(TextPosition text) {
        renderingMode.put(text, getGraphicsState().getTextState().getRenderingMode());
        strokingColor.put(text, getGraphicsState().getStrokingColor().getComponents());
        nonStrokingColor.put(text, getGraphicsState().getNonStrokingColor().getComponents());

        super.processTextPosition(text);
    }

    @Override
    protected void writeString(String string, List<TextPosition> textPositions) {
        String wordSeparator = getWordSeparator();
        List<TextPosition> word = new ArrayList<>();
        for (TextPosition text : textPositions) {
            String character = text.getUnicode();
            if (character != null) {
                if (character.length() >= 1) {
                    if (!character.equals(wordSeparator)) {
                        word.add(text);
                    } else if (!word.isEmpty()) {
                        printWord(word);
                        word.clear();
                    }
                }
            }
        }
        if (!word.isEmpty()) {
            printWord(word);
            word.clear();
        }
        TextIndexPrinterCaptor coordinatesPrinterCapturer = TextIndexPrinterCaptor.getInstance();
        coordinatesPrinterCapturer.setIndex(index);
    }

    private void printWord(List<TextPosition> word) {
        Rectangle2D boundingBox = null;
        StringBuilder builder = new StringBuilder();

        for (TextPosition text : word) {

            RenderingMode charRenderingMode = renderingMode.get(text);
            float[] charStrokingColor = strokingColor.get(text);
            float[] charNonStrokingColor = nonStrokingColor.get(text);

            StringBuilder textBuilder = new StringBuilder();

            if (FILLING_MODES.contains(charRenderingMode)) {
                textBuilder.append("FILL:").append(toString(charNonStrokingColor)).append(';');
            }

            if (STROKING_MODES.contains(charRenderingMode)) {
                textBuilder.append("STROKE:").append(toString(charStrokingColor)).append(';');
            }

            if (CLIPPING_MODES.contains(charRenderingMode)) {
                textBuilder.append("CLIP;");
            }

            Rectangle2D box =
                    new Rectangle2D.Float(
                            text.getXDirAdj(),
                            text.getYDirAdj(),
                            text.getWidthDirAdj(),
                            text.getHeightDir());
            if (boundingBox == null)
                boundingBox = box;
            else
                boundingBox.add(box);
            builder.append(text.getUnicode());

            index.add(new TextIndex(
                    getCurrentPageNo(),
                    builder.toString(),
                    text.getFont().getName(),
                    String.valueOf(text.getFontSizeInPt()),
                    textBuilder.toString(),
                    boundingBox.getX(),
                    boundingBox.getY(),
                    boundingBox.getHeight(),
                    boundingBox.getWidth()));
        }
    }

    private String toString(float[] values) {
        if (values == null)
            return "null";
        StringBuilder builder = new StringBuilder();
        switch (values.length) {
            case 1:
                builder.append("GRAY");
                break;
            case 3:
                builder.append("RGB");
                break;
            case 4:
                builder.append("CMYK");
                break;
            default:
                builder.append("UNKNOWN");
        }
        for (float f : values) {
            builder.append(' ')
                    .append(f);
        }
        return builder.toString();
    }
}
