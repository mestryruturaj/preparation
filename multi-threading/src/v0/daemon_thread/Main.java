package v0.daemon_thread;

public class Main {
    public static void main(String[] args) {
        DaemonThread t0 = new DaemonThread();
        DaemonThread t1 = new DaemonThread();
        DaemonThread t2 = new DaemonThread();

        t0.setDaemon(true);
        t1.setDaemon(true);
        t2.setDaemon(false);
        t0.start();
        t1.start();
        t2.start();
    }
}

class DaemonThread extends Thread {
    @Override
    public void run() {
        if (Thread.currentThread().isDaemon()) {
            System.out.println(Thread.currentThread().getName() + ": I am a daemon thread");
        } else {
            System.out.println(Thread.currentThread().getName() + ": I am a non daemon thread");
        }
    }
}
