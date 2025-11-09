package v0.collections.map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        ConcurrentHashMap<Integer, String> map = new ConcurrentHashMap<>();
        Thread writer0 = new Thread(new Writer(map));
        Thread writer1 = new Thread(new Writer(map));
        Thread reader0 = new Thread(new Reader(map));
        Thread reader1 = new Thread(new Reader(map));

        writer0.start();
        writer1.start();
        reader0.start();
        reader1.start();
    }
}

class Writer implements Runnable {
    private ConcurrentHashMap<Integer, String> map;

    public Writer(ConcurrentHashMap<Integer, String> map) {
        this.map = map;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            Number number = Number.findByValue(i);
            this.map.put(i, number.name());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

class Reader implements Runnable {
    private ConcurrentHashMap<Integer, String> map;

    public Reader(ConcurrentHashMap<Integer, String> map) {
        this.map = map;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(Thread.currentThread().getName() + ": I am ready to read");

            for (Map.Entry<Integer, String> entry : this.map.entrySet()) {
                int k = entry.getKey();
                String v = entry.getValue();

                System.out.println(Thread.currentThread().getName() + ": k = " + k + ", v = " + v);

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}

enum Number {
    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10);

    public final int value;

    private Number(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static Number findByValue(int value) {
        for (Number number : Number.values()) {
            if (number.getValue() == value) {
                return number;
            }
        }

        throw new RuntimeException("Invalid Number");
    }
}