package com.xdavide9.functionality;

import com.xdavide9.BetterNotePad;
import com.xdavide9.gui.Gui;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileFunctionsManager {

    //class to provide functionality to items under File menu

    private final Gui gui;
    private String path = "";
    private String fileName = BetterNotePad.getInitialFileName();

    private final String[] savingTabOptions;

    private boolean areEqual;    //determines if the textAreas is the same as the path

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
        JFileChooser chooser = new JFileChooser();
        int choice = chooser.showOpenDialog(gui.getFrame());

        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            StringBuilder builder = new StringBuilder(file.toString());
            fileName = builder.substring(builder.lastIndexOf("\\") + 1);
            path = file.getPath();
            //areEqual can be assumed to be false so that code must check equality in Exit()
            areEqual = false;
        } else {
            System.err.println("Did not choose a File");
            return;
        }

        gui.getTextArea().setText("");

        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

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
        JFileChooser chooser = new JFileChooser();
        int choice = chooser.showSaveDialog(gui.getFrame());

        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            path = file.toString();

            try {
                FileWriter writer;
                if (!path.endsWith(".txt"))
                    writer = new FileWriter(path + ".txt");
                else
                    writer = new FileWriter(path);
                writer.write(gui.getTextArea().getText());
                writer.close();

                StringBuilder builder = new StringBuilder(path);
                fileName = builder.substring(builder.lastIndexOf("\\") + 1);
                gui.getFrame().setTitle(fileName + ".txt");
                System.out.println("Saved as a New File");
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Could not Save As a New File");
            }

            //now that it has been saved textarea and path are identical
            areEqual = true;
        }
    }

    public void save() {
        if (path.equals("")) {
            saveAs();
        } else {
            try {
                FileWriter writer;
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
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

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
        int result = JOptionPane.showOptionDialog(gui.getFrame(), "Do you want to save before exiting?",
                BetterNotePad.getAppName(), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        switch (result) {
            case JOptionPane.YES_OPTION -> {
                save();
                System.exit(0);
                //TODO maybe you can actually just put System.exit(0) here without using any other
                //thread but why did you use it in the first place if it was not necessary wtf
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
