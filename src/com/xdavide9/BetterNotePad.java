package com.xdavide9;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.xdavide9.configuration.Configuration;
import com.xdavide9.configuration.ConfigurationSerializer;
import com.xdavide9.gui.Gui;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class BetterNotePad {

    private static final String defaultFontFamily = "Segoe UI";
    private static final String initialFileName = "Untitled";
    private ConfigurationSerializer configurationSerializer;
    private static final String appName = "BetterNotePad";
    private Configuration configuration;
    private static String fileToOpen;
    private Gui gui;

    public static void main(String[] args) {
        fileToOpen = fileToOpen(args);
        customizeLaf();
        SwingUtilities.invokeLater(BetterNotePad::new);
    }

    private static String fileToOpen(String[] args) {
        try {
            return args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("No path provided to args");
        }

        return null;
    }

    private static void customizeLaf() {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf() {
                //removing the content of this method because it was responsible for producing an annoying beep sound
                @Override
                public void provideErrorFeedback(Component component) {}
            });
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        UIManager.put("ScrollBar.showButtons", true);
        UIManager.put("ScrollBar.width", 12);
        UIManager.put("defaultFont", new Font(BetterNotePad.defaultFontFamily, Font.PLAIN, 14));
    }

    private BetterNotePad() {
        createGui();
        open(fileToOpen);
        saveConfiguration();
    }

    // todo can be improved more, maybe with Optional
    private void createGui() {
        configurationSerializer = new ConfigurationSerializer();

        configuration = configurationSerializer.deserialize();

        if (configuration == null)
            configuration = new Configuration();

        if (configuration.isSet()) {
            gui = new Gui(BetterNotePad.initialFileName,
                    configuration.getX(),
                    configuration.getY(),
                    configuration.getWidth(),
                    configuration.getHeight(),
                    configuration.getFont(),
                    configuration.isLineWrap());
            return;
        }

        gui = new Gui(BetterNotePad.initialFileName, 0, 0, 970, 600,
                new Font(BetterNotePad.defaultFontFamily, Font.PLAIN, 22), true);
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

        gui.getFileService().setPath(fileToOpen);
    }

    private void saveConfiguration() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Rectangle rect = gui.getFrame().getBounds();
            configuration.setX(rect.x);
            configuration.setY(rect.y);
            configuration.setWidth(rect.width);
            configuration.setHeight(rect.height);
            configuration.setFont(gui.getTextArea().getFont());
            configuration.setLineWrap(gui.getTextArea().getLineWrap());
            configurationSerializer.serialize(configuration);
        }));
    }

    // GETTERS

    public static String getAppName() {
        return appName;
    }

    public static String getInitialFileName() {
        return initialFileName;
    }

}
