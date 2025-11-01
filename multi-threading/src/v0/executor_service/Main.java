package v0.executor_service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> {
            for (int i = 0; i < 100; i++) {
                Thread.sleep(345);
                System.out.println("Hello, I am posting for the " + (i + 1) + "th time.");
            }

            return "I am don posting.";
        });

        try {
            System.out.println(future.get());
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
        }

        executor.shutdown();
    }
}
