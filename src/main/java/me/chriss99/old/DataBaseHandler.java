package me.chriss99.old;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DataBaseHandler {
    private static final String LS = System.lineSeparator();

    private final String fileLocation;

    public DataBaseHandler(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public void addDatedOSData(DatedOSData datedOSData) {
        try {
            FileInputStream fileInputStream = new FileInputStream(fileLocation);
            byte[] data = fileInputStream.readAllBytes();
            String string = new String(data, StandardCharsets.UTF_8);

            string = string.concat(LS).concat(datedOSData.toString());

            FileOutputStream fileOutputStream = new FileOutputStream(fileLocation);
            fileOutputStream.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<DatedOSData> getAllDatedOSData() {
        removeEmptyLines();

        try {
            FileInputStream fileInputStream = new FileInputStream(fileLocation);
            byte[] data = fileInputStream.readAllBytes();
            String string = new String(data, StandardCharsets.UTF_8);

            ArrayList<DatedOSData> allDatedOSData = new ArrayList<>();

            if (string.isEmpty())
                return allDatedOSData;

            String[] datedOSDataStrings = string.split(LS);
            for (String datedOSDataString : datedOSDataStrings)
                    allDatedOSData.add(DatedOSData.fromString(datedOSDataString));

            return allDatedOSData;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Did not work.", e);
        }
    }

    public void removeEmptyLines() {
        try {
            FileInputStream fileInputStream = new FileInputStream(fileLocation);
            byte[] data = fileInputStream.readAllBytes();
            String string = new String(data, StandardCharsets.UTF_8);

            string = string.replace(LS, "").replace("]", "]" + LS).replaceAll("$" + LS, "");

            FileOutputStream fileOutputStream = new FileOutputStream(fileLocation);
            fileOutputStream.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
