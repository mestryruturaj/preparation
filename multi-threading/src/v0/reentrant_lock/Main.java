package v0.reentrant_lock;

import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        SharedResource sharedResource = new SharedResource();
        ReentrantLock lock = new ReentrantLock();
        int threadCount = 3;
        int iterations = 33;
        Task task = new Task(sharedResource, lock, iterations);


        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(task);
            threads[i].start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


}

class Task implements Runnable {
    private final SharedResource sharedResource;
    private final ReentrantLock lock;
    private final int iterations;

    public Task(SharedResource sharedResource, ReentrantLock lock, int iterations) {
        this.sharedResource = sharedResource;
        this.lock = lock;
        this.iterations = iterations;
    }

    @Override
    public void run() {
        for (int i = 0; i < iterations; i++) {
            lock.lock();
            try {
                sharedResource.doWork(i);
            } finally {
                lock.unlock();
            }
        }
    }
}

class SharedResource {
    public void doWork(int i) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " is doing some work for the i = " + i);
    }
}
