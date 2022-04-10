package com.xdavide9.functionality;

import com.xdavide9.BetterNotePad;
import com.xdavide9.gui.Gui;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FileFunctionsManager {

    private final Gui gui;
    private final String[] savingTabOptions;
    private String path = "";
    private String fileName = BetterNotePad.getInitialFileName();

    // comparison between the current text in the textArea and the one in the actual file in the users computer
    private boolean areEqual;

    private JFileChooser chooser;
    private int choice;
    private StringBuilder builder;
    private FileReader fileReader;
    private BufferedReader bufferedReader;
    private FileWriter writer;

    public FileFunctionsManager(Gui gui, String[] savingTabOptions) {
        this.gui = gui;
        this.savingTabOptions = savingTabOptions;
    }

    public void New() {
        path = "";
        fileName = BetterNotePad.getInitialFileName();
        gui.getTextArea().setText("");
        gui.getFrame().setTitle(fileName);
        System.out.println("Created New File");
    }

    public void open() {
        chooser = new JFileChooser();
        choice = chooser.showOpenDialog(gui.getFrame());

        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            builder = new StringBuilder(file.toString());
            fileName = builder.substring(builder.lastIndexOf("\\") + 1);
            path = file.getPath();
            // areEqual has to be set false to make sure equality between the text area's text and the file on the
            // user's computer is checked
            areEqual = false;
        } else {
            System.err.println("Did not choose a File");
            return;
        }

        gui.getTextArea().setText("");

        try {
            fileReader = new FileReader(path);
            bufferedReader = new BufferedReader(fileReader);

            String line;
            while((line = bufferedReader.readLine()) != null)
                gui.getTextArea().append(line + "\n");

            fileReader.close();
            bufferedReader.close();

            gui.getFrame().setTitle(fileName);
            System.out.println("File Opened");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not Open File");
        }
    }

    public void saveAs() {
        chooser = new JFileChooser();
        choice = chooser.showSaveDialog(gui.getFrame());

        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            path = file.toString();

            try {
                if (!path.endsWith(".txt"))
                    writer = new FileWriter(path + ".txt");
                else
                    writer = new FileWriter(path);
                writer.write(gui.getTextArea().getText());
                writer.close();

                builder = new StringBuilder(path);
                fileName = builder.substring(builder.lastIndexOf("\\") + 1);
                if (!fileName.endsWith(".txt"))
                    fileName = fileName + ".txt";
                gui.getFrame().setTitle(fileName);
                System.out.println("Saved as a New File");
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Could not Save As a New File");
            }

            //now that it has been saved textArea and path are identical
            areEqual = true;
        }
    }

    public void save() {
        if (path.equals("")) {
            saveAs();
        } else {
            try {
                if (!path.endsWith(".txt")) {
                    writer = new FileWriter(path + ".txt");
                } else {
                    writer = new FileWriter(path);
                }
                writer.write(gui.getTextArea().getText());
                writer.close();
                System.out.println("Saved Successfully");

                //now that it has been saved textarea and path are identical
                areEqual = true;

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Could not Save");
            }
        }
    }

    public void exit() {
        if (path.equals("")) {
            if (gui.getTextArea().getText().equals("")) {
                System.err.println("Exiting without Saving");
                System.exit(0);
            }
            else {
                showSavingTab(savingTabOptions);
            }
        } else {
            //if files are equal can exit without comparing
            if (areEqual) {
                System.err.println("Exiting without Saving");
                System.exit(0);
            }

            //else check equality and decide what to do depending on that
            checkEquality();
            if (areEqual) {
                System.err.println("Exiting without Saving");
                System.exit(0);
            } else
                showSavingTab(savingTabOptions);
        }
    }

    private void checkEquality() {
        List<String> textAreaLines = gui.getTextArea().getText().lines().toList();
        List<String> pathLines = new ArrayList<>();

        try {
            fileReader = new FileReader(path);
            bufferedReader = new BufferedReader(fileReader);

            String line;
            while((line = bufferedReader.readLine()) != null)
                pathLines.add(line);

            fileReader.close();
            bufferedReader.close();
            System.out.println("Checked Equality");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not check Equality");
        }

        areEqual = textAreaLines.equals(pathLines);
    }

    private void showSavingTab(String[] options) {
        choice = JOptionPane.showOptionDialog(gui.getFrame(), "Do you want to save before exiting?",
                BetterNotePad.getAppName(), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        switch (choice) {
            case JOptionPane.YES_OPTION -> {
                save();
                System.exit(0);
            }
            case JOptionPane.NO_OPTION ->  {
                System.err.println("Exiting without Saving");
                System.exit(0);
            }
        }
    }

    // GETTERS SETTERS

    public void setPath(String path) {
        this.path = path;
    }
}
