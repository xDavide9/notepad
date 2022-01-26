package com.xdavide9.functionality;

import com.xdavide9.BetterNotePad;
import com.xdavide9.gui.Gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FormatFunctionsManager {

    private final Gui gui;
    private Font font;

    public FormatFunctionsManager(Gui gui) {
        this.gui = gui;
    }

    public void lineWrap() {
        if (gui.getTextArea().getLineWrap()) {
            gui.getScrollPane().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            gui.getTextArea().setLineWrap(false);
            System.out.println("Line wrap: " + gui.getTextArea().getLineWrap());
        } else {
            gui.getScrollPane().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            gui.getTextArea().setLineWrap(true);
            System.out.println("Line wrap: " + gui.getTextArea().getLineWrap());
        }
    }

    public void font(String title, String buttonText) {
        JFrame frame = new JFrame(title);
        frame.setLocationRelativeTo(gui.getFrame());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setIconImage(BetterNotePad.getIcon());
        frame.setLayout(new GridBagLayout());

        //font names
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        JList<String> namesList = new JList<>(fonts);
        namesList.setSelectedIndex(0);      //default selected font: the first in the list

        JScrollPane namesScrollPane = new JScrollPane(namesList);
        namesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        //font styles
        String[] styles = {"PLAIN", "BOLD", "ITALIC"};
        JList<String> stylesList = new JList<>(styles);
        stylesList.setSelectedIndex(0);     //default selected size: PLAIN

        JScrollPane stylesScrollPane = new JScrollPane(stylesList);
        stylesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        //font sizes
        List<Integer> s = new ArrayList<>();
        for (int i = 8; i <= 96; i += 2)
            s.add(i);
        Integer[] sizes = s.toArray(new Integer[0]);
        JList<Integer> sizesList = new JList<>(sizes);
        sizesList.setSelectedIndex(7);      //default selected size: 22

        JScrollPane sizesScrollPane = new JScrollPane(sizesList);
        sizesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        //button
        JButton button = new JButton(buttonText);
        button.addActionListener(e -> {
            String fontName = namesList.getSelectedValue();
            int fontStyle = stylesList.getSelectedIndex();
            //using index because styles (PLAIN, BOLD, ITALIC)
            //and they are put in the same order as their value
            Integer fontSize = sizesList.getSelectedValue();

            //todo to be serialized
            font = new Font(fontName, fontStyle, fontSize);
            gui.getTextArea().setFont(font);
            System.out.println("Successfully set new Font: " + font);

            frame.dispose();
        });

        //adding to the frame
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
