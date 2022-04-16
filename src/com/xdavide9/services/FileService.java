package com.xdavide9.services;

import com.xdavide9.BetterNotePad;
import com.xdavide9.gui.Gui;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FileService {

    private final Gui gui;
    private final String[] savingTabOptions;
    private final JFileChooser chooser = new JFileChooser();
    private String fileName = BetterNotePad.getInitialFileName();
    private String path = "";
    private BufferedReader bufferedReader;
    private FileReader fileReader;
    private int choice;

    public FileService(Gui gui, String[] savingTabOptions) {
        this.gui = gui;
        this.savingTabOptions = savingTabOptions;
    }

    // todo ask whether to save if necessary
    public void New() {
        path = "";
        fileName = BetterNotePad.getInitialFileName();
        gui.getTextArea().setText("");
        gui.getFrame().setTitle(fileName);
        System.out.println("Created New File");
    }

    public void open() {
        choice = chooser.showOpenDialog(gui.getFrame());

        if (choice == JFileChooser.APPROVE_OPTION) {
            path = chooser.getSelectedFile().getPath();
            changeTitle();

            try {
                readContent();
                System.out.println("File Opened");
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Could not Open File");
            }

            return;
        }

        System.err.println("Did not choose a File");

    }

    public void saveAs() {
        choice = chooser.showSaveDialog(gui.getFrame());

        if (choice == JFileChooser.APPROVE_OPTION) {
            path = chooser.getSelectedFile().getPath();
            changeTitle();

            try {
                writeContent();
                System.out.println("Saved as a New File");
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Could not Save As a New File");
            }
        }
    }

    public void save() {
        if (path.equals("")) {
            saveAs();
        }

        if (!(path.equals(""))) {
            try {
                writeContent();
                System.out.println("Saved Successfully");
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Could not Save");
            }
        }
    }

    public void exit() {
        /*
            4 possibilities:

            1) no path in memory and blank text                        -->  just exit
            2) no path in memory and some text written                 -->  ask whether to save
            3) path stored in memory and no differences to be saved    -->  just exit
            4) path stored in memory and differences found             -->  ask whether to save
        */

        if (path.equals("")) {
            if (gui.getTextArea().getText().equals("")) {
                System.err.println("Exiting without Saving");
                System.exit(0);
            }

            showSavingTab(savingTabOptions);
        }

        if (!(path.equals(""))) {
            if (areEqual()) {
                System.err.println("Exiting without Saving");
                System.exit(0);
            }

            showSavingTab(savingTabOptions);
        }
    }

    private boolean areEqual() {
        List<String> textAreaLines = gui.getTextArea().getText().lines().toList();
        List<String> fileLines = new ArrayList<>();

        try {
            fileReader = new FileReader(path);
            bufferedReader = new BufferedReader(fileReader);

            String line;
            while((line = bufferedReader.readLine()) != null)
                fileLines.add(line);

            fileReader.close();
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return textAreaLines.equals(fileLines);
    }

    private void showSavingTab(String[] options) {
        choice = JOptionPane.showOptionDialog(gui.getFrame(),
                "Do you want to save before exiting?",
                BetterNotePad.getAppName(),
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[0]);
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

    private String addExtension(String string) {
        if (!string.endsWith(".txt"))
            return string + ".txt";
        return string;
    }

    private void writeContent() throws IOException {
        FileWriter writer = new FileWriter(addExtension(path));
        writer.write(gui.getTextArea().getText());
        writer.close();
    }

    private void readContent() throws IOException {
        eraseTextArea();

        fileReader = new FileReader(path);
        bufferedReader = new BufferedReader(fileReader);

        String line;
        while((line = bufferedReader.readLine()) != null)
            gui.getTextArea().append(line + "\n");

        fileReader.close();
        bufferedReader.close();
    }

    private void changeTitle() {
        StringBuilder builder = new StringBuilder(path);
        fileName = addExtension(builder.substring(builder.lastIndexOf("\\") + 1));
        gui.getFrame().setTitle(fileName);
    }

    private void eraseTextArea() {
        gui.getTextArea().setText("");
    }

    // GETTERS SETTERS

    public void setPath(String path) {
        this.path = path;
    }
}
