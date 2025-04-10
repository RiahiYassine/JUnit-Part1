package learning.junit;

import java.util.List;

public class MyCalculator {

    // Addition operation
    public int add(int a, int b) {
        return a + b;
    }

    // Returns true if the number is even, false otherwise
    public boolean isEven(int number) {
        return number % 2 == 0;
    }

    public int gcd(int a, int b) {
        // Validate inputs: only allow non-negative integers, and exclude the (0, 0) case.
        if (a < 0 || b < 0) {
            throw new IllegalArgumentException("Only non-negative integers are allowed.");
        }
        if (a == 0 && b == 0) {
            throw new IllegalArgumentException("GCD is undefined when both inputs are zero.");
        }

        // Apply the Euclidean algorithm.
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // Returns the same instance to test assertSame
    public MyCalculator getInstance() {
        return this;
    }

    // Returns a new instance to test assertNotSame
    public MyCalculator getNewInstance() {
        return new MyCalculator();
    }

    // A method that simulates a delay for assertTimeoutPreemptively
    public void delayOperation() {
        try {
            Thread.sleep(500); // Sleeps for 500 milliseconds.
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Returns an array for testing assertArrayEquals
    public int[] getArray() {
        return new int[] {1, 2, 3, 4, 5};
    }

    // Returns an Iterable for testing assertIterableEquals
    public Iterable<Integer> getIterable() {
        return List.of(1, 2, 3, 4, 5);
    }

    // Returns a list of lines for testing assertLinesMatch
    public List<String> getLines() {
        return List.of("first line", "second line", "third line");
    }

}