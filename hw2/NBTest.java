import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class NBTest extends NB {
    HashSet<String> labels;
    HashMap<String, Integer> counts;
    final double smoothingConstant = 1;

    private boolean isInitialized() {
        return labels != null && counts != null;
    }

    public void initialize() {
        labels = new HashSet<String>();
        counts = new HashMap<String, Integer>();
        String curLine;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {            
            String key = scanner.next();
            int count = scanner.nextInt();
            if(key.contains("l")) {
                labels.add(key.substring(1, key.length()));
            }
            //System.out.println(key);
            counts.put(key, count);
        }
    }

    private double calculateLogProb(ArrayList<String> tokens, String label) {
        int numLabels = labels.size();
        int wordSize = counts.get("word_size");

        double numer = counts.get(Integer.toString(label.hashCode())) + smoothingConstant;
        double denom = counts.get(Integer.toString("sample_count".hashCode())) + smoothingConstant * numLabels;
        double logProb = Math.log(numer / denom);

        for (int i = 1; i < tokens.size(); i++) { 
            // Assemble the key
            String key = Integer.toString((tokens.get(i) + ";" + label).hashCode());
            //System.out.println(key);
            //if (counts.containsKey(key)) {
                numer = (counts.containsKey(key) ? counts.get(key) : 0) + smoothingConstant;
                denom = counts.get(Integer.toString(("*;" + label).hashCode())) + wordSize * smoothingConstant;
                logProb += Math.log(numer / denom);
            //}
        }
        return logProb;
    }   

    public double test(String testFileName) {
        if (isInitialized()) {
            try {
                String curLine;
                Scanner scanner = new Scanner(new FileReader(testFileName));
                int correctedLabeledCount = 0;
                int instanceCount = 0;
                while (scanner.hasNextLine()) {
                    instanceCount++;
                    curLine = scanner.nextLine();
                    ArrayList<String> tokens = tokenizeDoc(curLine);
                    String trueLabel = tokens.get(0);
                    double maxLogProb = Double.NEGATIVE_INFINITY;
                    String maxLabel = "";
                    //System.out.println(labels);
                    for (String label : labels) {                
                        double prob = calculateLogProb(tokens, label);
                        if(prob > maxLogProb){
                            maxLogProb = prob;
                            maxLabel = label;
                        }
                    }            
                    if (trueLabel.contains(maxLabel)) {
                        correctedLabeledCount ++;
                    }
                    System.out.println(maxLabel + "\t" + maxLogProb);            
                }
                return correctedLabeledCount * 100.0 / instanceCount;
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                return -1;
            }
        } else {
            return -1;
        }
    }

    public static void main(String[] args){
        NBTest nbtest = new NBTest();        
        String filename = "";
        if (args.length == 1) {
            filename = args[0];
            nbtest.initialize();
            double acurracy = nbtest.test(filename);
            //System.out.println("The acurracy is " + acurracy);
        }            
    }
}