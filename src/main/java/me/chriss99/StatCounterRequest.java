package me.chriss99;

import me.chriss99.data.DatedPercentage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatCounterRequest {
    public static final String twoFUCKEDLineBreaks = "&lt;BR&gt;&lt;BR&gt;";
    public static final String extraMessage = "[\\s\\S]+?" + twoFUCKEDLineBreaks;
    public static final String optionalExtraMessage = "(|(" + extraMessage + "))";
    public static final Pattern extraMessagePattern = Pattern.compile(extraMessage);
    public static final Pattern datedOSPercentagePattern = Pattern.compile("toolText='" + optionalExtraMessage + "[a-zA-Z ]+- [0-9.]+% - [a-zA-Z 0-9]+'");
    private static final Pattern namePattern = Pattern.compile("='" + optionalExtraMessage + "[a-zA-Z ]+-");
    private static final Pattern percentagePattern = Pattern.compile(" - [0-9.]+% - ");
    private static final Pattern datePattern = Pattern.compile(" - [a-zA-Z 0-9]+'");

    private final Granularity granularity;
    private final Date startDate;
    private final Date endDate;
    private final HashMap<String, ArrayList<DatedPercentage>> nameToDatedPercentagesMap;

    private StatCounterRequest(Granularity granularity, Date startDate, Date endDate) {
        this.granularity = granularity;
        this.startDate = startDate;
        this.endDate = endDate;

        nameToDatedPercentagesMap = createNameToDatedPercentagesMap();
    }

    public static HashMap<String, ArrayList<DatedPercentage>> getListOfDatedOSPercentages(Granularity granularity, Date startDate, Date endDate) {
        return new StatCounterRequest(granularity, startDate, endDate).nameToDatedPercentagesMap;
    }

    private HashMap<String, ArrayList<DatedPercentage>> createNameToDatedPercentagesMap() {
        HashMap<String, ArrayList<DatedPercentage>> nameToDatedPercentagesMap = new HashMap<>();

        for (String datedOSPercentage : extractDatedOSPercentageStrings()) {
            Matcher nameMatcher = namePattern.matcher(datedOSPercentage);
            nameMatcher.find();
            String name = nameMatcher.group().replace("='", "").replace(" -", "");

            Matcher extraMessageMatcher = extraMessagePattern.matcher(name);
            if (extraMessageMatcher.find())
                name = name.replace(extraMessageMatcher.group(), "");

            Matcher percentageMatcher = percentagePattern.matcher(datedOSPercentage);
            percentageMatcher.find();
            float percentage = Float.parseFloat(percentageMatcher.group().replace(" - ", "").replace("%", ""));

            Matcher dateMatcher = datePattern.matcher(datedOSPercentage);
            dateMatcher.find();
            Date date = granularity.dateFromString(dateMatcher.group().replace(" - ", "").replace("'", ""));

            DatedPercentage datedPercentage = new DatedPercentage(date, percentage);

            if (!nameToDatedPercentagesMap.containsKey(name))
                nameToDatedPercentagesMap.put(name, new ArrayList<>());

            nameToDatedPercentagesMap.get(name).add(datedPercentage);
        }

        return nameToDatedPercentagesMap;
    }

    private ArrayList<String> extractDatedOSPercentageStrings() {
        Matcher matcher = datedOSPercentagePattern.matcher(getStatCounterData());

        ArrayList<String> datedOSPercentages = new ArrayList<>();
        while(matcher.find())
            datedOSPercentages.add(matcher.group());

        return datedOSPercentages;
    }

    private String getStatCounterData() {
        ArrayList<String> statCounterContent = getStatCounterContent();

        String jsonData = null;
        for (String string : statCounterContent)
            if (string.startsWith("var json =")) {
                jsonData = string;
                break;
            }

        if (jsonData == null)
            throw new NullPointerException("Didn't find line");

        return jsonData;
    }


    private ArrayList<String> getStatCounterContent() {
        ArrayList<String> response = executePost("https://gs.statcounter.com/chart.php?desktop-os_combined-ww-" +
                granularity.name().toLowerCase() + "-" +
                granularity.stringFromDate(startDate) + "-" +
                granularity.stringFromDate(endDate), "");
        if (response == null)
            throw new NullPointerException("No response from statCounter");

        return response;
    }

    private ArrayList<String> executePost(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length",
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            ArrayList<String> response = new ArrayList<>(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.add(line);
            }
            rd.close();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
