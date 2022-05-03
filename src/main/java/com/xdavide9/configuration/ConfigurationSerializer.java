package com.xdavide9.configuration;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class ConfigurationSerializer {

    private final File file;

    public ConfigurationSerializer() {
        file = new File(System.getenv("APPDATA") + "\\BetterNotePad\\serialized.ser");

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
            log.error("Could not create the file to hold configuration");
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
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Configuration configuration = (Configuration) in.readObject();
            in.close();
            fileIn.close();
            return configuration;
        } catch (IOException | ClassNotFoundException e) {
            log.error("Could not deserialize", e);
        }
        return null;
    }
}
