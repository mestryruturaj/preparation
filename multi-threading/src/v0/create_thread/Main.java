package v0.create_thread;

import java.sql.SQLOutput;

public class Main {
    public static void main(String[] args) {
        MyThread t0 = new MyThread();
        t0.start();

        MyThreadWithoutRun t1 = new MyThreadWithoutRun(()->{
            System.out.println(MyThreadWithoutRun.class.getSimpleName());
        });
        t1.start();

        Thread t2 = new Thread(new MyRunnable());
        t2.start();
    }
}

class MyThread extends Thread {
    @Override
    public void run() {
        String output = this.getClass().getSimpleName() + ": this message is coming from run() method.";
        System.out.println(output);
    }
}

class MyThreadWithoutRun extends Thread {
    public MyThreadWithoutRun(Runnable runnable) {
        super(runnable);
    }
}

class MyRunnable implements Runnable {
    @Override
    public void run() {
        String output = this.getClass().getSimpleName() + ": this message is coming from run() method.";
        System.out.println(output);
    }
}
