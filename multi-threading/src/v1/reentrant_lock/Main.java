package v1.reentrant_lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        Message message = new Message();
        Runnable producerTask = new Runnable() {
            @Override
            public void run() {
                Producer producer = new Producer(message);
                for (int i = 0; i < 10; i++) {
                    producer.produceMessage("Waving at you for the " + (i + 1) + "th time.");
                }
            }
        };

        Runnable consumerTask = new Runnable() {
            @Override
            public void run() {
                Consumer consumer = new Consumer(message);
                for (int i = 0; i < 10; i++) {
                    consumer.consumeMessage();
                }
            }
        };

        Thread producer = new Thread(producerTask);
        Thread consumer = new Thread(consumerTask);
        producer.start();
        consumer.start();
    }
}

class Message {
    private String message;
    private volatile boolean hasMessage;
    private final ReentrantLock lock;
    private final Condition bufferNotFull;
    private final Condition bufferNotEmpty;

    public Message() {
        this.lock = new ReentrantLock();
        this.bufferNotEmpty = this.lock.newCondition();
        this.bufferNotFull = this.lock.newCondition();
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public Condition getBufferNotFull() {
        return bufferNotFull;
    }

    public Condition getBufferNotEmpty() {
        return bufferNotEmpty;
    }

    public String getMessage() {
        this.hasMessage = false;
        return message;
    }

    public void setMessage(String message) {
        this.hasMessage = true;
        this.message = message;
    }

    public boolean isHasMessage() {
        return hasMessage;
    }

    public void setHasMessage(boolean hasMessage) {
        this.hasMessage = hasMessage;
    }
}

class Producer {
    private Message message;

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
    private Message message;

    public Consumer(Message message) {
        this.message = message;
    }

    public void consumeMessage() {
        this.message.getLock().lock();
        try {
            while (!this.message.isHasMessage()) {
                try {
                    this.message.getBufferNotEmpty().await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            String message = this.message.getMessage();
            System.out.println("[" + Thread.currentThread().getName() + "] consumed message = " + message);
            this.message.getBufferNotFull().signalAll();
        } finally {
            this.message.getLock().unlock();
        }
    }
}
