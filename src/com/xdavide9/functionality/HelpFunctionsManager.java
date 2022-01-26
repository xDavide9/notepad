package com.xdavide9.functionality;

import com.xdavide9.gui.Gui;

import javax.swing.*;

public class HelpFunctionsManager {

    private final Gui gui;

    public HelpFunctionsManager(Gui gui) {
        this.gui = gui;
    }

    public void contact() {
        JOptionPane.showMessageDialog(gui.getFrame(), "Message me on Discord at xDavide9#1490");
    }

}