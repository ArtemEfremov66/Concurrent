import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static BlockingQueue<String> sumA = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> sumB = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> sumC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {
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
            String maxA = String.valueOf(0);
            int countA = 0;
            for (int i = 0; i < 10_000; i++) {
                try {
                    int countOld = countWord(maxA, 'a');
                    String newA = sumA.take();
                    if (countOld < countWord(newA, 'a')) {
                        maxA = newA;
                        countA = countWord(maxA, 'a');
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
            System.out.println(countA);
        });
        Runnable logicB = (() -> {
            String maxB = String.valueOf(0);
            int countB = 0;
            for (int i = 0; i < 10_000; i++) {
                try {
                    int countOld = countWord(maxB, 'b');
                    String newB = sumB.take();
                    if (countOld < countWord(newB, 'b')) {
                        maxB = newB;
                        countB = countWord(maxB, 'b');
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
            System.out.println(countB);
        });
        Runnable logicC = (() -> {
            String maxC = String.valueOf(0);
            int countC = 0;
            for (int i = 0; i < 10_000; i++) {
                try {
                    int countOld = countWord(maxC, 'c');
                    String newC = sumC.take();
                    if (countOld < countWord(newC, 'c')) {
                        maxC = newC;
                        countC = countWord(maxC, 'c');
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
            System.out.println(countC);
        });
        Thread threadA = new Thread(logicA);
        Thread threadB = new Thread(logicB);
        Thread threadC = new Thread(logicC);

        threadA.start();
        threadB.start();
        threadC.start();
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
}