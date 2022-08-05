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

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class HelpService {

    private final Gui gui;
    private final JEditorPane editorPane;

    public HelpService(Gui gui) {
        this.gui = gui;

        JLabel label = new JLabel();
        Font font = label.getFont();

        String style = "font-family:" + font.getFamily() + ";" + "font-weight:" + (font.isBold() ? "bold" : "normal") + ";" +
                "font-size:" + font.getSize() + "pt;";

        editorPane = new JEditorPane(
                "text/html",
                "<html>"
                        + "<body style=\"" + style + "\">"
                        + "Reach out to a contributor on discord: <br>"
                        + "<ul>"
                        + "<li>xDavide9#1490 (original author) </li>"
                        + "<li>Maczi#7367 </li>"
                        + "</ul>"
                        + "</body></html>");

        editorPane.addHyperlinkListener(e -> {
            if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI()); // roll your own link launcher or use Desktop if J6+
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        });

        editorPane.setEditable(false);
        editorPane.setBackground(label.getBackground());
    }

    public void contact() {
        JOptionPane.showMessageDialog(gui.getFrame(), editorPane);
    }

}