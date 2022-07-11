package com.xdavide9.jnotepad.gui;

import com.xdavide9.jnotepad.services.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

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

    private final Find findFrame;

    public Gui(String title, int x, int y, int width, int height, Font font, boolean lineWrap) {
        createFrame(title, x, y, width, height);
        createTextArea(font, lineWrap);

        fileService = new FileService(this, new String[]{"Save", "Don't Save", "Cancel"});
        editService = new EditService(this, 1000);
        formatService = new FormatService(this);
        helpService = new HelpService(this);

        findFrame = new Find(this, textArea, icon());

        frame.setVisible(true);
    }

    private void createFrame(String title, int x, int y, int width, int height) {
        frame = new JFrame(title);
        frame.setIconImage(icon());
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
        textArea.addMouseListener(new ClosestCaret());

        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        if (lineWrap)
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        else
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        frame.add(scrollPane);
    }

    public Image icon() {
        try {
            Image img = new ImageIcon(Objects.requireNonNull(getClass().getResource("/com/xdavide9/jnotepad/icon.png"))).getImage();
            log.info("Successfully set icon");
            return img;
        } catch (NullPointerException e) {
            log.error("Could not set icon", e);
        }
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
            case "Find..." -> editService.find();
            // format
            case "Font..." -> formatService.font();
            case "Line Wrap" -> formatService.lineWrap();
            // help
            case "Contact" -> helpService.contact();
        }
    }

    /**
     * On mouse press, horizontally move the caret position as close to the mouse as possible, like in Notepad -
     * Default JTextArea behavior is for the caret to always position to the left of the mouse
     */
    private class ClosestCaret extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            // verify that there isn't selected text, and that the caret isn't at the end of the document
            if(textArea.getSelectedText() != null || textArea.getCaretPosition() == textArea.getText().length())
                return;

            try {
                // as per default behavior, the caret will start to the left of the mouse
                Rectangle pixelPositionLeftCaret = textArea.modelToView2D( textArea.getCaretPosition() ).getBounds();
                textArea.setCaretPosition(textArea.getCaretPosition() + 1);
                Rectangle pixelPositionRightCaret = textArea.modelToView2D( textArea.getCaretPosition() ).getBounds();

                // if the caret position to the left is closer, or is on the line above, move caret back to the left
                if(Math.abs(e.getX() - pixelPositionLeftCaret.x) <= Math.abs(e.getX() - pixelPositionRightCaret.x) ||
                        pixelPositionLeftCaret.y != pixelPositionRightCaret.y) {
                    textArea.setCaretPosition(textArea.getCaretPosition() - 1);
                }
            } catch (BadLocationException ex) {
                log.error("Could not set caret position", ex);
            }
        }
    }

}