package com.xdavide9.jnotepad.services;

import com.xdavide9.jnotepad.JNotepad;
import com.xdavide9.jnotepad.gui.Gui;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FileService {

    private final Gui gui;
    private final String[] savingTabOptions;
    private final JFileChooser chooser = new JFileChooser();
    private String fileName = JNotepad.INITIAL_FILE_NAME;
    private BufferedReader bufferedReader;
    private FileReader fileReader;
    private int choice;

    @Setter
    private String path = "";

    public FileService(Gui gui, String[] savingTabOptions) {
        this.gui = gui;
        this.savingTabOptions = savingTabOptions;
    }

    public void New() {
        path = "";
        fileName = JNotepad.INITIAL_FILE_NAME;
        gui.getTextArea().setText("");
        gui.getFrame().setTitle(fileName);
        log.info("Successfully created a new File");
    }

    public void open() {
        choice = chooser.showOpenDialog(gui.getFrame());

        if (choice == JFileChooser.APPROVE_OPTION) {
            path = chooser.getSelectedFile().getPath();
            changeTitle();

            try {
                readContent();
                log.info("Successfully opened desired file");
            } catch (IOException e) {
                log.error("Could not open desired file", e);
            }

            return;
        }

        log.error("No file to open was chosen");
    }

    public void saveAs() {
        choice = chooser.showSaveDialog(gui.getFrame());

        if (choice == JFileChooser.APPROVE_OPTION) {
            path = chooser.getSelectedFile().getPath();
            changeTitle();

            try {
                writeContent();
                log.info("Successfully saved as a new file");
            } catch (IOException e) {
                log.error("Could not save as a new file", e);
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
                log.info("Successfully saved");
            } catch (IOException e) {
                log.error("Could not Save", e);
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
                log.info("Exiting without saving");
                System.exit(0);
            }

            showSavingTab(savingTabOptions);
        }

        if (!(path.equals(""))) {
            if (areEqual()) {
                log.info("Exiting without Saving");
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
                JNotepad.APP_NAME,
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
                log.info("Exiting without Saving");
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
}
