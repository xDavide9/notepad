package com.xdavide9.jnotepad.configuration;

import com.xdavide9.jnotepad.JNotepad;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class ConfigurationSerializer {

    private final File file;

    public ConfigurationSerializer() {
        file = new File(getAppDataDirectory() + File.separator + "BetterNotePad" + File.separator + "serialized-0.1.ser");

        try {
            if (file.getParentFile().mkdir())
                log.info("Directory to hold configuration created");
            else
                log.info("Directory to hold configuration already exists in memory");

            if (file.createNewFile())
                log.info("File to hold configuration created");
            else
                log.info("File to hold configuration already exists in memory");
        } catch (IOException e) {
            log.error("Could not create the file to hold configuration", e);
        }
    }

    public void serialize(Configuration configuration) {
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(configuration);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            log.error("Could not serialize", e);
        }
    }

    public Configuration deserialize() {
        if(file.length() == 0) {
            log.info("Config file is empty, skipping deserialization");
            return null;
        }

        try(FileInputStream fileIn = new FileInputStream(file); ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (Configuration) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("Could not deserialize", e);
        }
        return null;
    }

    /** Gets the path to the app data directory (cross-platform)
     *
     * @return path to the app data directory; if the directory is not found, the local directory is returned
     */
    public String getAppDataDirectory() {
        String workingDirectory = "";
        switch(JNotepad.os) {
            case WINDOWS -> workingDirectory = System.getenv("AppData");
            case MAC -> workingDirectory = System.getProperty("user.home") + "/Library/Application Support";
            case LINUX -> workingDirectory = System.getProperty("user.home");
        }
        //in case the directory doesn't exist, return the local directory
        if(new File(workingDirectory).exists()) {
            return workingDirectory;
        }
        else {
            String localDirectory = new File("").getAbsoluteFile().getAbsolutePath();
            log.info("App Data directory was not found, using local directory '" + localDirectory + "' instead");
            return localDirectory;
        }
    }
}