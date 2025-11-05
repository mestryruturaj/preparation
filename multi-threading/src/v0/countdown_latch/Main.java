package v0.countdown_latch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        int THREAD_COUNT = 3;
        CountDownLatch countDownLatch = new CountDownLatch(THREAD_COUNT);
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ": thread has started running.");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                } finally {
                    countDownLatch.countDown();
                }
                System.out.println(Thread.currentThread().getName() + ": thread has **completed** running.");
            }
        };

        executorService.submit(task);
        executorService.submit(task);
        executorService.submit(task);

        System.out.println("Main thread executed this line");
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Main thread waiting until all threads complete execution.");

        executorService.shutdown();
    }

    //CountDownLatch approach
    /*
    public static void main(String[] args) {
        int THREAD_COUNT = 3;
        CountDownLatch countDownLatch = new CountDownLatch(THREAD_COUNT);
        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ": thread has running.");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                } finally {
                    System.out.println(Thread.currentThread().getName() + ": thread has **completed**.");
                    countDownLatch.countDown();
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
            System.out.println("All threads are not yet completed.");
            countDownLatch.await();
            System.out.println("All threads are completed.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
    */

    //Better approach
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
    */

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
