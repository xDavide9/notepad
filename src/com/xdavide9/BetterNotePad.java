package com.xdavide9;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.xdavide9.data.DataHolder;
import com.xdavide9.data.DataManager;
import com.xdavide9.gui.Gui;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BetterNotePad {

    private static final String appName = "BetterNotePad";
    private static final String initialFileName = "Untitled";
    private static final String defaultFontFamily = "Segoe UI";
    private static Image icon;

    private Gui gui;
    private DataHolder dataHolder;
    private DataManager dataManager;

    private static String fileToOpen;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf() {
                //removing the content of this method because it was responsible for producing an annoying beep sound
                @Override
                public void provideErrorFeedback(Component component) {}
            });
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        customizeLaf();
        icon = setUpIcon("Icon.png");
        SwingUtilities.invokeLater(BetterNotePad::new);

        try {
            if (args[0] != null)
                fileToOpen = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            System.err.println("No path provided to args");
        }
    }

    private BetterNotePad() {
        createGui();
        saveConfiguration();
        open(fileToOpen);
    }

    private void saveConfiguration() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Rectangle rect = gui.getFrame().getBounds();
            dataHolder.setX(rect.x);
            dataHolder.setY(rect.y);
            dataHolder.setWidth(rect.width);
            dataHolder.setHeight(rect.height);
            dataHolder.setFont(gui.getTextArea().getFont());
            dataHolder.setLineWrap(gui.getTextArea().getLineWrap());
            dataManager.serialize(dataHolder);
        }));
    }

    private void createGui() {
        dataManager = new DataManager();

        dataHolder = dataManager.deserialize();
        if (dataHolder == null)
            dataHolder = new DataHolder();

        if (dataHolder.isHolding()) {
            gui = new Gui(BetterNotePad.initialFileName, dataHolder.getX(), dataHolder.getY(),
                    dataHolder.getWidth(), dataHolder.getHeight(), dataHolder.getFont(), dataHolder.isLineWrap());
            return;
        }

        gui = new Gui(BetterNotePad.initialFileName, 0, 0, 970, 600,
                new Font(BetterNotePad.defaultFontFamily, Font.PLAIN, 22), true);
    }

    private static Image setUpIcon(String path) {
        if (Files.exists(Paths.get(path)))
            return new ImageIcon(path).getImage();
        return null;
    }

    private void open(String path) {
        if (path == null)
            return;

        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while((line = bufferedReader.readLine()) != null)
                gui.getTextArea().append(line + "\n");

            fileReader.close();
            bufferedReader.close();

            StringBuilder builder = new StringBuilder(path);
            String fileName = builder.substring(builder.lastIndexOf("\\") + 1);
            gui.getFrame().setTitle(fileName);

            System.out.println("File Opened");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not Open File");
        }

        gui.getfManager().setPath(fileToOpen);
    }

    private static void customizeLaf() {
        UIManager.put("ScrollBar.showButtons", true);
        UIManager.put("ScrollBar.width", 12);
        UIManager.put("defaultFont", new Font(BetterNotePad.defaultFontFamily, Font.PLAIN, 14));
    }

    // GETTERS

    public static String getAppName() {
        return appName;
    }

    public static String getInitialFileName() {
        return initialFileName;
    }

    public static Image getIcon() {
        return icon;
    }
}
