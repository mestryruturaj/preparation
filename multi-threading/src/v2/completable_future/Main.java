package v2.completable_future;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Generating a random number.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
            return new Random().nextInt(10);
        }, executor);

        System.out.println(Thread.currentThread().getName() + ": is executing a asynchronously.");
        future.thenAccept((result) -> {
            System.out.println("Random number is here.");
            System.out.println(result);
        });

        System.out.println("Shutting down executor service.");
        executor.shutdown();
    }
}
