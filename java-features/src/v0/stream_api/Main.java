package v0.stream_api;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
//        List<Integer> nums = List.of(1, 2, 3, 4, 5, 11, 12, 14, 11, 111, 112);
//        System.out.println("Printing");
//        printEven(nums);
//        System.out.println("Extracting");
//        List<Integer> evenNums = extractEven(nums);
//        evenNums.forEach(num -> System.out.println(num));
//
//        System.out.println("Starting with x");
//
//        extractNumbersStartingWithX(nums, 11)
//                .forEach(num -> System.out.println(num));

//        findFirstNonRepeatingCharacter();
        long distinct = countUniqueFruits(List.of("Apple", "Banana", "Apple", "Apple", "Apple", "Orange", "Banana"));
        System.out.println(distinct);
    }

    public static void printEven(List<Integer> nums) {
        nums.stream()
                .filter(num -> num % 2 == 0)
                .forEach(System.out::println);
    }

    public static List<Integer> extractEven(List<Integer> nums) {
        return nums.stream()
                .filter(num -> num % 2 == 0)
                .collect(Collectors.toList());
    }

    public static List<Integer> extractNumbersStartingWithX(List<Integer> nums, int x) {
        return nums.stream()
                .filter(num -> String.valueOf(num).startsWith(String.valueOf(x)))
                .collect(Collectors.toList());
    }

    public static void findFirstNonRepeatingCharacter() {
        String str = "Hello world! How are you? We welcome you to planet X.";
        Character firstUniqueCharacter = str.chars()
                .mapToObj(ch -> Character.toLowerCase(Character.valueOf((char) ch)))
                .collect(Collectors.groupingBy(Function.identity(), () -> new LinkedHashMap<Character, Long>(), Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() == 1L)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Every character is repeated."))
                .getKey();

        System.out.println(firstUniqueCharacter);
    }

    public static long countUniqueFruits(List<String> fruits) {
        return fruits.stream().distinct().count();
    }

}
