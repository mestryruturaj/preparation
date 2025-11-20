package v0;

import java.util.ArrayList;
import java.util.List;

public class Generics {
    public static void main(String[] args) {
//        List<?> wildCardList = getEntity();
//        for (int i = 0; i < wildCardList.size(); i++) {
//            System.out.println(wildCardList.get(i));
//        }
        List<? extends Number> numbers = getNumbers();
        for (int i = 0; i < numbers.size(); i++) {
            System.out.println(numbers.get(i));
        }

        List<? super Integer> num = new ArrayList<>();
        num.add(1);
        System.out.println(num.get(0));

    }

    private static List<?> getEntity() {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            names.add("name_" + (i + 1));
        }

        return names;
    }

    private static <T extends Number> List<? extends Number> getNumbers() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            numbers.add(i);
        }
        return numbers;
    }


}
