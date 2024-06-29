package PDFreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class identifier {
    static String year = "==";
    static String Subject = "==";
    static String paperType_n;
    static String paperType_s;
    static String paperType_EC;
    static String paperMonth;
    static int time_m = 0;

    public static void specifier(String line) {

        if (line.contains("© UCLES")) {
            String str = line;
            Pattern pattern = Pattern.compile("\\d{4}");
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()) {
                year = matcher.group();
            }

            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!Year found: " + year);
        }

        if (line.contains("PHYSICS")) {
            Subject = "physics";
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!subject found:" + Subject);

        }

        String patternsForDetail = "^Paper.*?" + year + "$";
        if (line.matches(patternsForDetail)) {

            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!detail found:");
            Pattern pattern = Pattern.compile("Paper (\\d+) (\\w+) \\((\\w+)\\) ([^ ]+) (\\d+)");
            Matcher matcher = pattern.matcher(line);
            int paperType_ns;
            if (matcher.find()) {
                paperType_ns = Integer.parseInt(matcher.group(1));
                paperType_s = matcher.group(2);
                paperType_EC = matcher.group(3);
                paperMonth = matcher.group(4);

                paperType_n = Integer.toString(paperType_ns);

                System.out.println("paperType_n = " + paperType_n);
                System.out.println("paperType_s = " + paperType_s);
                System.out.println("paperType_EC = " + paperType_EC);
                System.out.println("paperMonth = " + paperMonth);
            }

        }
        line = line.trim(); // remove leading/trailing spaces

        Pattern patternForTime = Pattern.compile("(\\d+) hour(?:s)? (\\d+) minute(?:s)?");
        Matcher matcherForTime = patternForTime.matcher(line);
        if (matcherForTime.find()) {
            int hours = Integer.parseInt(matcherForTime.group(1));
            time_m = Integer.parseInt(matcherForTime.group(2));
            time_m += hours * 60;
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Minutes: " + time_m);
        }

    }

    public static void identifier(String docinput) {
        String syspath = System.getProperty("user.dir");
        String docname = docinput;
        String extPath = syspath + "\\ext_PDF";
        String fileFPath = extPath + "\\" + docname;
        String contextFPath = extPath + "\\" + docname + "\\context";
        String imageFPath = extPath + "\\" + docname + "\\image";

        File file = new File(contextFPath + "\\context1.txt");
        if (!file.exists()) {
            System.out.println("File does not exist: " + file.getAbsolutePath());
        }
        if (file.exists()) {
            System.out.println("File does exist: " + file.getAbsolutePath());
        }
        if (file.length() == 0) {
            System.out.println("File is empty: " + file.getAbsolutePath());
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            System.out.println("it is work mmmm");

            // Use a loop to iterate over each line of the file
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);

                specifier(line);
            }

            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }

    }
}
