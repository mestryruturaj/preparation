package v0.thread_local;

public class Main {
    public static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                threadLocal.set((int) (Math.random() * 100 * 43));
                try {
                    Thread.sleep((long) (Math.random() * 100 * 54));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println(Thread.currentThread().getName() + ": " + threadLocal.get());
            }
        };

        Thread t0 = new Thread(task);
        Thread t1 = new Thread(task);

        t0.start();
        t1.start();
    }
}
