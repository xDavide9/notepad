package com.xdavide9.services;

import com.xdavide9.gui.Gui;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FormatService {

    private final Gui gui;
    private Font font;

    public FormatService(Gui gui) {
        this.gui = gui;
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

    public void font(String title, String buttonText) {
        JFrame frame = new JFrame(title);
        frame.setLocationRelativeTo(gui.getFrame());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setIconImage(gui.icon());
        frame.setLayout(new GridBagLayout());

        //font names
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        JList<String> namesList = new JList<>(fonts);
        namesList.setSelectedIndex(0);      // default selected font: the first in the list

        JScrollPane namesScrollPane = new JScrollPane(namesList);
        namesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

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
        JButton button = new JButton(buttonText);
        button.addActionListener(e -> {
            String fontName = namesList.getSelectedValue();
            int fontStyle = stylesList.getSelectedIndex();
            Integer fontSize = sizesList.getSelectedValue();
            font = new Font(fontName, fontStyle, fontSize);
            gui.getTextArea().setFont(font);
            log.info("Font = {}", font);

            frame.dispose();
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
        frame.setVisible(true);
    }

}
