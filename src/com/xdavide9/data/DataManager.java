package com.xdavide9.data;

import java.io.*;

public class DataManager {

    private final File file;
    private DataHolder holder;

    public DataManager() {
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

    public void serialize(DataHolder dataHolder) {
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(dataHolder);
            out.close();
            fileOut.close();
            System.out.println("Serialized");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataHolder deserialize() {
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            holder = (DataHolder) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Deserialized");
            return holder;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Run for the first Time: applying default values");
        }
        return null;
    }

    // GETTERS
}
