package com.xdavide9.jnotepad.services;

import com.xdavide9.jnotepad.gui.Gui;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class HelpService {

    private final Gui gui;
    private JEditorPane ep;

    //code used from: https://stackoverflow.com/q/8348063/7254424
    public HelpService(Gui gui) {
        this.gui = gui;

        // for copying style
        JLabel label = new JLabel();
        Font font = label.getFont();

        // create some css from the label's font
        StringBuffer style = new StringBuffer("font-family:" + font.getFamily() + ";");
        style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
        style.append("font-size:" + font.getSize() + "pt;");

        // html content
        ep = new JEditorPane("text/html", "<html><body style=\"" + style + "\">" //
                + "This version of JNotepad is forked from xDavide9\'s repository<br>"
                + "<ul><li><a href=\"https://github.com/xDavide9/JNotepad\">Original repository</a></li><li>"
                + "<a href=\"https://github.com/woodrow73/JNotepad?organization=woodrow73&organization=woodrow73\">"
                + "Forked repository</a></li></ul></body></html>");

        // handle link events
        ep.addHyperlinkListener(new HyperlinkListener()
        {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e)
            {
                if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI()); // roll your own link launcher or use Desktop if J6+
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        ep.setEditable(false);
        ep.setBackground(label.getBackground());
    }

    public void contact() {
        JOptionPane.showMessageDialog(gui.getFrame(), ep);
    }

}