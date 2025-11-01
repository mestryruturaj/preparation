package v0.completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        try (ExecutorService executorService = Executors.newFixedThreadPool(2)) {
            CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(1234);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
                return "I am a returned value from a completable future.";
            }, executorService);

            completableFuture.thenAccept(result -> System.out.println("Received result now: " + result));
            try {
                completableFuture.join();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
}
