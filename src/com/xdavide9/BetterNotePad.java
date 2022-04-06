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

    private final Gui gui;
    private DataHolder dataHolder;
    private final DataManager dataManager;

    private static String filePath;

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

        //setting up an Icon
        String path = "Icon.png";
        if (Files.exists(Paths.get(path))) {
            icon = new ImageIcon(path).getImage();
            System.out.println("Icon set correctly");
        } else {
            icon = null;
            System.err.println("Could not set Icon");
        }

        SwingUtilities.invokeLater(BetterNotePad::new);

        //getting the file path from args if it exists
        try {
            if (args[0] != null)
                filePath = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            System.err.println("No path provided to args");
        }
    }

    private BetterNotePad() {
        dataManager = new DataManager();

        dataHolder = dataManager.deserialize();   //tries to set the current dataHolder to the one that was deserialized
        if (dataHolder == null)
            dataHolder = new DataHolder();  //if it is null, it is a first time run and a new one is initialised

        if (dataHolder.isHolding()) {   //checks if dataHolder holds and if it does, create a gui with custom values
            gui = new Gui(BetterNotePad.initialFileName, dataHolder.getX(), dataHolder.getY(),
                    dataHolder.getWidth(), dataHolder.getHeight(), dataHolder.getFont(), dataHolder.isLineWrap());
        } else {
            //first time run configuration
            gui = new Gui(BetterNotePad.initialFileName, 0, 0, 970, 600,
                    new Font(BetterNotePad.defaultFontFamily, Font.PLAIN, 22), true);
        }

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

        //opening the file supplied by command line
        open(filePath);

        //setting the path of the file to the path in the code
        if (filePath != null)
            gui.getfManager().setPath(filePath);
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
