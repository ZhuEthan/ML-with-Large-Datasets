import java.util.Scanner;

public class MergeCounts {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int wordSize = 0;
        String previousKey = scanner.next();
        int count = scanner.nextInt();
        int sum = count;
        String curKey;
        while(scanner.hasNext()) {
            curKey = scanner.next();
            if (curKey.contains("w")) {
                System.out.println(previousKey+" " + sum);
                String lastWord = curKey;
                wordSize++;
                while (scanner.hasNext()) {
                    String curWord = scanner.next();
                    //if (!curWord.equals(lastWord)) {
                        wordSize++;
                        //System.out.println(wordSize);
                        //lastWord = curWord;
                    //}
                }
                break;
            } else {
                count = scanner.nextInt();
                if(curKey.equals(previousKey)) {
                    sum += count;
                } else {
                    System.out.println(previousKey+" " + sum);
                    previousKey = curKey;
                    sum = count;
                }
            }
        }
        System.out.println(previousKey + " " + sum);
        System.out.println("word_size " + wordSize);
    }
}
