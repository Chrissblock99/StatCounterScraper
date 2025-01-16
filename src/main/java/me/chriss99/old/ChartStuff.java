package me.chriss99.old;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ChartStuff {
    public static void main(String[] args) {
        List<Date> xData = new ArrayList<>(List.of(new Date(1), new Date(2), new Date(4)));

        // Create Chart
        XYChart chart = new XYChartBuilder().title("Sample Chart").xAxisTitle("Date").yAxisTitle("Percentage").build();
        chart.addSeries("1", xData, List.of(1, 0.5, 1));
        chart.addSeries("2", xData, List.of(0, 0.3, 2));
        chart.addSeries("3", xData, List.of(1.5, 0.7, 0.4));

        // Show it
        new SwingWrapper(chart).displayChart();
    }

    public static void chartDatedOSDataList(ArrayList<DatedOSData> datedOSDataList) {
        ArrayList<Date> dates = new ArrayList<>();
        ArrayList<Float[]> percentagesIndexedByOS = new ArrayList<>();

        for (int i = 0; i < OS.values().length; i++)
            percentagesIndexedByOS.add(new Float[datedOSDataList.size()]);

        for (int i = 0; i < datedOSDataList.size(); i++) {
            DatedOSData datedOSData = datedOSDataList.get(i);

            dates.add(datedOSData.date());
            float[] percentages = datedOSData.indexedOSPercentages();
            for (int j = 0; j < percentages.length; j++)
                percentagesIndexedByOS.get(j)[i] = datedOSData.indexedOSPercentages()[j];
        }






        // Create Chart
        XYChart chart = new XYChartBuilder().title("Desktop OS usage Worldwide").xAxisTitle("Date").yAxisTitle("Percentage").build();
        for (int i = 0; i < OS.values().length; i++)
            chart.addSeries(OS.values()[i].name, dates, Arrays.stream(percentagesIndexedByOS.get(i)).toList());

        // Show it
        new SwingWrapper(chart).displayChart();
    }
}
