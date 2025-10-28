package v0.producer_consumer;

public class Main {
    public static void main(String[] args) {
        Message message = new Message();
        Runnable producerTask = new Runnable() {
            @Override
            public void run() {
                Producer producer = new Producer(message);
                for (int i = 0; i < 100; i++) {
                    String message = "Message no. " + i;
                    producer.produceMessage(message);
                }
            }
        };

        Runnable consumerTask = new Runnable() {
            @Override
            public void run() {
                Consumer consumer = new Consumer(message);
                for (int i = 0; i < 100; i++) {
                    consumer.consumeMessage();
                }
            }
        };

        Thread producerThread = new Thread(producerTask);
        Thread consumerThread1 = new Thread(consumerTask, "consumer_1");
        Thread consumerThread2 = new Thread(consumerTask, "consumer_2");
        producerThread.start();
        consumerThread1.start();
        consumerThread2.start();
    }
}

class Producer {
    private final Message message;

    public Producer(Message message) {
        this.message = message;
    }

    public void produceMessage(String message) {
        while (this.message.isHasMessage()) {
            try {
                System.out.println("Producer is waiting as last message still exist");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        synchronized (this.message) {
            while (this.message.isHasMessage()) {
                try {
                    this.message.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            this.message.setMessage(message);
            System.out.println("Produced Message: " + message);
            this.message.notifyAll();
        }
    }
}

class Consumer {
    private final Message message;

    public Consumer(Message message) {
        this.message = message;
    }

    public void consumeMessage() {
        while (!this.message.isHasMessage()) {
            try {
                System.out.println(Thread.currentThread().getName() + ": Consumer is waiting as no message to read");
                Thread.sleep(400);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        synchronized (this.message) {
            while (!this.message.isHasMessage()) {
                try {
                    this.message.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            String message = this.message.getMessage();
            System.out.println(Thread.currentThread().getName() + ": Consumed Message: " + message);
            this.message.notifyAll();
        }
    }
}

class Message {
    private String message;
    private boolean hasMessage;

    public Message() {
    }

    public Message(String message) {
        this.message = message;
        this.hasMessage = true;
    }

    public boolean isHasMessage() {
        return hasMessage;
    }

    public void setHasMessage(boolean hasMessage) {
        this.hasMessage = hasMessage;
    }

    public void toggleHasMessage() {
        this.hasMessage = !this.hasMessage;
    }

    public String getMessage() {
        this.hasMessage = false;
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        this.hasMessage = true;
    }
}
