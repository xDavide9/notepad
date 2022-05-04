package com.xdavide9.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class Configuration implements Serializable {
    private String title;
    private int x, y, width, height;
    private Font font;
    private boolean lineWrap;
}
