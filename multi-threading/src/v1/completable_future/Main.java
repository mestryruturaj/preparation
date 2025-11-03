package v1.completable_future;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + ": thread is executing task to generate a lucky number.");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Some error occurred";
            }

            return "Today's lucky number is " + new Random().nextInt(10);
        }, executor);

        System.out.println(Thread.currentThread().getName() + ": is running asynchronously.");
        try {
            future.thenAccept((result) -> {
                System.out.println("Results are");
                System.out.println(result);
            });
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }


        System.out.println("Shutting down executor service gracefully.");
        executor.shutdown();
    }
}
