package com.xdavide9.services;

import lombok.Getter;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

public class MenuBarService {

    @Getter
    private final JMenuBar menuBar;
    private JMenu fileMenu, editMenu, formatMenu, helpMenu;

    public MenuBarService(ActionListener listener) {
        menuBar = new JMenuBar();

        createMenus();
        createFileItems(listener);
        createEditItems(listener);
        createFormatItems(listener);
        createHelpItems(listener);
    }

    private void createMenus() {
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        formatMenu = new JMenu("Format");
        helpMenu = new JMenu("Help");

        List<JMenu> menus = Arrays.asList(fileMenu, editMenu, formatMenu, helpMenu);
        menus.forEach(menu -> {
            menu.getPopupMenu().setBorder(null);
            menuBar.add(menu);
        });
    }

    private void createFileItems(ActionListener listener) {
        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(listener);
        newItem.setActionCommand("New");
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        fileMenu.add(newItem);

        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(listener);
        openItem.setActionCommand("Open");
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        fileMenu.add(openItem);

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(listener);
        saveItem.setActionCommand("Save");
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        fileMenu.add(saveItem);

        JMenuItem saveAsItem = new JMenuItem("Save As");
        saveAsItem.addActionListener(listener);
        saveAsItem.setActionCommand("SaveAs");
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,  KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        fileMenu.add(saveAsItem);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(listener);
        exitItem.setActionCommand("Exit");
        fileMenu.add(exitItem);
    }

    private void createEditItems(ActionListener listener) {
        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.addActionListener(listener);
        undoItem.setActionCommand("Undo");
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        editMenu.add(undoItem);

        JMenuItem redoItem = new JMenuItem("Redo");
        redoItem.addActionListener(listener);
        redoItem.setActionCommand("Redo");
        redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
        editMenu.add(redoItem);

        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.addActionListener(listener);
        copyItem.setActionCommand("Copy");
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        editMenu.add(copyItem);

        JMenuItem pasteItem = new JMenuItem("Paste");
        pasteItem.addActionListener(listener);
        pasteItem.setActionCommand("Paste");
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        editMenu.add(pasteItem);

        JMenuItem cutItem = new JMenuItem("Cut");
        cutItem.addActionListener(listener);
        cutItem.setActionCommand("Cut");
        cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        editMenu.add(cutItem);
    }

    private void createFormatItems(ActionListener listener) {
        JMenuItem lineWrapItem = new JMenuItem("Line Wrap");
        lineWrapItem.addActionListener(listener);
        lineWrapItem.setActionCommand("Line Wrap");
        formatMenu.add(lineWrapItem);

        JMenuItem fontItem = new JMenuItem("Font...");
        fontItem.addActionListener(listener);
        fontItem.setActionCommand("Font...");
        formatMenu.add(fontItem);
    }

    public void createHelpItems(ActionListener listener) {
        JMenuItem contactItem = new JMenuItem("Contact");
        contactItem.addActionListener(listener);
        contactItem.setActionCommand("Contact");
        helpMenu.add(contactItem);
    }
}
