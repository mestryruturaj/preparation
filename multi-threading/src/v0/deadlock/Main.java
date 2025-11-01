package v0.deadlock;

public class Main {
    public static void main(String[] args) {
        Reader reader = new Reader();
        Writer writer = new Writer();
        Runnable readWriteTask = new Runnable() {
            @Override
            public void run() {
                synchronized (reader) {
                    System.out.println("Reading operation underway...");
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }

                    synchronized (writer) {
                        System.out.println("Writing operation underway");
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }
            }
        };

        Runnable writeReadTask = new Runnable() {
            @Override
            public void run() {
                synchronized (writer) {
                    System.out.println("Writing operation underway...");
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }

                    synchronized (reader) {
                        System.out.println("Reading operation underway");
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }
            }
        };

        Thread t0 = new Thread(readWriteTask);
        Thread t1 = new Thread(writeReadTask);
        t0.start();
        t1.start();
    }
}

class Reader {

}

class Writer {

}
