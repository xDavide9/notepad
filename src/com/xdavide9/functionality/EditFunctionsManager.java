package com.xdavide9.functionality;

import com.xdavide9.gui.Gui;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class EditFunctionsManager {

    private final Gui gui;
    private final UndoManager undoManager;

    public EditFunctionsManager(Gui gui, int undoManagerEditLimit) {
        this.gui = gui;
        undoManager = new UndoManager();
        undoManager.setLimit(undoManagerEditLimit);
    }

    public void undo() {
        try {
            undoManager.undo();
            System.out.println("Undid Correctly");
        } catch (CannotUndoException e) {
            e.printStackTrace();
            System.err.println("Nothing to undo");
        }
    }

    public void redo() {
        try {
            undoManager.redo();
            System.out.println("Redid Correctly");
        } catch (CannotRedoException e) {
            e.printStackTrace();
            System.err.println("Nothing to redo");
        }

    }

    public void copy() {
        gui.getTextArea().copy();
        System.out.println("Copied");
    }

    public void paste() {
        gui.getTextArea().paste();
        System.out.println("Pasted");
    }

    public void cut() {
        gui.getTextArea().cut();
        System.out.println("Cut");
    }

    // GETTERS

    public UndoManager getUndoManager() {
        return undoManager;
    }
}
