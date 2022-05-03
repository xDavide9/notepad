package com.xdavide9;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.xdavide9.configuration.Configuration;
import com.xdavide9.configuration.ConfigurationSerializer;
import com.xdavide9.gui.Gui;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;

@Slf4j
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
            log.info("No file to open provided to the arguments");
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
            log.error("Could not set up Look and Feel", e);
        }

        UIManager.put("ScrollBar.showButtons", true);
        UIManager.put("ScrollBar.width", 12);
        UIManager.put("defaultFont", new Font(BetterNotePad.defaultFontFamily, Font.PLAIN, 14));
        log.info("Successfully set up and customized Look and Feel");
    }

    private BetterNotePad() {
        createGui();
        open(fileToOpen);
        saveConfiguration();
    }

    //todo make configuration a record and improve with optionals
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
            log.info("Successfully created Gui with saved Configuration = {}", configuration);
            return;
        }

        gui = new Gui(BetterNotePad.initialFileName, 0, 0, 970, 600,
                new Font(BetterNotePad.defaultFontFamily, Font.PLAIN, 22), true);
        log.info("Successfully created Gui with default configuration");

    }

    private void open(String path) {
        if (path == null) {
            return;
        }

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

            gui.getFileService().setPath(fileToOpen);

            log.info("Successfully opened file provided from the arguments");
        } catch (Exception e) {
            log.error("Could not open file provided from the arguments", e);
        }
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
            log.info("Successfully saved configuration");
        }));
    }

    public static String getAppName() {
        return appName;
    }

    public static String getInitialFileName() {
        return initialFileName;
    }
}
