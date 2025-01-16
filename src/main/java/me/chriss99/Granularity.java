package me.chriss99;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.Function;

public enum Granularity {
    DAILY((s) -> {
        String[] strings = s.split(" ");

        int day = Integer.parseInt(strings[0]);
        Month month = getMonth(strings[1]);
        int year = Integer.parseInt(strings[2]);

        return java.sql.Date.valueOf(LocalDate.of(year, month, day));
    }, (date) -> LocalDate.of(date.getYear()+1970, date.getMonth()+1, date.getDate()- ((date.getMonth() == 1 && (date.getYear()+1970)%4 == 2010%4) ? 1 : 0)).format(DateTimeFormatter.ofPattern("yyyyMMdd"))),
    MONTHLY((s) -> {
        String[] strings = s.split(" ");

        Month month = getMonth(strings[0]);
        int year = Integer.parseInt(strings[1]);

        return java.sql.Date.valueOf(LocalDate.of(year, month, month.length(false)));
    }, (date) -> LocalDate.of(date.getYear()+1970, date.getMonth()+1, date.getDate()).format(DateTimeFormatter.ofPattern("yyyyMM")));

    private final Function<String, Date> stringToDateFunction;
    private final Function<Date, String> dateToStringFunction;

    Granularity(Function<String, Date> stringToDateFunction, Function<Date, String> dateToStringFunction) {
        this.stringToDateFunction = stringToDateFunction;
        this.dateToStringFunction = dateToStringFunction;
    }

    public Date dateFromString(String dateString) {
        return stringToDateFunction.apply(dateString);
    }

    public String stringFromDate(Date date) {
        return dateToStringFunction.apply(date);
    }


    private static Month getMonth(String month) {
        return switch (month) {
            case "Jan" -> Month.JANUARY;
            case "Feb" -> Month.FEBRUARY;
            case "Mar" -> Month.MARCH;
            case "Apr" -> Month.APRIL;
            case "May" -> Month.MAY;
            case "June" -> Month.JUNE;
            case "July" -> Month.JULY;
            case "Aug" -> Month.AUGUST;
            case "Sept" -> Month.SEPTEMBER;
            case "Oct" -> Month.OCTOBER;
            case "Nov" -> Month.NOVEMBER;
            case "Dec" -> Month.DECEMBER;
            default -> {
                try {
                    yield Month.valueOf(month.toUpperCase());
                } catch (Exception e) {
                    throw new IllegalArgumentException("Unexpected value: \"" + month + "\"");
                }
            }
        };
    }
}
