import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ApproxPageRank {

    private static double alpha = 0.3;
    private static double epsilon = 0.00001;
    private static String seed;
    private static String path;
    private static Map<String, Double> p;
    private static Map<String, Double> r;

    private static void update(String key, double ru, double weight) {
        if (r.containsKey(key)) {
            r.put(key, r.get(key) + (1 - alpha) * ru * weight);
        } else {
            r.put(key, (1 - alpha) * ru * weight);
        }
    }

    private static void push(String[] tokens) {
        String ukey = tokens[0];

        double outdegree = tokens.length - 1;
        double ru = r.get(ukey);

        if (p.containsKey(ukey)) {
            p.put(ukey, p.get(ukey) + alpha * ru);
        } else {
            p.put(ukey, alpha * ru);
        }

        if (r.containsKey(ukey)) {
            r.remove(ukey);
        }

        update(ukey, ru, 1.0 / 2.0);
        for (int i = 1; i < tokens.length; i++) {
            update(tokens[i], ru, 1.0 / (2.0 * outdegree));
        }
    }

    public static void main(String[] args) throws IOException {

        path = args[0];
        seed = args[1];
        alpha = Double.parseDouble(args[2]);
        epsilon = Double.parseDouble(args[3]);

        p = new HashMap<String, Double>();
        r = new HashMap<String, Double>();

        r.put(seed, 1.0);

        boolean updated = true;
        while (updated) {
            updated = false;
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = null;

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("\t");
                String ukey = tokens[0];

                if (r.containsKey(ukey)) {
                    double ru = r.get(ukey);
                    double outdegree = tokens.length - 1;

                    if (outdegree == 0 || (ru / outdegree) <= epsilon) {
                        continue;
                    }

                    push(tokens);
                    updated = true;
                }
            }
        }

        for (Map.Entry<String, Double> entry : p.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }
    }
}