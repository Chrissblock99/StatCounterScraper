package me.chriss99;

import me.chriss99.data.DatedPercentageList;
import me.chriss99.data.DatedPercentageListFile;

public class DataProcessor {
    public static final String[] names = {"Windows", "OS X", "Linux", "Chrome OS", "Unknown"};


    public static DatedPercentageList[] getIndexedData() {
        DatedPercentageList[] indexedDatedPercentageLists = new DatedPercentageList[names.length];
        for (int i = 0; i < names.length; i++) {
            DatedPercentageList datedPercentageList = DatedPercentageListFile.forName(names[i]).datedPercentageList();
            datedPercentageList.removeDuplicates();
            indexedDatedPercentageLists[i] = datedPercentageList;
        }

        //indexedDatedPercentageLists[0].addNumeric(DatedPercentageListFile.forName("Unknown").datedPercentageList());

        return  indexedDatedPercentageLists;
    }

    public static DatedPercentageList[][] getIndexedDataWorkdayWeekend() {
        DatedPercentageList[] indexedDatedPercentageLists = getIndexedData();

        DatedPercentageList[] indexedWorkday = new DatedPercentageList[names.length];
        DatedPercentageList[] indexedWeekend = new DatedPercentageList[names.length];

        for (int i = 0; i < names.length; i++) {
            DatedPercentageList[] workdayWeekend = indexedDatedPercentageLists[i].getWorkdayWeekend();
            indexedWorkday[i] = workdayWeekend[0];
            indexedWeekend[i] = workdayWeekend[1];
        }

        return new DatedPercentageList[][]{indexedDatedPercentageLists, indexedWorkday, indexedWeekend};
    }

        /*DatedPercentageList[] indexedMin = new DatedPercentageList[names.length];
        DatedPercentageList[] indexedMax = new DatedPercentageList[names.length];

        for (int i = 0; i < names.length; i++) {
            indexedMax[i] = new DatedPercentageList();
            indexedMin[i] = new DatedPercentageList();

            DatedPercentageList datedPercentageList = indexedDatedPercentageLists[i];

            for (int j = 1; j < datedPercentageList.size()-1; j++) {
                Date date = datedPercentageList.date(j);
                float[] surroundingPercentages = {datedPercentageList.percentage(j-1), datedPercentageList.percentage(j), datedPercentageList.percentage(j+1)};

                float derivA = surroundingPercentages[1]-surroundingPercentages[0];
                float derivB = surroundingPercentages[2]-surroundingPercentages[1];

                if (derivA >= 0 && derivB <= 0)
                    indexedMax[i].add(date, surroundingPercentages[1]);
                else if (derivA <= 0 && derivB >= 0)
                    indexedMin[i].add(date, surroundingPercentages[1]);
            }
        }


        DatedPercentageList[] indexedWorkdayMinMax = new DatedPercentageList[names.length];
        DatedPercentageList[] indexedWeekendMinMax = new DatedPercentageList[names.length];

        for (int i = 0; i < names.length; i++) {
            indexedWorkdayMinMax[i] = new DatedPercentageList();
            indexedWeekendMinMax[i] = new DatedPercentageList();

            DatedPercentageList min = indexedMin[i];
            DatedPercentageList max = indexedMax[i];


            for (int j = 1; j < min.size()-1; j++) {
                Date date = min.date(j);
                int day = date.getDay();
                float percentage = min.percentage(j);

                if (day > 4)
                    indexedWeekendMinMax[i].add(date, percentage);
                else if (day > 0 && day < 4)
                    indexedWorkdayMinMax[i].add(date, percentage);
            }

            for (int j = 1; j < max.size()-1; j++) {
                Date date = max.date(j);
                int day = date.getDay();
                float percentage = max.percentage(j);

                if (day > 4)
                    indexedWeekendMinMax[i].add(date, percentage);
                else if (day > 0 && day < 4)
                    indexedWorkdayMinMax[i].add(date, percentage);
            }

            indexedWorkdayMinMax[i].sort();
            indexedWeekendMinMax[i].sort();
        }*/
}
