package v3.producer_consumer;

import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        Resource resource = new Resource();
        Runnable producerTask = new Runnable() {
            @Override
            public void run() {
                Producer producer = new Producer(resource);
                for (int i = 0; i < 10; i++) {
                    producer.produce(i);
                }

                synchronized (resource) {
                    resource.setProducingCompleted(true);
                    resource.notifyAll();
                }
            }
        };

        Runnable consumerTask = new Runnable() {
            @Override
            public void run() {
                Consumer consumer = new Consumer(resource);
                for (int i = 0; i < 10; i++) {
                    consumer.consume();
                }
            }
        };

        Thread p0 = new Thread(producerTask, "p0");
        Thread c0 = new Thread(consumerTask, "c0");
        Thread c1 = new Thread(consumerTask, "c1");

        p0.start();
        c0.start();
        c1.start();

        try {
            p0.join();
            c0.join();
            c1.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Execution completed.");
    }
}

//Producer produces to a list
//Consumer consumes from a list when the size is >= 2 otherwise does not consume

class Resource {
    private Queue<Integer> nums;
    private volatile boolean isProducingCompleted;


    public Resource() {
        this.nums = new LinkedList<>();
        this.isProducingCompleted = false;
    }

    public void produce(int num) {
        nums.add(num);
    }

    public int consume() {
        if (nums.isEmpty()) {
            return -1;
        }
        return nums.poll();
    }

    public int getSize() {
        return this.nums.size();
    }

    public boolean isProducingCompleted() {
        return isProducingCompleted;
    }

    public void setProducingCompleted(boolean producingCompleted) {
        isProducingCompleted = producingCompleted;
    }
}

class Producer {
    private final Resource resource;

    public Producer(Resource resource) {
        this.resource = resource;
    }

    public void produce(int num) {
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        synchronized (this.resource) {
            System.out.println(Thread.currentThread().getName() + " has produced a message.");
            resource.produce(num);
            this.resource.notifyAll();
        }
    }
}

class Consumer {
    private final Resource resource;

    public Consumer(Resource resource) {
        this.resource = resource;
    }

    public void consume() {
        int consumedNum;
        synchronized (this.resource) {
            while (this.resource.getSize() < 2 && !this.resource.isProducingCompleted()) {
                try {
                    System.out.println(Thread.currentThread().getName() + " thread is waiting for consume condition to be met.");
                    this.resource.wait();
                } catch (InterruptedException e) {
                    System.out.println("Something went wrong while waiting.");
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }

            if (this.resource.getSize() > 0) {
                consumedNum = this.resource.consume();
            } else {
                return;
            }
        }

        System.out.println(Thread.currentThread().getName() + " consumed number: " + consumedNum);
    }
}
