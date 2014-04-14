import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ApproxPageRank {

    private static double alpha = 0.3;
    private static double epsilon = 0.00001;
    private static String seed;
    private static String path;
    private static Map<String, Double> p; // p vector
    private static Map<String, Double> r; // r vector

    private static void push(String[] tokens) {
        String ukey = tokens[0];

        int len = tokens.length;
        int outdegree = len - 1;
        final double ru = r.get(ukey);

        // 1. p' = p + alpha*ru
        if (p.containsKey(ukey)) {
            p.put(ukey, p.get(ukey) + alpha * ru);
        } else {
            p.put(ukey, alpha * ru);
        }

        // 2. r' = r - ru + (1 - alpha) * ru * W;
        if (r.containsKey(ukey)) {
            r.remove(ukey);
        }

        double selfweight = 1.0 / 2.0;
        if (r.containsKey(ukey)) {
            r.put(ukey, r.get(ukey) + (1 - alpha) * ru * selfweight);
        } else {
            r.put(ukey, (1 - alpha) * ru * selfweight);
        }

        for (int i = 1; i < len; i++) {
            String vkey = tokens[i];
            double vweight = 1.0 / ( 2.0 * (double) outdegree);

            if (r.containsKey(vkey)) {
                r.put(vkey, r.get(vkey) + (1 - alpha) * ru * vweight);
            } else {
                r.put(vkey, (1 - alpha) * ru * vweight);
            }
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
                    int len = tokens.length;
                    int outdegree = len - 1;

                    if (outdegree == 0 || ru / (double) outdegree <= epsilon) {
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