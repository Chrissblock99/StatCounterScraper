package me.chriss99.old;
public class WriteLatestData {
    public static void main(String[] args) {
        DataBaseHandler dataBaseHandler = new DataBaseHandler(String.join(" ", args));
        dataBaseHandler.addDatedOSData(new DatedOSData(StatCounterUtil.getLatestIndexedOSPercentages()));
        dataBaseHandler.removeEmptyLines();
    }
}
