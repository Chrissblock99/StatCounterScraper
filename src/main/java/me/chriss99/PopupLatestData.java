package me.chriss99;

import me.chriss99.data.DatedPercentageList;
import me.chriss99.data.DatedPercentageListFile;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

public class PopupLatestData {
    public static final String[] names = DataProcessor.names;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMMM yyyy");

    public static void main(String[] args) {
        UpdateData.updateData((s -> runCommand("notify-send --app-name='Desktop OS usage worldwide' \"Data state:\" \"" + s + "\"")));

        DatedPercentageList windowsDatedPercentages = DatedPercentageListFile.forName("Windows").datedPercentageList();
        Date latestDate = windowsDatedPercentages.date(windowsDatedPercentages.size()-1);

        String month = Month.values()[latestDate.getMonth()].getDisplayName(TextStyle.FULL, Locale.UK);
        String weekDay = DayOfWeek.values()[(latestDate.getDay()-1+7)%7].getDisplayName(TextStyle.FULL, Locale.UK);
        runCommand("notify-send --app-name='Desktop OS usage worldwide' --urgency=critical \"" +
                latestDate.getDate() + " " + month + " " + (latestDate.getYear()+1900) + " (" + weekDay +
                "):\" \"" + getDataSummary() + "\"");
    }

    public static String getDataSummary() {
        DatedPercentageList[] indexedData = DataProcessor.getIndexedData();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < names.length; i++)
            stringBuilder.append(names[i]).append(": ").append(indexedData[i].getLatestDataAndChange()).append(System.lineSeparator());

        return stringBuilder.toString();
    }

    public static void runCommand(String command) {
        try {
            new ProcessBuilder("sh", "-c", command).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
