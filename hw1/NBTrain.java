import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NBTrain extends NB {
    private HashMap<String, Integer> counts;

    public NBTrain() {
        counts = new HashMap<String, Integer>();
    }  

    private void increaseCount(String key, int increasedBy) {
        if (counts.containsKey(key)) {
            counts.put(key, counts.get(key) + increasedBy);
        } else {
            counts.put(key, increasedBy);
        }
    }

    public void train() {
        String curLine;
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            curLine = scanner.nextLine();
            ArrayList<String> tokens = tokenizeDoc(curLine);          
            String[] labels = tokens.get(0).split(",");
            Pattern pattern = Pattern.compile(".+CAT");
            for (int i = 0; i < labels.length; i++) {
                Matcher matcher = pattern.matcher(labels[i]);
                if (matcher.find()) {
                    for (int j = 1; j < tokens.size(); j++) {
                        increaseCount("Y=" + labels[i] + ",W=" + tokens.get(j), 1);
                    }
                    increaseCount("Y=*", 1);
                    increaseCount(labels[i], 1);
                    increaseCount("Y=" + labels[i] + ",W=*", tokens.size() - 1);
                }
            }
        }
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }
    }

    public static void main(String[] args) {
        NBTrain nbtrain = new NBTrain();
        nbtrain.train();
    }   
}
