package com.xdavide9.gui;

import com.xdavide9.services.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Getter
public class Gui implements ActionListener {

    private JFrame frame;
    private JTextArea textArea;
    private JScrollPane scrollPane;

    private final FileService fileService;
    private final EditService editService;
    private final FormatService formatService;
    private final HelpService helpService;

    public Gui(String title, int x, int y, int width, int height, Font font, boolean lineWrap) {
        fileService = new FileService(this, new String[]{"Save", "Don't Save", "Cancel"});
        editService = new EditService(this, 1000);
        formatService = new FormatService(this);
        helpService = new HelpService(this);

        createFrame(title, x, y, width, height);
        createTextArea(font, lineWrap);

        frame.setVisible(true);
    }

    private void createFrame(String title, int x, int y, int width, int height) {
        frame = new JFrame(title);
        frame.setIconImage(icon()); //todo improve with optionals
        frame.setBounds(x, y, width, height);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fileService.exit();
            }
        });

        frame.setJMenuBar(new MenuBarService(this).getMenuBar());
    }

    private void createTextArea(Font font, boolean lineWrap) {
        textArea = new JTextArea();
        textArea.setFont(font);
        textArea.setLineWrap(lineWrap);
        textArea.setWrapStyleWord(true);
        textArea.getDocument().addUndoableEditListener(e -> editService.getUndoManager().addEdit(e.getEdit()));

        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        if (lineWrap)
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        else
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        frame.add(scrollPane);
    }

    public Image icon() {
        if (Files.exists(Paths.get("src/main/resources/icon.png"))) {
            log.info("Icon provided");
            return new ImageIcon("src/main/resources/icon.png").getImage();
        }
        log.info("No Icon provided");
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            // file
            case "New" -> fileService.New();
            case "SaveAs" -> fileService.saveAs();
            case "Save" -> fileService.save();
            case "Open" -> fileService.open();
            case "Exit" -> fileService.exit();
            // edit
            case "Undo" -> editService.undo();
            case "Redo" -> editService.redo();
            case "Copy" -> editService.copy();
            case "Paste" -> editService.paste();
            case "Cut" -> editService.cut();
            // format
            case "Font..." -> formatService.font("Select Font", "Apply");
            case "Line Wrap" -> formatService.lineWrap();
            // help
            case "Contact" -> helpService.contact();
        }
    }
}
