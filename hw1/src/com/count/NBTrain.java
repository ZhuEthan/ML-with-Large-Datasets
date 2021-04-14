package com.count;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.util.IOOperation;

public class NBTrain {
    //each label y the number of training instances of that class
    static Map<String, Integer> numberOfTrainingInstanceOfLabelY = new ConcurrentHashMap<>();
    static Integer numberOfTrainingInstance = 0;
    static Map<String, Integer> nubmerOfTokenWithLabelY = new ConcurrentHashMap<>();
    static Map<String, Integer> totalTokenWithLabelY = new ConcurrentHashMap<>();

    static Set<String> targetLabels = new HashSet<>();
    public static void main(String[] args) {
        NBTrain nbtrain = new NBTrain();

        IOOperation ioOperation = new IOOperation();
        targetLabels.addAll(Arrays.asList("CCAT", "ECAT", "GCAT", "MCAT"));

        String line = null;
        while ((line=ioOperation.read()) != null) {
            String[] splitData = line.split("\\t");
            nbtrain.collectStatistics(splitData);
        }

        nubmerOfTokenWithLabelY.forEach(
            (k, v) -> ioOperation.write("Y="+nbtrain.splitKey(k)[0]+",W="+nbtrain.splitKey(k)[1]+"\t"+v+"\n"));

        totalTokenWithLabelY.forEach(
            (k, v) -> ioOperation.write("Y="+k+",W=*\t"+v+"\n"));

        numberOfTrainingInstanceOfLabelY.forEach((k, v) -> ioOperation.write("Y="+k+"\t"+v+"\n"));

        ioOperation.write("Y=*\t"+numberOfTrainingInstance+"\n");
        
        ioOperation.close();

    }

    private String[] splitKey(String str) {
        return str.split(",");
    }

    public void collectStatistics(String[] splitData) {
        List<String> labels = parseLabel(splitData[0]);
        List<String> words = parseWord(splitData[1]);

        addNumberOfTrainingInstance();

        labels.parallelStream()
            .forEach(this::addNumOfTrainingInstanceOfLabelY);
        
        words.parallelStream()
            .forEach(word -> addNumberOfTokenWithLabels(labels, word));
    }

    private void addNumberOfTokenWithLabels(List<String> labels, String word)  {
        labels.stream()
            .forEach(label -> addNumberOfTokenWithLabel(label, word));
    }

    private void addNumberOfTokenWithLabel(String label, String word) {
        totalTokenWithLabelY.put(label, totalTokenWithLabelY.getOrDefault(label, 0) + 1);
        nubmerOfTokenWithLabelY.put(label+","+word, nubmerOfTokenWithLabelY.getOrDefault(label+","+word, 0)+1);
    }

    private void addNumOfTrainingInstanceOfLabelY(String label) {
        numberOfTrainingInstanceOfLabelY.put(label, numberOfTrainingInstanceOfLabelY.getOrDefault(label, 0)+1);
    }

    private void addNumberOfTrainingInstance() {
        numberOfTrainingInstance += 1;
    }

    public List<String> parseWord(String str) {
        return Arrays.asList(str.split("\\s+"))
            .stream()
            .map(word -> word.replaceAll("\\W", ""))
            .filter(word -> word.length() > 0)
            .collect(Collectors.toList());
    }

    public List<String> parseLabel(String str) {
        return Arrays.asList(str.split(","))
            .stream()
            .map(word -> word.replaceAll("\\W", ""))
            .filter(word -> word.length() > 0 && targetLabels.contains(word))
            .collect(Collectors.toList());
    }
}