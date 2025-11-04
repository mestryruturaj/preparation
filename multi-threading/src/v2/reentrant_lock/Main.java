package v2.reentrant_lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        Message message = new Message();
        Producer producer = new Producer(message);
        Consumer consumer = new Consumer(message);
        Runnable producerTask = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    producer.produceMessage("waving at you! x" + (i + 1));
                }
            }
        };
        Runnable consumerTask = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 200; i++) {
                    consumer.consumeMessage();
                }
            }
        };

        Thread producerThread = new Thread(producerTask);
        producerThread.start();
        Thread consumerThread = new Thread(consumerTask);
        consumerThread.start();
    }
}

class Message {
    private String message;
    private boolean hasMessage;
    private ReentrantLock lock;
    private Condition bufferNotEmpty;
    private Condition bufferNotFull;

    public Message() {
        this.lock = new ReentrantLock();
        this.bufferNotEmpty = this.lock.newCondition();
        this.bufferNotFull = this.lock.newCondition();
    }

    public String getMessage() {
        this.hasMessage = false;
        return message;
    }

    public void setMessage(String message) {
        this.hasMessage = true;
        this.message = message;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public Condition getBufferNotEmpty() {
        return bufferNotEmpty;
    }

    public Condition getBufferNotFull() {
        return bufferNotFull;
    }

    public boolean isHasMessage() {
        return hasMessage;
    }
}

class Producer {
    private final Message message;

    public Producer(Message message) {
        this.message = message;
    }

    public void produceMessage(String message) {
        this.message.getLock().lock();
        try {
            while (this.message.isHasMessage()) {
                try {
                    this.message.getBufferNotFull().await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            this.message.setMessage(message);
            this.message.getBufferNotEmpty().signalAll();
        } finally {
            this.message.getLock().unlock();
        }
    }
}

class Consumer {
    private final Message message;

    public Consumer(Message message) {
        this.message = message;
    }

    public void consumeMessage() {
        this.message.getLock().lock();
        try {
            while (!this.message.isHasMessage()) {
                try {
                    System.out.println("Waiting for message to be produced.");
                    this.message.getBufferNotEmpty().await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            String message = this.message.getMessage();
            System.out.println("Consumed message = " + message);
            this.message.getBufferNotFull().signalAll();
        } finally {
            this.message.getLock().unlock();
        }
    }
}
