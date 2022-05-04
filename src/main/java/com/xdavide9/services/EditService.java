package com.xdavide9.services;

import com.xdavide9.gui.Gui;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

@Slf4j
public class EditService {

    private final Gui gui;

    @Getter
    private final UndoManager undoManager;

    public EditService(Gui gui, int undoManagerEditLimit) {
        this.gui = gui;
        undoManager = new UndoManager();
        undoManager.setLimit(undoManagerEditLimit);
    }

    public void undo() {
        try {
            undoManager.undo();
            log.info("Successfully undid");
        } catch (CannotUndoException e) {
            log.error("Could not undo", e);
        }
    }

    public void redo() {
        try {
            undoManager.redo();
            log.info("Successfully redid");
        } catch (CannotRedoException e) {
            log.error("Could not redo", e);
        }

    }

    public void copy() {
        gui.getTextArea().copy();
        log.info("Successfully copy text");
    }

    public void paste() {
        gui.getTextArea().paste();
        log.info("Successfully pasted text");
    }

    public void cut() {
        gui.getTextArea().cut();
        log.info("Successfully cut text");
    }
}
