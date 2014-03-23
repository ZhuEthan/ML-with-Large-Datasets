import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageGenerator {
    static public void main(String[] args) throws IOException {
        String curLine = null;
        BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter (new OutputStreamWriter(System.out));
        Matcher matcher = null;
        String str = null;
        Pattern pattern = Pattern.compile("(\\w+)\\s+(\\w+)\\s+\\d+\\s+\\d+");
        while ((curLine = reader.readLine()) != null) {
            matcher = pattern.matcher(curLine);
            if (matcher.matches()) {
                String phrase1 = matcher.group(1);
                String phrase2 = matcher.group(2);

                str = String.format("%s %s %s +\n", phrase1, phrase1, phrase2);
                writer.write(str); 
                str = String.format("%s %s %s -\n", phrase2, phrase1, phrase2); 
                writer.write(str); 
            }
        }
        writer.flush();
        writer.close();
        reader.close();
    }   

}
