package me.chriss99.old;

import java.util.Arrays;
import java.util.Date;

public record DatedOSData(Date date, float[] indexedOSPercentages) {

    public static DatedOSData fromString(String data) {
        String[] dateAndFloats = data.split(": ");
        Date date = new Date(Long.parseLong(dateAndFloats[0]));

        String[] floatStrings = dateAndFloats[1].replace("[", "").replace("]", "").split(",");
        float[] floats = new float[floatStrings.length];

        for (int i = 0; i < floats.length; i++)
            floats[i] = Float.parseFloat(floatStrings[i]);

        return new DatedOSData(date, floats);
    }

    public DatedOSData(float[] indexedOSPercentages) {
        this(new Date(), indexedOSPercentages);
    }

    @Override
    public String toString() {
        return date.getTime() + ": " + Arrays.toString(indexedOSPercentages);
    }
}
