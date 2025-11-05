package v0.countdown_latch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    //Better approach
    public static void main(String[] args) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ": has started running.");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                System.out.println(Thread.currentThread().getName() + ": has **completed** running.");
            }
        };

        try (ExecutorService executorService = Executors.newFixedThreadPool(3)) {
            List<Callable<Object>> callables = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                Callable<Object> callable = Executors.callable(task);
                callables.add(callable);
            }

            System.out.println("Three tasks have been submitted.");
            List<Future<Object>> futures = new ArrayList<>();
            try {
                futures.addAll(executorService.invokeAll(callables));
                System.out.println("All the threads are completed either by returning or throwing an exception.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            for (Future<Object> f : futures) {
                try {
                    System.out.println(f.get());
                } catch (InterruptedException | ExecutionException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    //Naive approach
    /*
    public static void main(String[] args) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ": has started running.");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                System.out.println(Thread.currentThread().getName() + ": has **completed** running.");
            }
        };

        Thread t0 = new Thread(task);
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t0.start();
        t1.start();
        t2.start();

        System.out.println("This part of Main thread is still running...");
        try {
            t0.join();
            t1.join();
            t2.join();

            System.out.println("Thread completed running");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("This part of Main thread had to wait until the thread completed running...");
    }
    */
}
