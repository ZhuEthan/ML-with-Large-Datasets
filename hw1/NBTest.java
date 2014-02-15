import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class NBTest extends NB {
    HashSet<String> labels;
    HashSet<String> words;
    HashMap<String, Integer> counts;
    final double smoothingConstant = 1.0;

    private boolean isInitialized() {
        return labels != null && words != null && counts != null;
    }

    public void initialize() {
        labels = new HashSet<String>();
        words = new HashSet<String>();
        counts = new HashMap<String, Integer>();
        String curLine;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {            
            String key = scanner.next();
            if(key.contains("W=*")){
                // Get the label
                labels.add(key.substring(2, 6));
            } else if(key.contains(",")){
                String[] tokens = key.split(",");
                if(!tokens[1].equals("W=*")){
                    words.add(tokens[1]);
                }
            }
            counts.put(key, scanner.nextInt());
        }
    }

    private double calculateLogProb(ArrayList<String> tokens, String label) {
        int numLabels = labels.size();
        int wordSize = words.size();
        double numer = counts.get(label) + smoothingConstant;
        double denom = counts.get("Y=*") + smoothingConstant * numLabels;
        double logProb = Math.log(numer / denom);

        for (int i = 1; i < tokens.size(); i++) { 
            // Assemble the key
            String key = "Y=" + label+",W=" + tokens.get(i);
            numer = (counts.containsKey(key) ? counts.get(key) : 0) + smoothingConstant;
            denom = counts.get(label) + wordSize;
            logProb += Math.log(numer / denom);
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