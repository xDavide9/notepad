/**
 * Copyright 2022 xDavide9
 * .
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * .
 *        http://www.apache.org/licenses/LICENSE-2.0
 * .
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.xdavide9.jnotepad.services;

import com.xdavide9.jnotepad.gui.Gui;
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

    public void find() {
        gui.getFindFrame().openFindWindow();
        log.info("Opened Find window");
    }
}