package me.chriss99;

import me.chriss99.data.DatedPercentage;
import me.chriss99.data.DatedPercentageList;
import me.chriss99.data.DatedPercentageListFile;

import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Testing {
    public static final HashMap<String, ArrayList<DatedPercentage>> nameToDatedPercentagesMap = new HashMap<>();

    public static void main(String[] args) {
        Date debugStart = new Date(2014-1900, 1-1, 1);
        Date debugEnd = new Date(2014-1900, 1-1, 5);

        Date one = new Date(2014-1900, 1-1, 2);
        Date two = new Date(2014-1900, 1-1, 3);
        Date three = new Date(2014-1900, 1-1, 4);


        System.out.println(debugStart.getTime());
        System.out.println(one.getTime());
        System.out.println(two.getTime());
        System.out.println(three.getTime());
        System.out.println(debugEnd.getTime());

        System.out.println(new Date(1604358000000L));
        System.out.println(new Date(2020-1900, 11-1, 2).getTime());

        /*int startYear = debugStart.getYear()+1900;
        int endYear = debugEnd.getYear()+1900;

        int startMonth = debugStart.getMonth();
        int endMonth = debugEnd.getMonth();

        int month = startMonth;
        for (int year = startYear; year <= endYear; year++) {
            for (; month <= endMonth; month++) {
                Date startDate = new Date(year-1970, month, 0);
                Date endDate = new Date(year-1970, month, Month.of(month+1).length((year-2000)%4 == 0));

                addNameToDatedPercentagesMap(StatCounterRequest.getListOfDatedOSPercentages(Granularity.DAILY, startDate, endDate));
            }
            month = 0;
        }


        for (Map.Entry<String, ArrayList<DatedPercentage>> nameToDatedPercentageListEntry : nameToDatedPercentagesMap.entrySet()) {
            System.out.println();
            System.out.println(nameToDatedPercentageListEntry.getKey());
            for (DatedPercentage datedPercentage : nameToDatedPercentageListEntry.getValue()) {
                System.out.println(datedPercentage.date() + ": " + datedPercentage.percentage());
            }
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
        }*/
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
