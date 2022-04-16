package com.xdavide9.configuration;

import java.io.*;

public class ConfigurationSerializer {

    private final File file;

    public ConfigurationSerializer() {
        file = new File(System.getenv("APPDATA") + "\\BetterNotePad\\serialized.ser");

        try {
            if (file.getParentFile().mkdir())
                System.out.println("Directory created");
            else
                System.out.println("Directory already exists");

            if (file.createNewFile())
                System.out.println("File created");
            else
                System.out.println("File already exists");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serialize(Configuration configuration) {
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(configuration);
            out.close();
            fileOut.close();
            System.out.println("Serialized");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration deserialize() {
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Configuration configuration = (Configuration) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Deserialized");
            return configuration;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Run for the first Time: applying default values");
        }
        return null;
    }

    // GETTERS
}
