package me.chriss99.data;

import me.chriss99.math.GFG;
import org.knowm.xchart.XYChart;

import java.time.DayOfWeek;
import java.util.*;

public record DatedPercentageList(ArrayList<DatedPercentage> datedPercentageList) {
    private static final String LS = System.lineSeparator();

    public DatedPercentageList() {
        this(new ArrayList<>());
    }

    public void add(DatedPercentage datedPercentage) {
        datedPercentageList.add(datedPercentage);
    }

    public void add(Date date, float percentage) {
        datedPercentageList.add(new DatedPercentage(date, percentage));
    }

    public DatedPercentage get(int i) {
        return datedPercentageList.get(i);
    }

    public Date date(int i) {
        return datedPercentageList.get(i).date();
    }

    public float percentage(int i) {
        return datedPercentageList.get(i).percentage();
    }

    public int size() {
        return datedPercentageList.size();
    }

    public void sort() {
        datedPercentageList.sort(Comparator.comparingLong((a) -> a.date().getTime()));
    }

    public void addNumeric(DatedPercentageList other) {
        for (int i = 0; i < other.size(); i++) {
            int index = this.size()-i-1;
            this.datedPercentageList.set(index, new DatedPercentage(this.get(index).date(),
                    this.get(index).percentage() + other.get(other.size()-i-1).percentage()));
        }
    }

    public DatedPercentageList[] getMinMax(int size) {
        DatedPercentageList min = new DatedPercentageList();
        DatedPercentageList max = new DatedPercentageList();

        ArrayList<DatedPercentage> currentlyConsidered = new ArrayList<>();

        for (int i = 0; i < size(); i++) {
            currentlyConsidered.add(datedPercentageList.get(i));

            DatedPercentage smallest = new DatedPercentage(null, 200);
            DatedPercentage largest = new DatedPercentage(null, -100);

            for (DatedPercentage datedPercentage : currentlyConsidered) {
                if (datedPercentage.percentage() > largest.percentage())
                    largest = datedPercentage;
                if (datedPercentage.percentage() < smallest.percentage())
                    smallest = datedPercentage;
            }

            min.add(smallest);
            max.add(largest);

            if (i >= size)
                currentlyConsidered.remove(0);
        }

        min.removeDuplicates();
        max.removeDuplicates();

        return new DatedPercentageList[]{min, max};
    }

    public DatedPercentageList[] getAverageWorkdayWeekend() {
        DatedPercentageList workday = new DatedPercentageList();
        DatedPercentageList weekend = new DatedPercentageList();

        float currentWorkdayPercentage = 0;
        float currentWeekendPercentage = 0;

        for (int j = 0; j < size(); j++) {
            Date date = date(j);
            int day = date.getDay();
            float percentage = percentage(j);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, -(day-1 +7)%7);

            Date week = calendar.getTime();
            int mod = (date.getTime() < new Date(2020-1900, 11-1, 5).getTime()) ? 0 : 1;
            int modifiedDay = (day-mod + 7)%7;

            switch (modifiedDay) {
                case 6: {
                    currentWeekendPercentage += percentage;
                    break;
                }
                case 3, 4: {
                    currentWorkdayPercentage += percentage;
                    break;
                }
            }

            if (day == 0) {
                weekend.add(week, currentWeekendPercentage);
                currentWeekendPercentage = 0;
            }
            if (day == 4) {
                workday.add(week, currentWorkdayPercentage/2);
                currentWorkdayPercentage = 0;
            }
        }

