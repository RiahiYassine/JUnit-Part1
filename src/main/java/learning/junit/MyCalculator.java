package learning.junit;

import java.time.Duration;
import java.util.List;

public class MyCalculator {

    // Addition operation
    public int add(int a, int b) {
        return a + b;
    }

    // Subtraction operation
    public int subtract(int a, int b) {
        return a - b;
    }

    // Multiplication operation
    public int multiply(int a, int b) {
        return a * b;
    }

    // Division operation (throws ArithmeticException on divide by zero)
    public int divide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return a / b;
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

    // Returns the same instance to test assertSame
    public MyCalculator getInstance() {
        return this;
    }

    // Returns a new instance to test assertNotSame
    public MyCalculator getNewInstance() {
        return new MyCalculator();
    }

    // Returns true if the number is even, false otherwise
    public boolean isEven(int number) {
        return number % 2 == 0;
    }

    // A method that does nothing to test assertDoesNotThrow
    public void doNothing() { }

    // A method that simulates a delay for assertTimeoutPreemptively
    public void delayOperation() {
        try {
            Thread.sleep(500); // Sleeps for 500 milliseconds.
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}