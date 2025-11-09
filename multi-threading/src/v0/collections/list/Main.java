package v0.collections.list;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    public static void main(String[] args) {
//        runCopyOnWriteArrayListDemo();
    }

    private static void runCopyOnWriteArrayListDemo() {
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            list.add(i);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time required to modify the thread safe list is " + (endTime - startTime));

        startTime = System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {
            list.get(i);
        }
        endTime = System.currentTimeMillis();
        System.out.println("Time required to read from thread safe list is " + (endTime - startTime));


        ArrayList<Integer> list1 = new ArrayList<>();
        startTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            list1.add(i);
        }
        endTime = System.currentTimeMillis();
        System.out.println("Time required to modify the non thread safe list1 is " + (endTime - startTime));

        startTime = System.currentTimeMillis();
        for (int i = 0; i < list1.size(); i++) {
            list1.get(i);
        }
        endTime = System.currentTimeMillis();
        System.out.println("Time required to read from non thread safe list1 is " + (endTime - startTime));

    }

    private static void runVectorDemo() {
        Vector<Integer> vector = new Vector<>();
        vector.add(10);
        vector.get(0);
    }
}
