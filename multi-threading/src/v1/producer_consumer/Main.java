package v1.producer_consumer;

public class Main {
    public static void main(String[] args) {
        Message message = new Message();
        Runnable consumerTask = new Runnable() {
            @Override
            public void run() {
                Consumer consumer = new Consumer(message);
                for (int i = 0; i < 100; i++) {
                    consumer.consumeMessage();
                }
            }
        };

        Runnable producerTask = new Runnable() {
            @Override
            public void run() {
                Producer producer = new Producer(message);
                for (int i = 0; i < 100; i++) {
                    producer.produceMessage("Current message is " + Thread.currentThread().getName() + "_" + i);
                }
            }
        };

        Thread p0 = new Thread(producerTask,"Producer 0");
        Thread p1 = new Thread(producerTask, "Producer 1");
        Thread p2 = new Thread(producerTask, "Producer 2");
        Thread p3 = new Thread(producerTask, "Producer 3");
        Thread c0 = new Thread(consumerTask,"Consumer 0");
        Thread c1 = new Thread(consumerTask, "Consumer 1");
        p0.start();
        p1.start();
        p2.start();
        p3.start();
        c0.start();
        c1.start();
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

    public String getMessage() {
        this.hasMessage = false;
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        this.hasMessage = true;
    }

    public void toggleHasMessage() {
        this.hasMessage = !this.hasMessage;
    }

    public boolean isHasMessage() {
        return hasMessage;
    }

    public void setHasMessage(boolean hasMessage) {
        this.hasMessage = hasMessage;
    }
}

class Producer {
    private final Message message;

    public Producer(Message message) {
        this.message = message;
    }

    public void produceMessage(String message) {
        while (this.message.isHasMessage()) {
            System.out.println(Thread.currentThread().getName() + ": sleeping until previous message is consumed.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        synchronized (this.message) {
            System.out.println(Thread.currentThread().getName() + " acquired the lock.");

            while (this.message.isHasMessage()) {
                try {
                    System.out.println(Thread.currentThread().getName() + " waiting so released the lock.");
                    this.message.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            this.message.setMessage(message);
            this.message.notifyAll();
            System.out.println(Thread.currentThread().getName() + " released the lock.");
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
            System.out.println(Thread.currentThread().getName() + ": waiting until new message is produced.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        synchronized (this.message) {
            System.out.println(Thread.currentThread().getName() + " acquired the lock.");
            while (!this.message.isHasMessage()) {
                try {
                    System.out.println(Thread.currentThread().getName() + " waiting so released the lock.");
                    this.message.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            String message = this.message.getMessage();
            System.out.println(Thread.currentThread().getName() + ": message consumed = " + message);
            this.message.notifyAll();
            System.out.println(Thread.currentThread().getName() + " released the lock.");
        }
    }
}
