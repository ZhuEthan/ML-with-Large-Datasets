import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NBTrain extends NB {
    private HashMap<Integer, Integer> counts;
    private HashSet<Integer> words;
    private HashSet<String> labelsSet;

    public NBTrain() {
        counts = new HashMap<Integer, Integer>(100000);
        words = new HashSet<Integer>(100000);
        labelsSet = new HashSet<String>();
    }  

    private void increaseCount(int key, int increasedBy) {
        if (counts.size() > 100000) {
            for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
            counts.clear();
        }
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
            for (int i = 0; i < labels.length; i++) {
                for (int j = 1; j < tokens.size(); j++) {
                    increaseCount((tokens.get(j) + ";" + labels[i]).hashCode(), 1);
                    words.add(tokens.get(j).hashCode());
                    //System.out.println("w" + tokens.get(j).hashCode());
                    if (words.size() > 100000) {
                        for (int w : words) {
                            System.out.println("w" + w);
                        }
                        words.clear();
                    }
                    //System.out.println(tokens.get(j) + ";" + labels[i] + " 1");
                }
                labelsSet.add(labels[i]);
                increaseCount(("*;" + labels[i]).hashCode(), tokens.size() - 1);
                increaseCount("sample_count".hashCode(), 1);
                increaseCount(labels[i].hashCode(), 1);
                //System.out.println("*;" + labels[i] + " " + (tokens.size() - 1));
                //System.out.println("sample_count 1");
                //System.out.println(labels[i] + " 1");
            }
        }

        if (counts.size() != 0) {
            for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
        }

        if (words.size() != 0) {
            for (int w : words) {
                System.out.println("w" + w);
            }
        }
        for (String label : labelsSet) {
            System.out.println("l" + label + " 1");
        }
        //System.out.println(words.size());
    }

    public static void main(String[] args) {
        NBTrain nbtrain = new NBTrain();
        nbtrain.train();
    }   
}
