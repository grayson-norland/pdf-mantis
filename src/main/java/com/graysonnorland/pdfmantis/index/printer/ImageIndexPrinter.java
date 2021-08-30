package com.graysonnorland.pdfmantis.index.printer;

import com.graysonnorland.pdfmantis.index.object.ImageIndex;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.contentstream.operator.DrawObject;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.contentstream.PDFStreamEngine;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.state.Concatenate;
import org.apache.pdfbox.contentstream.operator.state.Restore;
import org.apache.pdfbox.contentstream.operator.state.Save;
import org.apache.pdfbox.contentstream.operator.state.SetGraphicsStateParameters;
import org.apache.pdfbox.contentstream.operator.state.SetMatrix;

public class ImageIndexPrinter extends PDFStreamEngine {

    private static int imageCount = 0;
    private static int pageNumber = 0;

    public ImageIndexPrinter() {
        addOperator(new Concatenate());
        addOperator(new DrawObject());
        addOperator(new SetGraphicsStateParameters());
        addOperator(new Save());
        addOperator(new Restore());
        addOperator(new SetMatrix());
    }

    public void getImageIndex(PDDocument pdf) throws IOException {
        ImageIndexPrinter printer = new ImageIndexPrinter();
        for (PDPage page : pdf.getPages()) {
            pageNumber = pdf.getPages().indexOf(page) + 1;
            printer.processPage(page);
        }
    }

    @Override
    protected void processOperator(Operator operator, List<COSBase> operands) throws IOException {
        String operation = operator.getName();
        if ("Do".equals(operation)) {
            COSName objectName = (COSName) operands.get(0);
            PDXObject xObject = getResources().getXObject(objectName);
            if (xObject instanceof PDImageXObject) {
                imageCount++;
                PDImageXObject image = (PDImageXObject) xObject;
                Matrix ctmNew = getGraphicsState().getCurrentTransformationMatrix();

                String uniqueName = "page_" + pageNumber + "_" + objectName.getName() + "_" + imageCount;
                double imageWidth = image.getWidth();
                double imageHeight = image.getHeight();
                double imageX = ctmNew.getTranslateX();
                double imageY = ctmNew.getTranslateY();
                ImageIndexPrinterCaptor coordinatesPrinterCapturer = ImageIndexPrinterCaptor.getInstance();
                coordinatesPrinterCapturer.addIndex(
                        new ImageIndex(pageNumber, uniqueName, imageX, imageY, imageHeight, imageWidth, image.getImage()));

            } else if (xObject instanceof PDFormXObject) {
                PDFormXObject form = (PDFormXObject) xObject;
                showForm(form);
            }
        } else {
            super.processOperator(operator, operands);
        }
    }
}
