package v2.producer_consumer;

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
    private boolean hasMessage;

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
    private final Message message;

    public Producer(Message message) {
        this.message = message;
    }

    public void produceMessage(String message) {
        while (this.message.isHasMessage()) {
            try {
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

            System.out.println("[" + Thread.currentThread().getName() + "] Message is produced. " + message);
            this.message.setMessage(message);
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
                Thread.sleep(1000);
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
            System.out.println("[" + Thread.currentThread().getName() + "] Message is consumed. " + message);
            this.message.notifyAll();
        }
    }
}
