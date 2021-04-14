package com.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;



public class NBTest {
    static Set<String> targetLabels = new HashSet<>();

    static Map<String, Double> numberOfTrainingInstanceOfLabelY = new ConcurrentHashMap<>();
    static Double numberOfTrainingInstance = 0.0;
    static Map<String, Double> nubmerOfTokenWithLableY = new ConcurrentHashMap<>();
    static Map<String, Double> totalTokenWithLabelY = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        NBTest nbtest = new NBTest();
        nbtest.initValue();
        //nbtest.printData();
        //IOOperation ioOperation = new IOOperation();
        targetLabels.addAll(Arrays.asList("CCAT", "ECAT", "GCAT", "MCAT"));        

        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        br.lines().forEach(line -> {
            String[] splitline = line.split("\\t");
            Optional<String> labelname = targetLabels.stream()
                .max((a, b) -> Double.compare(
                    nbtest.calculateProbability(splitline[1], a),
                    nbtest.calculateProbability(splitline[1], b)));
            labelname.ifPresent(label -> System.out.print("The prediction is " + label));
            System.out.println(" compared with real label: " + splitline[0]);
        });
        br.close();
    }

    public void initValue() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        in.lines().forEach(this::consume);
    }

    public void printData() {
        numberOfTrainingInstanceOfLabelY.forEach(
            (k, v) -> System.out.println(k + ":" + v));
        nubmerOfTokenWithLableY.forEach(
            (k, v) -> System.out.println(k + ":" + v));
        totalTokenWithLabelY.forEach(
            (k, v) -> System.out.println(k + ":" + v));
    }

    private void consume(String line) {
        String[] splitStr = line.split("\\t");
        if (splitStr[0].contains("Y=") && splitStr[0].contains(",W=*")) {
            int breakpos = splitStr[0].indexOf(",W=");
            totalTokenWithLabelY.put(
                splitStr[0].substring(2, breakpos), Double.valueOf(splitStr[1]));
        } else if (splitStr[0].contains("Y=") && splitStr[0].contains(",W=")) {
            int breakpos = splitStr[0].indexOf(",W=");
            nubmerOfTokenWithLableY.put(
                splitStr[0].substring(2, breakpos) + "," + splitStr[0].substring(breakpos+3), 
                Double.valueOf(splitStr[1]));
        } else if (splitStr[0].contains("Y=*")) {
            numberOfTrainingInstance = Double.valueOf(splitStr[1]);
        } else {
            numberOfTrainingInstanceOfLabelY.put(
                splitStr[0].substring(2), Double.valueOf(splitStr[1]));
        }
    }

    public Double calculateProbability(String words, String label) {
        Double probWithWordAndLabel = parseWord(words).stream()
            .map(word -> this.getProbForWordWithLabel(word, label))
            .reduce(Double.valueOf(0), (a, b)->(a+b));
        Double probWithLabel = getProbForLabel(label);

        return probWithWordAndLabel+probWithLabel;
    }

    public Double getProbForWordWithLabel(String word, String label) {
        return Math.log(getNumberOfTokenWithWordAndLabel(word, label)+1)-Math.log(getNumberOfTokenWithLabel(label)+1);
    }

    private double getNumberOfTokenWithWordAndLabel(String word, String label) {
        return nubmerOfTokenWithLableY.getOrDefault(label+","+word, 0.0);
    }

    private double getNumberOfTokenWithLabel(String label) {
        return totalTokenWithLabelY.getOrDefault(label, 0.0);
    }

    public double getProbForLabel(String label) {
        return Math.log(getNumberOfLabel(label)+1) - Math.log(getNumberOfTotalLabel()+1);
    }

    private double getNumberOfTotalLabel() {
        return numberOfTrainingInstance;
    }

    private double getNumberOfLabel(String label) {
        return numberOfTrainingInstanceOfLabelY.getOrDefault(label, 0.0);
    }

    public List<String> parseWord(String str) {
        return Arrays.asList(str.split("\\s+"))
            .stream()
            .map(word -> word.replaceAll("\\W", ""))
            .filter(word -> word.length() > 0)
            .collect(Collectors.toList());
    }

    public Set<String> parseLabel(String str) {
        return Arrays.asList(str.split(","))
            .stream()
            .map(word -> word.replaceAll("\\W", ""))
            .filter(word -> word.length() > 0 && targetLabels.contains(word))
            .collect(Collectors.toSet());
}
