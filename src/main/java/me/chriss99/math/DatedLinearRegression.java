package me.chriss99.math;

import me.chriss99.data.DatedPercentage;
import me.chriss99.data.DatedPercentageList;

import java.util.ArrayList;
import java.util.Date;

public record DatedLinearRegression(LinearRegression linearRegression) {
    public static DatedLinearRegression fromDatedPercentageList(DatedPercentageList datedPercentageList) {
        int size = datedPercentageList.size();
        double[] dates = new double[size];
        double[] percentages = new double[size];

        for (int i = 0; i < size; i++) {
            dates[i] = datedPercentageList.date(i).getTime();
            percentages[i] = datedPercentageList.percentage(i);
        }

        return new DatedLinearRegression(dates, percentages);
    }

    public DatedLinearRegression(double[] dates, double[] percentages) {
        this(new LinearRegression(dates, percentages));
    }

    public DatedPercentageList getDatedPercentageList(Date from, Date to, long gap) {
        ArrayList<DatedPercentage> datedPercentages = new ArrayList<>();

        for (long time = from.getTime(); time < to.getTime(); time += gap) {
            Date date = new Date(time);
            datedPercentages.add(new DatedPercentage(date, (float) linearRegression.predict((double) time)));
        }

        return new DatedPercentageList(datedPercentages);
    }
}
