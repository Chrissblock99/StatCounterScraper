package me.chriss99;

import me.chriss99.data.DatedPercentageList;
import me.chriss99.data.DatedPercentageListFile;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.XYStyler;

public class DisplayXChartData {
    public static final String[] names = DataProcessor.names;
    public static final String[] types = {"", "workday", "weekend"};
    public static final int[] convolutionSizes = {350, 8, 4};
    public static final long DAY_IN_MILLIS = 1000*60*60*24;

    public static void main(String[] args) {
        DatedPercentageList[][] data = DataProcessor.getIndexedDataWorkdayWeekend();

        // Create Chart
        XYChart chart = new XYChartBuilder().title("Desktop OS usage Worldwide").xAxisTitle("Date").yAxisTitle("Percentage").width(3000).height(1200).build();
        XYStyler styler = chart.getStyler();
        styler.setZoomEnabled(true);
        styler.setToolTipsEnabled(true);
        styler.setMarkerSize(0);

        boolean logarithmic = true;

        //for (int i = 0; i < names.length; i++)
        //    for (int j = 0; j < 3; j++)
        //        data[j][i].addToChart(chart, names[i] + " " + types[j], logarithmic);

        for (int i = 0; i < names.length; i++) {
            data[0][i].addToChart(chart, names[i], logarithmic);
            DatedPercentageList[] minMax = data[0][i].getMinMax(7);
            minMax[0].addToChart(chart, names[i] + " (min)", logarithmic);
            minMax[1].addToChart(chart, names[i] + " (max)", logarithmic);
            //DatedPercentageList[] workdayWeekend = data[0][i].getWorkdayWeekend();
            //workdayWeekend[0].addToChart(chart, names[i] + " (workday)", logarithmic);
            //workdayWeekend[1].addToChart(chart, names[i] + " (weekend)", logarithmic);
            //DatedPercentageList[] averageWorkdayWeekend = data[0][i].getAverageWorkdayWeekend();
            //averageWorkdayWeekend[0].addToChart(chart, names[i] + " (average workday)", logarithmic);
            //averageWorkdayWeekend[1].addToChart(chart, names[i] + " (average weekend)", logarithmic);
            //DatedPercentageList[] minMaxedWorkdayWeekend = data[0][i].getMinMaxedWorkdayWeekend();
            //minMaxedWorkdayWeekend[0].addToChart(chart, names[i] + " (minMaxed workday)", logarithmic);
            //minMaxedWorkdayWeekend[1].addToChart(chart, names[i] + " (minMaxed weekend)", logarithmic);
            //data[0][i].getPresentableWorkdayWeekendDifference(4).addToChart(chart, names[i] + " (difference)", false);
        }

        /*for (int i = 0; i < names.length; i++) {
            DatedPercentageList a = data[0][i];
            Date startDate = a.date(0);
            Date endDate = new Date(a.date(a.size() - 1).getTime());// + DAY_IN_MILLIS*365*7);

            //data[0][i] = data[0][i].getMovingAverageConvolution(3000);

            for (int j = 0; j < 3; j++) {
                DatedPercentageList datedPercentageList = data[j][i].getExtendedAverage(convolutionSizes[j]/5);

                DatedLinearRegression avgLine = DatedLinearRegression.fromDatedPercentageList(datedPercentageList);
                double avgLineR2 = avgLine.linearRegression().R2();
                DatedPercentageList avgLineList = avgLine.getDatedPercentageList(startDate, endDate, DAY_IN_MILLIS * 7);

                DatedLinearRegression avgExponential = DatedLinearRegression.fromDatedPercentageList(datedPercentageList.getLog());
                double avgExponentialR2 = avgExponential.linearRegression().R2();
                DatedPercentageList avgExponentialList = avgExponential.getDatedPercentageList(startDate, endDate, DAY_IN_MILLIS * 7).getExponential();


                datedPercentageList.addToChart(chart, names[i] + " " + types[j], logarithmic);

                if (avgLineR2 > avgExponentialR2)
                    avgLineList.addToChart(chart, names[i] + " " + types[j] + " (avgLine " + avgLineR2 + ")", logarithmic);
                else avgExponentialList.addToChart(chart, names[i] + " " + types[j] + " (exponential " + avgExponentialR2 + ")", logarithmic);
            }
        }*/

        // Show it
        new SwingWrapper(chart).displayChart();
    }
}
