package com.xdavide9.jnotepad.services;

import com.xdavide9.jnotepad.JNotepad;
import com.xdavide9.jnotepad.util.OperatingSystem;
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

    /** Key used for shortcuts */
    int shortcutKey;

    public MenuBarService(ActionListener listener) {
        shortcutKey = JNotepad.os == OperatingSystem.MAC ? KeyEvent.META_DOWN_MASK : KeyEvent.CTRL_DOWN_MASK;
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
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, shortcutKey));
        fileMenu.add(newItem);

        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(listener);
        openItem.setActionCommand("Open");
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, shortcutKey));
        fileMenu.add(openItem);

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(listener);
        saveItem.setActionCommand("Save");
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, shortcutKey));
        fileMenu.add(saveItem);

        JMenuItem saveAsItem = new JMenuItem("Save As");
        saveAsItem.addActionListener(listener);
        saveAsItem.setActionCommand("SaveAs");
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,  shortcutKey | KeyEvent.SHIFT_DOWN_MASK));
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
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, shortcutKey));
        editMenu.add(undoItem);

        JMenuItem redoItem = new JMenuItem("Redo");
        redoItem.addActionListener(listener);
        redoItem.setActionCommand("Redo");
        redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, shortcutKey));
        editMenu.add(redoItem);

        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.addActionListener(listener);
        copyItem.setActionCommand("Copy");
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, shortcutKey));
        editMenu.add(copyItem);

        JMenuItem pasteItem = new JMenuItem("Paste");
        pasteItem.addActionListener(listener);
        pasteItem.setActionCommand("Paste");
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, shortcutKey));
        editMenu.add(pasteItem);

        JMenuItem cutItem = new JMenuItem("Cut");
        cutItem.addActionListener(listener);
        cutItem.setActionCommand("Cut");
        cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, shortcutKey));
        editMenu.add(cutItem);

        JMenuItem findItem = new JMenuItem("Find...");
        findItem.addActionListener(listener);
        findItem.setActionCommand("Find...");
        findItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, shortcutKey));
        editMenu.add(findItem);
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