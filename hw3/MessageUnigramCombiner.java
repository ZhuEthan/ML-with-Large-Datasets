import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUnigramCombiner {
  
  static public void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter (new OutputStreamWriter(System.out));

        Pattern pattern1 = Pattern.compile("(\\w+)\\s+(\\d+\\s+\\d+)"); 
        Pattern pattern2 = Pattern.compile("(\\w+)\\s+(\\w+\\s+\\w+)\\s+(\\W)"); 

        String curLine = reader.readLine(); 
        writer.write(curLine);
        writer.write("\n");

        String str = null;
        Matcher matcher = null;
        while (curLine != null) {
            matcher = pattern1.matcher(curLine);
            String attr = "";
            String value = "";
            if (matcher.matches()) {
                attr = matcher.group(1);
                value = matcher.group(2);
            }

            while ((curLine = reader.readLine()) != null) {
                matcher = pattern2.matcher(curLine);
                if (matcher.matches()) {
                    String phrase = matcher.group(2);
                    String flag = matcher.group(3);   
                    str = String.format("%s %s %s %s\n", phrase, flag, flag, value);
                    writer.write(str);
                } else {
                    break;
                }
            }
        }

        writer.flush();
        writer.close();
        reader.close();
    }
}
