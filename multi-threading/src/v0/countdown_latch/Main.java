package v0.countdown_latch;

public class Main {

    //Naive approach
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
}
