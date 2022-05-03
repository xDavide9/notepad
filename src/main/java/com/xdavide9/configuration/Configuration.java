package com.xdavide9.configuration;

import java.awt.*;
import java.io.Serializable;

//todo should be a record
public class Configuration implements Serializable {

    // this type holds the fields which need to be serialized

    private int x, y, width, height;
    private Font font;
    private boolean lineWrap;

    public boolean isSet() {
        return font != null;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", font=" + font +
                ", lineWrap=" + lineWrap +
                '}';
    }

    // SETTERS

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setLineWrap(boolean lineWrap) {
        this.lineWrap = lineWrap;
    }

    // GETTERS

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Font getFont() {
        return font;
    }

    public boolean isLineWrap() {
        return lineWrap;
    }
}
