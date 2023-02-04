import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        long startTs = System.currentTimeMillis(); // start time
        ExecutorService service = Executors.newFixedThreadPool(10);
        List<Future<Integer>> tasks = new ArrayList<>();
        for (String text : texts) {
            tasks.add(service.submit(() -> {
                int maxSize = 0;
                for (int i = 0; i < text.length(); i++) {
                    for (int j = 0; j < text.length(); j++) {
                        if (i >= j) {
                            continue;
                        }
                        boolean bFound = false;
                        for (int k = i; k < j; k++) {
                            if (text.charAt(k) == 'b') {
                                bFound = true;
                                break;
                            }
                        }
                        if (!bFound && maxSize < j - i) {
                            maxSize = j - i;
                        }
                    }
                }
                return maxSize;
//                System.out.println(text.substring(0, 100) + " -> " + maxSize);
            }));
        }

//        long endTs = System.currentTimeMillis(); // end time

//        System.out.println("Time: " + (endTs - startTs) + "ms");
        int maxTasksSize = 0;
        for (int i = 0; i < tasks.size(); i++) {
            int value = tasks.get(i).get().intValue();
            if (value > maxTasksSize) {
                maxTasksSize = value;
            }
        }
        System.out.println("Максимальный интервал значений среди всех строк: " + maxTasksSize);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
