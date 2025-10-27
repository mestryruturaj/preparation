package v0.shared_counter;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    /**
     * Increment the counter of the shared resource.
     * Counter must match the number of times the count has increased
     * Requirement:
     * 1. Shared resource
     * 2. Synchronization
     * 3. Multiple threads accessing the resource
     * @param args
     */
    public static void main(String[] args) {
//        SynchronizedCounter counter = new SynchronizedCounter(0);
        AtomicCounter counter = new AtomicCounter(0);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    counter.increment();
                }
            }
        };

        Thread t0 = new Thread(task);
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t0.start();
        t1.start();
        t2.start();

        try {
            t0.join();
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Exception occurred during the thread execution");
        }

        System.out.println(counter.getCounter());
    }


}

class SynchronizedCounter {
    private int counter;

    public SynchronizedCounter(int counter) {
        this.counter = counter;
    }

    public synchronized void increment() {
        this.counter++;
    }

    public int getCounter() {
        return this.counter;
    }
}

class AtomicCounter {
    private final AtomicInteger counter;

    public AtomicCounter(int counter) {
        this.counter = new AtomicInteger(counter);
    }

    public void increment() {
        this.counter.incrementAndGet();
    }

    public int getCounter() {
        return this.counter.get();
    }
}
