package me.chriss99.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public record DatedPercentageListFile(String name, DatedPercentageList datedPercentageList) {
    private static final String LS = System.lineSeparator();
    private static String dbLocation = "/home/chriss99/Documents/other projects/get latest statcounter linux percentage/dataBase/";

    static {
        if ("gartenzwerg".equals(System.getProperty("user.name")))
            dbLocation = "/run/media/gartenzwerg/SCHOOLSTICK/percentages/dataBase/";
    }

    public static DatedPercentageListFile forName(String name) {
        String fileLocation = dbLocation + name + ".txt";
        removeEmptyLines(fileLocation);

        try {
            FileInputStream fileInputStream = new FileInputStream(fileLocation);
            byte[] data = fileInputStream.readAllBytes();
            String string = new String(data, StandardCharsets.UTF_8);

            DatedPercentageList datedPercentageList = new DatedPercentageList();

            if (string.isEmpty())
                return new DatedPercentageListFile(name, datedPercentageList);

            String[] datedOSDataStrings = string.split(LS);
            for (String datedOSDataString : datedOSDataStrings)
                datedPercentageList.add(DatedPercentage.fromString(datedOSDataString));

            return new DatedPercentageListFile(name, datedPercentageList);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Did not work.", e);
        }
    }

    public void writeToFile() {
        String fileLocation = dbLocation + name + ".txt";
        String string = datedPercentageList.toString();

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileLocation);
            fileOutputStream.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Did not work.", e);
        }
    }

    public static void removeEmptyLines(String fileLocation) {
        try {
            new File(fileLocation).createNewFile();
            FileInputStream fileInputStream = new FileInputStream(fileLocation);
            byte[] data = fileInputStream.readAllBytes();
            String string = new String(data, StandardCharsets.UTF_8);

            string = string.replaceAll(LS + "+", LS).replaceAll("^" + LS, "").replaceAll("$" + LS, "");

            FileOutputStream fileOutputStream = new FileOutputStream(fileLocation);
            fileOutputStream.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
