package ex2;

import java.util.Random;

public class DelayUtil {

    public static void delay(int multiplier) {
        Random random = new Random();
        int rand = random.nextInt(5);
        try {
            Thread.sleep(rand * multiplier);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void initialTimeout() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
