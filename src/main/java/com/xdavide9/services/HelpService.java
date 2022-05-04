package com.xdavide9.services;

import com.xdavide9.gui.Gui;

import javax.swing.*;

public record HelpService (Gui gui) {

    public void contact() {
        JOptionPane.showMessageDialog(gui.getFrame(), "Message me on Discord at xDavide9#1490");
    }

}