package v0.creational.singleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        Runnable task = () -> {
            Singleton singletonInstance = Singleton.getInstance();
            System.out.println(singletonInstance);
        };

        try (ExecutorService executorService = Executors.newFixedThreadPool(23)) {
            for (int i = 0; i < 100; i++) {
                executorService.submit(task);
            }
        }
    }
}


class Singleton {
    private static volatile Singleton instance;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }

        return instance;
    }
}
