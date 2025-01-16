package me.chriss99.old;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatCounterUtil {
    private static final Pattern findPattern = Pattern.compile("[0-9.]*' label='[a-zA-Z ]*");
    private static final Pattern percentagePattern = Pattern.compile("[0-9.]*");
    private static final Pattern osNamePattern = Pattern.compile("='[a-zA-Z ]*");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");

    public static float[] getLatestIndexedOSPercentages() {
        ArrayList<String> regexNamedOSPercentages = extractRegexNamedOSPercentages();
        float[] indexedOSPercentages = new float[OS.values().length];

        for (String regex : regexNamedOSPercentages) {
            Matcher osNameMatcher = osNamePattern.matcher(regex);
            osNameMatcher.find();
            String name = osNameMatcher.group().replace("='", "");

            Matcher percentageMatcher = percentagePattern.matcher(regex);
            percentageMatcher.find();
            float percentage = Float.parseFloat(percentageMatcher.group());

            indexedOSPercentages[OS.getOSFromName(name).index] = percentage;
        }

        return indexedOSPercentages;
    }

    private static ArrayList<String> extractRegexNamedOSPercentages() {
        ArrayList<String> statCounterContent = getStatCounterContent();

        String jsonData = null;
        for (String string : statCounterContent)
            if (string.startsWith("var json =")) {
                jsonData = string;
                break;
            }

        if (jsonData == null)
            throw new NullPointerException("Didn't find line");

        Matcher matcher = findPattern.matcher(jsonData);

        ArrayList<String> regexNamedOSPercentages = new ArrayList<>();
        while(matcher.find())
            regexNamedOSPercentages.add(matcher.group());

        return regexNamedOSPercentages;
    }

    private static ArrayList<String> getStatCounterContent() {
        String time = LocalDate.now().format(formatter);

        ArrayList<String> response = executePost("https://gs.statcounter.com/chart.php?desktop-os_combined-ww-monthly-" + time + "-" + time, "");
        if (response == null)
            throw new NullPointerException("No response from statCounter");

        return response;
    }

    private static ArrayList<String> executePost(String targetURL, String urlParameters) {
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
