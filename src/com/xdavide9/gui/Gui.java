package com.xdavide9.gui;

import com.xdavide9.BetterNotePad;
import com.xdavide9.functionality.EditFunctionsManager;
import com.xdavide9.functionality.FileFunctionsManager;
import com.xdavide9.functionality.FormatFunctionsManager;
import com.xdavide9.functionality.HelpFunctionsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Gui implements ActionListener {

    private JFrame frame;
    private JTextArea textArea;
    private JScrollPane scrollPane;

    private final FileFunctionsManager fManager;
    private final EditFunctionsManager eManager;
    private final FormatFunctionsManager formatManger;
    private final HelpFunctionsManager helpManager;

    public Gui(String title, int x, int y, int width, int height, Font font, boolean lineWrap) {
        String[] options = {"Save", "Don't Save", "Cancel"};
        fManager = new FileFunctionsManager(this, options);
        eManager = new EditFunctionsManager(this, 1000);
        formatManger = new FormatFunctionsManager(this);
        helpManager = new HelpFunctionsManager(this);

        createFrame(title, x, y, width, height);
        createTextArea(font, lineWrap);

        frame.setVisible(true);
    }

    private void createFrame(String title, int x, int y, int width, int height) {
        frame = new JFrame(title);
        frame.setIconImage(BetterNotePad.getIcon());
        frame.setBounds(x, y, width, height);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fManager.exit();
            }
        });

        frame.setJMenuBar(new MenuBarManager(this).getMenuBar());
    }

    private void createTextArea(Font font, boolean lineWrap) {
        textArea = new JTextArea();
        textArea.setFont(font);
        textArea.setLineWrap(lineWrap);
        textArea.setWrapStyleWord(true);
        textArea.getDocument().addUndoableEditListener(e -> eManager.getUndoManager().addEdit(e.getEdit()));

        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        if (lineWrap)
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        else
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        frame.add(scrollPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            //file
            case "New" -> fManager.New();
            case "SaveAs" -> fManager.saveAs();
            case "Save" -> fManager.save();
            case "Open" -> fManager.open();
            case "Exit" -> fManager.exit();
            //edit
            case "Undo" -> eManager.undo();
            case "Redo" -> eManager.redo();
            case "Copy" -> eManager.copy();
            case "Paste" -> eManager.paste();
            case "Cut" -> eManager.cut();
            //format
            case "Font..." -> formatManger.font("Select Font", "Apply");
            case "Line Wrap" -> formatManger.lineWrap();
            //help
            case "Contact" -> helpManager.contact();
        }
    }

    // GETTERS

    public JFrame getFrame() {
        return frame;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public FileFunctionsManager getfManager() {
        return fManager;
    }
}
