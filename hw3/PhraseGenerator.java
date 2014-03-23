import java.util.Scanner;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhraseGenerator {
    static final Pattern patternForX = Pattern.compile("(\\w+\\s+\\w+)\\s+\\+\\s\\+\\s+(\\d+)\\s+(\\d+)"); 
    static final Pattern patternForY = Pattern.compile("(\\w+\\s+\\w+)\\s+\\-\\s\\-\\s+(\\d+)\\s+(\\d+)"); 
    static final Pattern patternForXY = Pattern.compile("(\\w+\\s+\\w+)\\s+(\\d+)\\s+(\\d+)"); 
    static final Pattern patternForUnigram = Pattern.compile("(\\W+)\\s+(\\d+)\\s+(\\d+)"); 
    static final Pattern patternForBigram = Pattern.compile("(\\W+\\s+\\W+)\\s+(\\d+)\\s+(\\d+)"); 

    static long totalBX = 0;
    static long  totalCX = 0;
    static long  totalBXY = 0;
    static long  totalCXY = 0;
    final static int top = 20;  

    static double KL(double p, double q){
        if (p <= 0 || q <= 0) {
          return -Double.MAX_VALUE;
        }
        return p * Math.log(p / q);
    }
  
    private static double calculatePhraseness(long x, long y, long xy){
        double px = (double) x / totalCX;
        double py = (double) y / totalCX;
        double pxy = (double) xy / totalCXY;
        return KL(pxy, px * py);
    }
  
    private static double calculateInformativeness(long cxy, long bxy){
        double pCxy = (double) cxy / totalCXY;
        double pBxy = (double) bxy / totalBXY;
        return KL(pCxy, pBxy);
    }
  
    private static String parseAttrValPair(String line, long[] freq, Pattern pattern){
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            freq[0] = Long.parseLong(matcher.group(2));
            freq[1] = Long.parseLong(matcher.group(3));
            return matcher.group(1);
        }
        return null;
    }

    public static void main(String [] args) {
        Scanner scanner = new Scanner(System.in);
        PriorityQueue<Score> topPhrases = new PriorityQueue<Score>(top, new ScoreComparator());
    
        String curLine = scanner.nextLine();
        long[] freq = new long[2];
        parseAttrValPair(curLine, freq, patternForBigram);
        totalBX = freq[0];
        totalCX = freq[1];

        curLine = scanner.nextLine();
        parseAttrValPair(curLine, freq, patternForUnigram);
        totalBXY = freq[0];
        totalCXY = freq[1];
    
        while (scanner.hasNextLine()){
            curLine = scanner.nextLine();
            parseAttrValPair(curLine, freq, patternForX);
            long bxy = freq[0];
            long cxy = freq[1];

            curLine = scanner.nextLine();
            parseAttrValPair(curLine, freq, patternForY);
            long by = freq[0];
            long cy = freq[1];

            curLine = scanner.nextLine();
            String phrase = parseAttrValPair(curLine, freq, patternForXY);
            long bx = freq[0];
            long cx = freq[1];

            double phraseness = calculatePhraseness(cx, cy, cxy);
            double informativeness = calculateInformativeness(cxy, bxy);
            double score = phraseness + informativeness;
      
            if (topPhrases.size() < top) {
                topPhrases.add(new Score(phrase, score, phraseness, informativeness));
            } else {
                Score s = topPhrases.peek();
                if (score > s.score){
                    topPhrases.poll();
                    topPhrases.add(new Score(phrase, score, phraseness, informativeness));
                } 
            } 
        } 
        LinkedList<Score> list = new LinkedList<Score> ();
        while (!topPhrases.isEmpty()) {
            list.push(topPhrases.poll());
        }
        while (!list.isEmpty()){
            Score s = list.pop();
            System.out.format("%s\t%f\t%f\t%f\n", s.phrase , s.score, s.phraseness, s.informativeness);
        }
    }
}

class Score {
    String phrase;
    double score;
    double phraseness;
    double informativeness;
    public Score(String p, double s, double ph, double info){
        phrase = p;
        score = s;
        phraseness = ph;
        informativeness = info;
    }
}

class ScoreComparator implements Comparator<Score>{
    @Override
    public int compare(Score ps0, Score ps1) {
        return ps0.score < ps1.score ? -1 : ps0.score > ps1.score ? 1 : 0;
    }
}