        return new DatedPercentageList[]{workday, weekend};
    }

    public DatedPercentageList getWorkdayWeekendDifference(int size) {
        DatedPercentageList[] averages = getAverageWorkdayWeekend();
        DatedPercentageList max = new DatedPercentageList();

        for (int i = 0; i < averages[0].size() && i < averages[1].size(); i++) {
            float percentage = averages[0].percentage(i)-averages[1].percentage(i);
            max.add(averages[0].date(i), percentage);
        }

        return max.getExtendedAverage(size);
    }

    public DatedPercentageList getPresentableWorkdayWeekendDifference(int size) {
        DatedPercentageList presentable = new DatedPercentageList();

        for (DatedPercentage datedPercentage : getWorkdayWeekendDifference(size).datedPercentageList) {
            float percentage = datedPercentage.percentage();
            percentage = Math.max(-4, Math.min(percentage, 4));
            percentage /= 10;

            presentable.add(datedPercentage.date(), percentage);
        }

        return presentable;
    }

    public DatedPercentageList[] getMinMaxedWorkdayWeekend() {
        DatedPercentageList workdayWeekendDifference = getWorkdayWeekendDifference(4);
        DatedPercentageList[] minMax = getMinMax(7);
        int weekIndex = 0;
        int fuckups = 0;

        DatedPercentageList minMaxedWorkday = new DatedPercentageList();
        DatedPercentageList minMaxedWeekend = new DatedPercentageList();

        for (DatedPercentage datedPercentage : minMax[0].datedPercentageList) {
            try {
                long nextWeek = workdayWeekendDifference.date(weekIndex).getTime() + (1000*60*60*24*7);
                if (nextWeek <= datedPercentage.date().getTime())
                    weekIndex++;

                //System.out.println(workdayWeekendDifference.date(weekIndex) + " | " + datedPercentage.date());
                System.out.println((double) (datedPercentage.date().getTime()-workdayWeekendDifference.date(weekIndex).getTime())/(double) (1000*60*60*24));

                if (workdayWeekendDifference.percentage(weekIndex) > 0)
                    minMaxedWeekend.add(datedPercentage);
                else minMaxedWorkday.add(datedPercentage);
            } catch (Exception e) {
                fuckups++;
            }
        }
        System.out.println("fuckups1: " + fuckups);

        fuckups = 0;
        weekIndex = 0;
        for (DatedPercentage datedPercentage : minMax[1].datedPercentageList) {
            try {
                long nextWeek = workdayWeekendDifference.date(weekIndex).getTime() + (1000*60*60*24*7);
                if (nextWeek <= datedPercentage.date().getTime())
                    weekIndex++;

                if (workdayWeekendDifference.percentage(weekIndex) > 0)
                    minMaxedWorkday.add(datedPercentage);
                else minMaxedWeekend.add(datedPercentage);
            } catch (Exception e) {
                fuckups++;
            }
        }

        System.out.println("fuckups2: " + fuckups);

        minMaxedWorkday.sort();
        minMaxedWeekend.sort();

        return new DatedPercentageList[]{minMaxedWorkday, minMaxedWeekend};
    }

    public DatedPercentageList[] getWorkdayWeekend() {
        DatedPercentageList workday = new DatedPercentageList();
        DatedPercentageList weekend = new DatedPercentageList();

        for (int j = 0; j < size(); j++) {
            Date date = date(j);
            int day = date.getDay();
            float percentage = percentage(j);

            int mod = (date.getTime() < 1604358000000L) ? 0 : 1;

            if (day == (6+mod)%7)
                weekend.add(date, percentage);
            else if (day == 2+mod || day == 3+mod)
                workday.add(date, percentage);
        }

        return new DatedPercentageList[]{workday, weekend};
    }

    public DatedPercentageList getMovingAverageConvolution(int size) {
        float multiplier = 1f/((float) size);
        float[] mover = new float[size];
        Arrays.fill(mover, multiplier);

        float[] list = new float[this.size()];

        for (int i = 0; i < list.length; i++)
            list[i] = percentage(i);

        float[] averaged = GFG.calcConvolution(list, mover);
        int cutOff = size;

        DatedPercentageList averagedList = new DatedPercentageList();
        for (int i = cutOff; i < averaged.length-cutOff; i++)
            averagedList.add(date(i-cutOff/2), averaged[i]);

        return averagedList;
    }

    @Deprecated
    public DatedPercentageList getExtendedAverage(int size) {
        float[] list = new float[this.size()];
        for (int i = 0; i < list.length; i++)
            list[i] = percentage(i);

        float[] averaged = GFG.calcAveraged(list, size);
        int cutOff = size/2;

        DatedPercentageList averagedList = new DatedPercentageList();
        for (int i = cutOff; i < averaged.length-cutOff; i++)
            averagedList.add(date(i-cutOff), averaged[i]);

        return averagedList;
    }

    public List<DatedPercentageList> split(Date[] splitAt) {
        ArrayList<DatedPercentageList> datedPercentageLists = new ArrayList<>();
        for (int i = 0; i <= splitAt.length; i++)
            datedPercentageLists.add(new DatedPercentageList());

        int currentSplitIndex = 0;
        for (DatedPercentage datedPercentage : datedPercentageList) {
            if (splitAt.length != currentSplitIndex && datedPercentage.date().after(splitAt[currentSplitIndex]))
                currentSplitIndex++;

            datedPercentageLists.get(currentSplitIndex).add(datedPercentage);
        }

        return datedPercentageLists;
    }

    public DatedPercentageList getLog() {
        DatedPercentageList logged = new DatedPercentageList();

        for (DatedPercentage datedPercentage : datedPercentageList)
            logged.add(datedPercentage.date(), (float) Math.log10(datedPercentage.percentage()));

        return logged;
    }

    public DatedPercentageList getExponential() {
        DatedPercentageList exponentiated = new DatedPercentageList();

        for (DatedPercentage datedPercentage : datedPercentageList)
            exponentiated.add(new DatedPercentage(datedPercentage.date(), (float) Math.pow(10, datedPercentage.percentage())));

        return exponentiated;
    }

    public DatedPercentageList addToChart(XYChart chart, String name, boolean logarithmic) {
        List<Date> dates = datedPercentageList.stream().map(DatedPercentage::date).toList();
        List<Double> percentages = datedPercentageList.stream().map(
                (a) -> logarithmic ? Math.log10(a.percentage()+1) : a.percentage()
        ).toList();

        chart.addSeries(name, dates, percentages).setLineWidth(0.9f);

        return this;
    }

    public void removeDuplicates() {
        HashSet<DatedPercentage> datedPercentageSet = new LinkedHashSet<>(datedPercentageList);
        datedPercentageList.clear();
        datedPercentageList.addAll(datedPercentageSet);
    }

    public String getLatestDataAndChange() {
        float latestPercentage = percentage(size()-1);
        float previousPercentage = percentage(size()-2);
        float change = latestPercentage-previousPercentage;
        float changePercent = (latestPercentage/previousPercentage -1) * 100;

        change = Math.round(change*100f)/100f;
        latestPercentage = Math.round(latestPercentage*100f)/100f;
        changePercent = Math.round(changePercent*1000f)/1000f;

        String sign = (change >= 0) ? "+" : "";
        return latestPercentage + "% | " + sign + change + " | " + sign + changePercent + "%";
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (DatedPercentage datedPercentage : datedPercentageList)
            stringBuilder.append(datedPercentage.toString()).append(LS);
        if (!stringBuilder.isEmpty())
            stringBuilder.deleteCharAt(stringBuilder.length()-1);

        return stringBuilder.toString();
    }
}
