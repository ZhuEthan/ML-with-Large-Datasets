import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Aggregate {

    static final int CORPUS_YEAR = 1990;

    static public void main (String[] args) throws IOException {
        if (args.length < 1) {
            System.exit(-1);
        }

        int type = 0;
        try{
            type = Integer.parseInt(args[0]);
        } catch(Exception e){
            System.exit (-1);
        }

        Pattern pattern; 
        if (type == 0) {
            pattern = Pattern.compile("(\\w+)\\s+(\\d+)\\s+(\\d+)");
        } else {
            pattern = Pattern.compile("(\\w+\\s+\\w+)\\s+(\\d+)\\s+(\\d+)");
        }
        long totalBFreq = 0;
        long totalCFreq = 0;
        long bFreq = 0;
        long cFreq = 0;
        String last = null;
        String curLine;
        String str;
        BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter (new OutputStreamWriter(System.out));
        while ((curLine = reader.readLine()) != null) {
            Matcher matcher = pattern.matcher(curLine);
            if (matcher.matches()) {
                String token = matcher.group(1);
                int year = Integer.parseInt(matcher.group(2));
                long n = Long.parseLong(matcher.group(3));

                if (last != null && !token.equals(last)){
                    str = String.format("%s %d %d\n", last, bFreq, cFreq);
                    writer.write(str);
                    bFreq = 0;
                    cFreq = 0;
                }
                if (year < CORPUS_YEAR){
                    bFreq += n;
                    totalBFreq += n;
                } else {
                    cFreq += n;
                    totalCFreq += n;
                }
                last = token;
            }
        }

        str = String.format("%s %d %d\n", last, bFreq, cFreq);
        writer.write(str);
        if (type == 0) {
            str = String.format("* %d %d\n", totalBFreq, totalCFreq);
        } else {
            str = String.format("* * %d %d\n", totalBFreq, totalCFreq);
        } 
        writer.write(str);
        writer.flush();
        writer.close();
        reader.close();
    }
}
