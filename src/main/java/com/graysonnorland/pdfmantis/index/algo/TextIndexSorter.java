package com.graysonnorland.pdfmantis.index.algo;

import com.graysonnorland.pdfmantis.index.object.TextIndex;

import java.util.List;
import java.util.stream.Collectors;

public class TextIndexSorter {

    public TextIndex determineIndex(List<TextIndex> index, String string) {

        int pageNumber = index.stream()
                .mapToInt(TextIndex::getPageNumber)
                .findFirst().orElseThrow(IllegalStateException::new);

        String font = index.stream().map(TextIndex::getFont)
                .distinct().collect(Collectors.joining("||"));

        String fontSize = index.stream().map(TextIndex::getFontSize)
                .distinct().collect(Collectors.joining("||"));

        String colour = index.stream().map(TextIndex::getColour)
                .distinct().collect(Collectors.joining("||"));

        double x = index.stream()
                .mapToDouble(TextIndex::getX)
                .min().orElseThrow(IllegalStateException::new);

        double y = index.stream()
                .mapToDouble(TextIndex::getY)
                .min().orElseThrow(IllegalStateException::new);

        double h = determineHeight(index, y);

        double w = determineWidth(index, x);

        return new TextIndex(pageNumber, string, font, fontSize, colour, x, y, h, w);
    }

    private double determineWidth(List<TextIndex> index, double x) {

        double maxX = index.stream()
                .mapToDouble(TextIndex::getX)
                .max().orElseThrow(IllegalStateException::new);

        if (stringBreaksToNewLine(index)) {
            double maxW = index.stream()
                    .mapToDouble(TextIndex::getWidth)
                    .max().orElseThrow(IllegalStateException::new);
            return (maxX - x) + maxW;
        } else {
            double lastW = index.stream()
                    .mapToDouble(TextIndex::getWidth)
                    .reduce((first, second) -> second)
                    .orElseThrow(IllegalStateException::new);
            return (maxX - x) + lastW;
        }
    }

    private double determineHeight(List<TextIndex> index, double y) {

        double maxY = index.stream()
                .mapToDouble(TextIndex::getY)
                .max().orElseThrow(IllegalStateException::new);

        double maxH = index.stream()
                .mapToDouble(TextIndex::getHeight)
                .max().orElseThrow(IllegalStateException::new);

        return (maxY - y) + maxH;
    }

    private boolean stringBreaksToNewLine(List<TextIndex> index) {
        Double lastX = null;

        for (double x : index.stream()
                .mapToDouble(TextIndex::getX)
                .toArray()) {
            if (lastX == null) {
                lastX = x;
            } else {
                if (x < lastX) {
                    return true;
                }
            }
        }
        return false;
    }
}
