package com.graysonnorland.pdfmantis.index.object;

public class TextIndex {

    private int pageNumber;
    private String word;
    private String font;
    private String fontSize;
    private String colour;
    private double x;
    private double y;
    private double h;
    private double w;

    public TextIndex(int pageNumber, String word, String font, String fontSize, String colour, double x, double y, double h, double w) {
        this.pageNumber = pageNumber;
        this.word = word;
        this.font = font;
        this.fontSize = fontSize;
        this.colour = colour;
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
    }

    public TextIndex() {
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public String getWord() {
        return word;
    }

    public String getFont() {
        return font;
    }

    public String getFontSize() {
        return fontSize;
    }

    public String getColour() {
        return colour;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getHeight() {
        return h;
    }

    public double getWidth() {
        return w;
    }
}
