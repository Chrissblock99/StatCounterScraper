package me.chriss99.data;

import java.util.Date;

public record DatedPercentage(Date date, float percentage) {

    public static DatedPercentage fromString(String string) {
        String[] dateAndPercentage = string.split(": ");

        Date date = new Date(Long.parseLong(dateAndPercentage[0]));
        float percentage = Float.parseFloat(dateAndPercentage[1]);

        return new DatedPercentage(date, percentage);
    }

    @Override
    public String toString() {
        return date.getTime() + ": " + percentage;
    }
}
