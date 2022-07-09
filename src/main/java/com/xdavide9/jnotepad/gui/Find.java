package com.xdavide9.jnotepad.gui;

import com.xdavide9.jnotepad.JNotepad;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;

/** A replication of Notepad's Find window */
public class Find {

    private JDialog frame;

    private JTextField searchTextField;
    private JButton findNext;
    private JRadioButton upRadioButton;
    private JCheckBox matchCase, wrap;

    private final Gui gui;
    private final JTextArea textArea;

    /** stores the caret's index at the start of JNotepad's selected text */
    private int caretPositionMark = 0;

    /** stores the caret's index at the end of JNotepad's selected text */
    private int caretPositionDot = 0;

    public Find(Gui gui, JTextArea textArea, Image icon) {
        this.gui = gui;
        this.textArea = textArea;
        textArea.addCaretListener(e -> {
            Find.this.caretPositionMark = e.getMark();
            Find.this.caretPositionDot = e.getDot();
        });

        createFrame(icon);
    }

    private void createFrame(Image icon) {
        frame = new JDialog(gui.getFrame(), "Find", false);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setIconImage(icon);
        frame.setResizable(false);
        frame.setBounds(100, 100, 392, 181);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        frame.setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel findWhat = new JLabel("Find what:");
        findWhat.setFont(new Font(JNotepad.DEFAULT_FONT_FAMILY, Font.PLAIN, 12));
        findWhat.setBounds(9, 16, 65, 14);
        contentPane.add(findWhat);

        findNext = new JButton("Find Next");
        findNext.setEnabled(false);
        findNext.setFont(new Font(JNotepad.DEFAULT_FONT_FAMILY, Font.PLAIN, 12));
        findNext.setBounds(262, 7, 100, 28);
        findNext.addActionListener(new FindNext());
        contentPane.add(findNext);

        JButton cancel = new JButton("Cancel");
        cancel.setFont(new Font(JNotepad.DEFAULT_FONT_FAMILY, Font.PLAIN, 12));
        cancel.setBounds(262, 40, 100, 28);
        cancel.addActionListener(e -> Find.this.frame.setVisible(false));
        contentPane.add(cancel);

        searchTextField = new JTextField();
        searchTextField.setBounds(81, 9, 166, 29);
        searchTextField.setFont(new Font(JNotepad.DEFAULT_FONT_FAMILY, Font.PLAIN, 12));
        contentPane.add(searchTextField);
        searchTextField.setColumns(10);
        searchTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                searchTextField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(64, 104, 149), 3, true),
                        BorderFactory.createEmptyBorder(1, 6, 2, 4)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                searchTextField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(81, 83, 85), 2, true),
                        BorderFactory.createEmptyBorder(1, 6, 2, 4)));
            }
        });
        // disable the 'Find Next' button if the text field is empty
        searchTextField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) { updateButton(); }

            @Override
            public void removeUpdate(DocumentEvent e) { updateButton(); }

            @Override
            public void insertUpdate(DocumentEvent e) { updateButton(); }

            public void updateButton() {
                findNext.setEnabled(searchTextField.getText().length() != 0);
            }
        });

        // click the 'Find Next' button when enter key is pressed in the JTextField
        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    findNext.doClick();
            }
        });

        JPanel directionPanel = new JPanel();
        TitledBorder directionBorder = BorderFactory.createTitledBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                "Direction");
        directionBorder.setTitleFont(new Font(JNotepad.DEFAULT_FONT_FAMILY, Font.PLAIN, 12));
        directionPanel.setBorder(directionBorder);
        directionPanel.setBounds(120, 49, 129, 50);
        contentPane.add(directionPanel);
        directionPanel.setLayout(null);

        ButtonGroup bg = new ButtonGroup();

        upRadioButton = new JRadioButton("Up");
        upRadioButton.setFont(new Font(JNotepad.DEFAULT_FONT_FAMILY, Font.PLAIN, 11));
        upRadioButton.setBounds(7, 19, 47, 23);
        directionPanel.add(upRadioButton);
        bg.add(upRadioButton);

        JRadioButton downRadioButton = new JRadioButton("Down");
        downRadioButton.setFont(new Font(JNotepad.DEFAULT_FONT_FAMILY, Font.PLAIN, 11));
        downRadioButton.setBounds(58, 19, 72, 23);
        downRadioButton.setSelected(true);
        directionPanel.add(downRadioButton);
        bg.add(downRadioButton);


        matchCase = new JCheckBox("Match case");
        matchCase.setFont(new Font(JNotepad.DEFAULT_FONT_FAMILY, Font.PLAIN, 12));
        matchCase.setBounds(6, 79, 119, 23);
        contentPane.add(matchCase);

        wrap = new JCheckBox("Wrap around");
        wrap.setFont(new Font(JNotepad.DEFAULT_FONT_FAMILY, Font.PLAIN, 12));
        wrap.setBounds(6, 104, 119, 23);
        contentPane.add(wrap);
    }

    /** When Control+F is pressed, display the Find Frame */
    public void openFindWindow() {
        if(!frame.isVisible()) {
            frame.setLocationRelativeTo(gui.getFrame());
            frame.setVisible(true);
        }

        SwingUtilities.invokeLater(() -> searchTextField.requestFocus());
    }

    /** When the 'Find Next' button is clicked, the text specified inside searchTextField
     *  is searched for & selected inside Gui.textArea
     *
     *  The search starts from the caret's current position inside Gui.textArea, taking into account
     *  all settings inside the Find.frame JDialog window
     */
    private class FindNext implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String notepadText = matchCase.isSelected() ? textArea.getText() : textArea.getText().toLowerCase(Locale.ROOT);
            String findText = matchCase.isSelected() ? searchTextField.getText() : searchTextField.getText().toLowerCase(Locale.ROOT);

            // the index inside notepadText where the next occurrence of findText is found; will be -1 if not found
            int nextIndex;

            if(wrap.isSelected()) {
                if(upRadioButton.isSelected()) {
                    String aboveText = notepadText.substring(0, caretPositionMark);
                    nextIndex = aboveText.contains(findText) ? aboveText.lastIndexOf(findText) : notepadText.lastIndexOf(findText);
                } else { // down radio button is selected
                    String belowText = notepadText.substring(caretPositionDot);
                    nextIndex = belowText.contains(findText) ? belowText.indexOf(findText) + caretPositionDot : notepadText.indexOf(findText);
                }
            } else { // wrap isn't selected
                if(upRadioButton.isSelected()) {
                    nextIndex = notepadText.substring(0, caretPositionMark).lastIndexOf(findText);
                } else { // down radio button is selected
                    String belowText = notepadText.substring(caretPositionDot);
                    nextIndex = belowText.contains(findText) ? belowText.indexOf(findText) + caretPositionDot : -1;
                }
            }

            if(nextIndex == -1) {
                JOptionPane.showMessageDialog(gui.getFrame(), "Cannot find \"" + searchTextField.getText() + "\"",
                        "JNotepad", JOptionPane.INFORMATION_MESSAGE);
            } else {
                textArea.setSelectionStart(nextIndex);
                textArea.setSelectionEnd(nextIndex + findText.length());
            }
        }
    }

}
