package v0.semaphore;

import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        int NUM_THREADS = 10;
        int NUM_PERMITS = 3;
        Semaphore semaphore = new Semaphore(NUM_PERMITS);

        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(new Worker(semaphore));
            threads[i].start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}

class Worker implements Runnable {
    private final Semaphore semaphore;

    public Worker(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            System.out.println(Thread.currentThread().getName() + ": has acquired semaphore. available permits are " + semaphore.availablePermits());
            Thread.sleep(1111);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        } finally {
            semaphore.release();
            System.out.println(Thread.currentThread().getName() + ": has released semaphore. available permits are " + semaphore.availablePermits());
        }
    }
}
