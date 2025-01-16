package me.chriss99.old;

import java.util.ArrayList;

public class PrintLatestDBEntry {
    public static void main(String[] args) {
        DataBaseHandler dataBaseHandler = new DataBaseHandler(String.join(" ", args));
        ArrayList<DatedOSData> allDatedOSData = dataBaseHandler.getAllDatedOSData();
        float[] indexedOSPercentages = allDatedOSData.get(allDatedOSData.size()-1).indexedOSPercentages();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < indexedOSPercentages.length; i++)
            stringBuilder.append(OS.values()[i].name).append(": ").append(indexedOSPercentages[i]).append("%").append(System.lineSeparator());

        //stringBuilder cause easier compatibility with notify-send
        System.out.println(stringBuilder);
    }
}
