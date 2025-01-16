package me.chriss99;

import me.chriss99.data.DatedPercentage;
import me.chriss99.data.DatedPercentageList;
import me.chriss99.data.DatedPercentageListFile;

import java.time.Month;
import java.util.*;
import java.util.function.Consumer;

public class UpdateData {
    public static final HashMap<String, ArrayList<DatedPercentage>> nameToDatedPercentagesMap = new HashMap<>();
    public static void main(String[] args) {
        updateData(System.out::println);
    }

    public static void updateData(Consumer<String> dataFeedBack) {
        DatedPercentageList windowsDatedPercentages = DatedPercentageListFile.forName("Windows").datedPercentageList();

        Date latestDate = windowsDatedPercentages.date(windowsDatedPercentages.size()-1);
        Date today = new Date();

        if (Granularity.DAILY.stringFromDate(new Date(latestDate.getTime() + 3600000*24)).equals(Granularity.DAILY.stringFromDate(today))) {
            dataFeedBack.accept("Data is up to Date.");
            return;
        } else dataFeedBack.accept("Updating Data...");

        int startYear = latestDate.getYear()+1900;
        int endYear = today.getYear()+1900;

        int startMonth = latestDate.getMonth();
        int endMonth = today.getMonth();

        int month = startMonth;
        for (int year = startYear; year <= endYear; year++) {
            for (; month <= endMonth; month++) {
                Date startDate = new Date(year-1970, month, 0);
                Date endDate = new Date(year-1970, month, Month.of(month+1).length((year-2000)%4 == 0));

                addNameToDatedPercentagesMap(StatCounterRequest.getListOfDatedOSPercentages(Granularity.DAILY, startDate, endDate));
            }
            month = 0;
        }


        ArrayList<DatedPercentageListFile> listOfDatedOSPercentages = new ArrayList<>();
        for (Map.Entry<String, ArrayList<DatedPercentage>> nameToDatedPercentages : nameToDatedPercentagesMap.entrySet()) {
            DatedPercentageListFile datedPercentageListFile = DatedPercentageListFile.forName(nameToDatedPercentages.getKey());
            datedPercentageListFile.datedPercentageList().datedPercentageList().addAll(nameToDatedPercentages.getValue());
            listOfDatedOSPercentages.add(datedPercentageListFile);
        }

        for (DatedPercentageListFile datedPercentageListFile : listOfDatedOSPercentages) {
            datedPercentageListFile.datedPercentageList().removeDuplicates();
            datedPercentageListFile.writeToFile();
        }
    }

    public static void addNameToDatedPercentagesMap(HashMap<String, ArrayList<DatedPercentage>> nameToDatedPercentagesMapToAdd) {
        for (Map.Entry<String, ArrayList<DatedPercentage>> nameToDatedPercentages : nameToDatedPercentagesMapToAdd.entrySet()) {
            ArrayList<DatedPercentage> datedPercentages = nameToDatedPercentagesMap.get(nameToDatedPercentages.getKey());
            if (datedPercentages == null) {
                nameToDatedPercentagesMap.put(nameToDatedPercentages.getKey(), nameToDatedPercentages.getValue());
                continue;
            }

            datedPercentages.addAll(nameToDatedPercentages.getValue());
        }
    }
}
