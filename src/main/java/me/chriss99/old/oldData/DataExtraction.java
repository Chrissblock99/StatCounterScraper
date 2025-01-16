package me.chriss99.old.oldData;

import me.chriss99.old.ChartStuff;
import me.chriss99.old.DatedOSData;
import me.chriss99.old.OS;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataExtraction {private static final Pattern findPattern = Pattern.compile("toolText='[a-zA-Z ]*- [0-9.]*% - [a-zA-Z 0-9]*'");
    private static final Pattern namePattern = Pattern.compile("='[a-zA-Z ]*-");
    private static final Pattern percentagePattern = Pattern.compile(" - [0-9.]*% - ");
    private static final Pattern datePattern = Pattern.compile(" - [a-zA-Z 0-9]*'");
    private static final Pattern monthPattern = Pattern.compile("[a-zA-Z]*");
    private static final Pattern yearPattern = Pattern.compile("[0-9]*$");
    public static final String fileLocation = "/home/chriss99/Documents/other projects/get latest statcounter linux percentage/allStatCounterData.txt";

    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(fileLocation);
        byte[] data = fileInputStream.readAllBytes();
        String string = new String(data, StandardCharsets.UTF_8);

        Matcher matcher = findPattern.matcher(string);
        ArrayList<String> regexDatedOSPercentages = new ArrayList<>();
        while(matcher.find())
            regexDatedOSPercentages.add(matcher.group());

        EnumMap<OS, ArrayList<DatedOSPercentage>> datedOSPercentages = new EnumMap<>(OS.class);

        for (OS os : OS.values())
            datedOSPercentages.put(os, new ArrayList<>());

        for (String regexDatedOSPercentage : regexDatedOSPercentages) {
            Matcher nameMatcher = namePattern.matcher(regexDatedOSPercentage);
            nameMatcher.find();
            String name = nameMatcher.group().replace("='", "").replace(" -", "");
            OS os = ("iOS".equals(name)) ? OS.FREEBSD : OS.getOSFromName(name);

            Matcher percentageMatcher = percentagePattern.matcher(regexDatedOSPercentage);
            percentageMatcher.find();
            float percentage = Float.parseFloat(percentageMatcher.group().replace(" - ", "").replace("%", ""));

            Matcher dateMatcher = datePattern.matcher(regexDatedOSPercentage);
            dateMatcher.find();
            String dateString = dateMatcher.group().replace(" - ", "").replace("'", "");

            Matcher monthMatcher = monthPattern.matcher(dateString);
            monthMatcher.find();
            Month month = getMonth(monthMatcher.group());

            Matcher yearMatcher = yearPattern.matcher(dateString);
            yearMatcher.find();
            int year = Integer.parseInt(yearMatcher.group());

            Date date = Date.valueOf(LocalDate.of(year, month, month.length(false)));

            datedOSPercentages.get(os).add(new DatedOSPercentage(date, os, percentage));
        }


        ArrayList<DatedOSData> allOldDatedOSData = new ArrayList<>();

        for (int i = 0; i < 181; i++) {
            float[] indexedPercentages= new float[OS.values().length];
            for (OS os : OS.values())
                indexedPercentages[os.index] = datedOSPercentages.get(os).get(i).percentage();

            //add FreeBSD(iOS) percentage to other and set FreeBSD to 0
            indexedPercentages[OS.OTHER.index] += indexedPercentages[OS.FREEBSD.index];
            indexedPercentages[OS.FREEBSD.index] = 0;

            indexedPercentages[OS.WINDOWS.index] += indexedPercentages[OS.UNKNOWN.index];
            indexedPercentages[OS.UNKNOWN.index] = 0;


            for (OS os : OS.values())
                if (!OS.FREEBSD.equals(os))
                    indexedPercentages[OS.FREEBSD.index] += indexedPercentages[os.index];
            indexedPercentages[1] -= 100;
            System.out.println(indexedPercentages[1]);

            for (OS os : OS.values())
                indexedPercentages[os.index] = (float) Math.log10(indexedPercentages[os.index]+1);

            allOldDatedOSData.add(new DatedOSData(datedOSPercentages.get(OS.LINUX).get(i).date(), indexedPercentages));
        }

        ChartStuff.chartDatedOSDataList(allOldDatedOSData);
    }

    private static Month getMonth(String month) {
        return switch (month) {
            case "Jan" -> Month.JANUARY;
            case "Feb" -> Month.FEBRUARY;
            case "Mar" -> Month.MARCH;
            case "Apr" -> Month.APRIL;
            case "May" -> Month.MAY;
            case "June" -> Month.JUNE;
            case "July" -> Month.JULY;
            case "Aug" -> Month.AUGUST;
            case "Sept" -> Month.SEPTEMBER;
            case "Oct" -> Month.OCTOBER;
            case "Nov" -> Month.NOVEMBER;
            case "Dec" -> Month.DECEMBER;
            default -> throw new IllegalStateException("Unexpected value: " + month);
        };
    }
}
