package v0.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        int THREAD_COUNT = 3;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(THREAD_COUNT, new Runnable() {
            @Override
            public void run() {
                System.out.println("All the threads have it's barriers.");
            }
        });

        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ": has started running.");
                try {
                    Thread.sleep(1234);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                try {
                    Thread.sleep(1202);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                System.out.println(Thread.currentThread().getName() + ": has **completed** running.");
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        executorService.submit(task);
        executorService.submit(task);
        executorService.submit(task);

        executorService.shutdown();
    }
}
