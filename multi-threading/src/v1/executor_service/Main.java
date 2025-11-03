package v1.executor_service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) {
        try (ExecutorService executor = Executors.newSingleThreadExecutor();) {
            Future<Boolean> future = executor.submit(() -> {
                System.out.println("Producing messages");
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return false;
                    }

                    System.out.println("Number is " + (i + 1));
                }

                return true;
            });

            System.out.println(Thread.currentThread().getName() + ": thread runs asynchronously.");

            try {
                System.out.println("I am assuming task executed successfully. is it? " + future.get());
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
