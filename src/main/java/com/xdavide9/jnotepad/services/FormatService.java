package com.xdavide9.jnotepad.services;

import com.xdavide9.jnotepad.JNotepad;
import com.xdavide9.jnotepad.gui.Gui;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
public class FormatService {

    private final Gui gui;
    private Font font;
    private final JFrame frame;
    private final JList<String> namesList;
    private final DefaultListModel<String> namesListModel;
    private final JScrollPane namesScrollPane;

    /** Links HTML stylized font names (to be used in a JList) to each font name */
    private final LinkedHashMap<String, String> htmlToFont;

    public FormatService(Gui gui) {
        this.gui = gui;
        frame = new JFrame("Select Font");
        frame.setLocationRelativeTo(gui.getFrame());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setIconImage(gui.icon());
        frame.setLayout(new GridBagLayout());

        font = JNotepad.configuration.getFont();

        //font names
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        htmlToFont = new LinkedHashMap<>();
        for (String value : fonts) {
            htmlToFont.put(fontNameToHTML(value), value);
        }

        namesListModel = new DefaultListModel<>();
        namesListModel.addAll(htmlToFont.keySet().stream().toList());

        //display the HTML in the JList
        namesList = new JList<>(namesListModel);

        namesScrollPane = new JScrollPane(namesList);
        namesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        namesScrollPane.setPreferredSize(new Dimension((int)namesScrollPane.getPreferredSize().getWidth(), 184));
        moveCurrentFontToTopJList();

        // font styles
        String[] styles = {"PLAIN", "BOLD", "ITALIC"};
        JList<String> stylesList = new JList<>(styles);
        stylesList.setSelectedIndex(0);     //default selected size: PLAIN

        JScrollPane stylesScrollPane = new JScrollPane(stylesList);
        stylesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // font sizes
        List<Integer> s = new ArrayList<>();
        for (int i = 8; i <= 96; i += 2)
            s.add(i);
        Integer[] sizes = s.toArray(new Integer[0]);
        JList<Integer> sizesList = new JList<>(sizes);
        sizesList.setSelectedIndex(7);      //default selected size: 22

        JScrollPane sizesScrollPane = new JScrollPane(sizesList);
        sizesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // button
        JButton button = new JButton("Apply");
        button.addActionListener(e -> {
            String fontName = htmlToFont.get(namesList.getSelectedValue());
            int fontStyle = stylesList.getSelectedIndex();
            Integer fontSize = sizesList.getSelectedValue();
            font = new Font(fontName, fontStyle, fontSize);
            gui.getTextArea().setFont(font);
            log.info("Font = {}", font);

            frame.setVisible(false);

            reOrderTopJListFont();
            moveCurrentFontToTopJList();

            log.info("button press font: "+font.getName()+"|"+namesList.getSelectedValue());
        });

        GridBagConstraints gb = new GridBagConstraints();
        gb.insets = new Insets(8, 8, 8, 8);
        gb.gridx = 0;
        gb.gridy = 0;
        frame.add(namesScrollPane, gb);
        gb.gridx = 1;
        frame.add(stylesScrollPane, gb);
        gb.gridx = 2;
        frame.add(sizesScrollPane, gb);
        gb.gridy = 1;
        gb.insets = new Insets(0, 8, 8, 8);
        frame.add(button, gb);

        frame.pack();
    }

    public void lineWrap() {
        if (gui.getTextArea().getLineWrap()) {
            gui.getScrollPane().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            gui.getTextArea().setLineWrap(false);
            log.info("Line wrap = {}", gui.getTextArea().getLineWrap());
        } else {
            gui.getScrollPane().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            gui.getTextArea().setLineWrap(true);
            log.info("Line wrap = {}", gui.getTextArea().getLineWrap());
        }
    }

    public void font() {
        SwingUtilities.invokeLater(() -> {
            if (!frame.isVisible()) {
                frame.setLocationRelativeTo(gui.getFrame());
                frame.setVisible(true);
            }
            frame.requestFocus();
        });
    }

    /** Moves the first font inside the namesList JList back to its initial location (the order of fonts that was set by
     * GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) */
    private void reOrderTopJListFont() {
        String formerTopElement = namesListModel.remove(0);

        int iterator = 0;
        for(String key : htmlToFont.keySet()) {
            if(key.equals(formerTopElement)) {
                namesListModel.insertElementAt(formerTopElement, iterator);
                break;
            }
            iterator++;
        }
    }

    /**
     * Inside the namesList JList, moves the font currently used by Gui.textArea to the top
     */
    private void moveCurrentFontToTopJList() {
        namesListModel.insertElementAt(fontNameToHTML(font.getName()), 0);

        for(int i = 1; i < namesListModel.size(); i++) {

            //delete where the new font is inside the JList
            if(namesListModel.get(i).equals(fontNameToHTML(font.getName()))) {
                namesListModel.remove(i);
                break;
            }
        }

        namesScrollPane.getVerticalScrollBar().setValue(0);
        namesList.setSelectedIndex(0);
    }

    /** Converts a font name into HTML that applies the font, and displays the font name
     *
     * @param fontName the name of a font
     * @return HTML that applies the font, and displays the font name
     */
    private String fontNameToHTML(String fontName) {
        return "<html><font face=\"" + fontName + "\">" + fontName + "</font></html>";
    }

}