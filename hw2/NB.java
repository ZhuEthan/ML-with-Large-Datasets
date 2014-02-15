import java.util.ArrayList;

public class NB {
    public ArrayList<String> tokenizeDoc(String curDoc) {
        String[] words = curDoc.split("\\s+");
        ArrayList<String> tokens = new ArrayList<String>();
        tokens.add(words[0]);
        for (int i = 1; i < words.length; i++) {
            words[i] = words[i].replaceAll("\\W", "");
            if (words[i].length() > 0) {
                tokens.add(words[i]);
            }
        }
        return tokens;
    }
}
