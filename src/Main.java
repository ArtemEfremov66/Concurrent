import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static BlockingQueue<String> sumA = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> sumB = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> sumC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) throws InterruptedException {
        Runnable logic = (() -> {
            for (int i = 0; i < 10_000; i++) {
                try {
                    String text = generateText("abc", 100_000);
                    sumA.put(text);
                    sumB.put(text);
                    sumC.put(text);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        Thread threadMain = new Thread(logic);
        threadMain.start();

        Runnable logicA = (() -> {
            finder('a', sumA);
        });
        Runnable logicB = (() -> {
            finder('b', sumB);
        });
        Runnable logicC = (() -> {
            finder('c', sumC);
        });
        Thread threadA = new Thread(logicA);
        Thread threadB = new Thread(logicB);
        Thread threadC = new Thread(logicC);

        threadA.start();
        threadB.start();
        threadC.start();

        threadA.join();
        threadB.join();
        threadC.join();
        
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int countWord(String text, char word) {
        int sum = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == word) {
                sum++;
            }
        }
        return sum;
    }
    public static void finder(char word, BlockingQueue<String> line) {
            String max = String.valueOf(0);
            int count = 0;
            for (int i = 0; i < 10_000; i++) {
                try {
                    String nextLine = line.take();
                    if (countWord(max, word) < countWord(line.take(), word)) {
                        max = nextLine;
                        count = countWord(max, word);
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
            System.out.println(count);
    }
}
