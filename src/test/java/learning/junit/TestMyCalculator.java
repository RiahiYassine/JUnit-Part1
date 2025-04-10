package learning.junit;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Test suite for MyCalculator class demonstrating JUnit 5 features.
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@Tag("calculator")
class TestMyCalculator {

    private MyCalculator calculator;

    @BeforeAll
    static void setUpTestSuite() {
        System.out.println("Initializing test suite for MyCalculator");
    }

    @AfterAll
    static void tearDownTestSuite() {
        System.out.println("Completed test suite for MyCalculator");
    }

    @BeforeEach
    void initializeCalculator() {
        calculator = new MyCalculator();
    }

    @AfterEach
    void cleanupAfterTest() {
        System.out.println("Test completed, cleaning up resources");
    }

    // ========== Basic Arithmetic Operations ==========

    @Nested
    @DisplayName("Basic Arithmetic")
    @Tag("arithmetic")
    class ArithmeticOperations {

        @ParameterizedTest(name = "{0} + {1} = {2}")
        @CsvSource({
                "0, 0, 0",
                "5, 7, 12",
                "-2, 2, 0",
                "-10, -5, -15",
                "1000, 2000, 3000"
        })
        @DisplayName("add() correctly sums integers")
        void add_WithValidInputs_ReturnsSumCorrectly(int operand1, int operand2, int expectedSum) {
            int actualSum = calculator.add(operand1, operand2);
            assertEquals(expectedSum, actualSum,
                    () -> "Expected %d + %d = %d, but got %d".formatted(operand1, operand2, expectedSum, actualSum));
        }

        @Test
        @DisplayName("add() with MAX_VALUE shows expected overflow")
        void add_WithIntegerOverflow_ReturnsNegativeValue() {
            int actualSum = calculator.add(Integer.MAX_VALUE, 1);
            assertTrue(actualSum < 0,
                    "Integer overflow should wrap to negative value when adding beyond MAX_VALUE");
        }

        @Test
        @DisplayName("add() with MIN_VALUE shows expected underflow")
        void add_WithIntegerUnderflow_ReturnsPositiveValue() {
            int actualSum = calculator.add(Integer.MIN_VALUE, -1);
            assertTrue(actualSum > 0,
                    "Integer underflow should wrap to positive value when adding below MIN_VALUE");
        }
    }

    // ========== Number Property Operations ==========

    @Nested
    @DisplayName("Number Properties")
    @Tag("properties")
    class NumberProperties {

        @ParameterizedTest(name = "isEven({0}) = {1}")
        @CsvSource({
                "2, true",
                "3, false",
                "0, true",
                "-4, true",
                "-3, false",
                "2147483647, false",  // MAX_VALUE
                "-2147483648, true"   // MIN_VALUE
        })
        @DisplayName("isEven() correctly identifies even/odd numbers")
        void isEven_WithDifferentInputs_IdentifiesEvenness(int input, boolean expected) {
            boolean actual = calculator.isEven(input);
            assertEquals(expected, actual,
                    () -> "Expected isEven(%d) to be %b".formatted(input, expected));
        }
    }

    // ========== Collection and Data Structure Tests ==========

    @Nested
    @DisplayName("Collections & Data Structures")
    @Tag("collections")
    class CollectionOperations {

        @Test
        @DisplayName("getArray() returns expected array")
        void getArray_WhenCalled_ReturnsExpectedElements() {
            int[] expected = {1, 2, 3, 4, 5};
            int[] actual = calculator.getArray();
            assertArrayEquals(expected, actual,
                    "Array elements should match the expected values in the correct order");
        }

        @Test
        @DisplayName("getIterable() returns expected elements")
        void getIterable_WhenCalled_ReturnsExpectedElements() {
            List<Integer> expected = List.of(1, 2, 3, 4, 5);
            Iterable<Integer> actual = calculator.getIterable();
            assertIterableEquals(expected, actual,
                    "Iterable elements should match the expected values in the correct order");
        }

        @Test
        @DisplayName("getLines() returns expected text lines")
        void getLines_WhenCalled_ReturnsExpectedLines() {
            List<String> expected = List.of("first line", "second line", "third line");
            List<String> actual = calculator.getLines();
            assertLinesMatch(expected, actual,
                    "Text lines should match the expected content exactly");
        }
    }

    // ========== Instance Management Tests ==========

    @Nested
    @DisplayName("Instance Management")
    @Tag("instances")
    class InstanceManagement {

        @Test
        @DisplayName("getInstance() returns consistent singleton instance")
        void getInstance_CalledMultipleTimes_ReturnsSameInstance() {
            MyCalculator firstInstance = calculator.getInstance();
            MyCalculator secondInstance = calculator.getInstance();
            assertSame(firstInstance, secondInstance,
                    "Multiple calls to getInstance() should return the same object reference");
        }

        @Test
        @DisplayName("getNewInstance() returns unique instances")
        void getNewInstance_WhenCalled_ReturnsDistinctInstance() {
            MyCalculator singletonInstance = calculator.getInstance();
            MyCalculator newInstance = calculator.getNewInstance();
            assertNotSame(singletonInstance, newInstance,
                    "getNewInstance() should return a different object reference than getInstance()");
        }
    }

    // ========== Performance Tests ==========

    @Nested
    @DisplayName("Performance Characteristics")
    @Tag("performance")
    class PerformanceTests {

        @Test
        @DisplayName("delayOperation() completes within time limit")
        void delayOperation_WhenCalled_CompletesWithinTimeLimit() {
            assertTimeoutPreemptively(
                    Duration.ofSeconds(1),
                    () -> calculator.delayOperation(),
                    "Operation should complete within the specified time limit"
            );
        }
    }

    // ========== Conditional Tests ==========

    @Nested
    @DisplayName("Environment-Specific Tests")
    @Tag("conditional")
    class ConditionalTests {

        @Test
        @EnabledOnOs(OS.WINDOWS)
        @DisplayName("Windows-specific operation executes successfully")
        void windowsSpecificOperation_OnWindowsOs_ExecutesSuccessfully() {
            assertTrue(true, "This test verifies functionality specific to Windows operating systems");
        }

        @Test
        @EnabledOnJre(JRE.JAVA_17)
        @DisplayName("Java 17-specific operation executes successfully")
        void java17SpecificOperation_OnJava17_ExecutesSuccessfully() {
            assertTrue(true, "This test verifies functionality specific to Java 17 runtime");
        }

        @Test
        @EnabledIfSystemProperty(named = "deployment.mode", matches = "ci-cd")
        @DisplayName("CI/CD-specific operation executes in pipeline")
        void cicdSpecificOperation_InCicdEnvironment_ExecutesSuccessfully() {
            assumeTrue(System.getProperty("deployment.mode").equals("ci-cd"),
                    "This test only runs in CI/CD environment");
            assertTrue(true, "This test verifies functionality specific to CI/CD deployment mode");
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "APP_ENV", matches = "dev")
        @DisplayName("Development-specific operation executes in dev environment")
        void devSpecificOperation_InDevEnvironment_ExecutesSuccessfully() {
            assumeTrue(System.getenv("APP_ENV").equals("dev"),
                    "This test only runs in development environment");
            assertTrue(true, "This test verifies functionality specific to development environment");
        }

        @Test
        @Disabled("Intentionally disabled to demonstrate @Disabled annotation")
        @DisplayName("Disabled test - should be skipped")
        void intentionallyDisabled_WhenExecuted_ShouldFail() {
            fail("This test should not execute because it is marked as @Disabled");
        }
    }

    // ========== GCD Algorithm Tests ==========

    @Nested
    @DisplayName("Greatest Common Divisor - Valid Cases")
    @Tag("gcd")
    @Tag("valid")
    class GcdValidTests {

        @ParameterizedTest(name = "gcd({0}, {1}) = {2}")
        @CsvSource({
                "8, 12, 4",
                "0, 5, 5",
                "5, 0, 5",
                "1, 1, 1",
                "10, 5, 5",
                "12, 15, 3",
                "81, 153, 9",
                "14, 49, 7",
                "100, 25, 25",
                "27, 36, 9"
        })
        @DisplayName("gcd() with valid inputs returns correct result")
        void gcd_WithValidInputs_ReturnsCorrectValue(int a, int b, int expected) {
            int actual = assertDoesNotThrow(
                    () -> calculator.gcd(a, b),
                    "gcd() should not throw exception for valid inputs"
            );
            assertEquals(expected, actual,
                    () -> "gcd(%d, %d) should be %d, but was %d".formatted(a, b, expected, actual));
        }

        @Test
        @DisplayName("gcd() with large numbers returns correct result")
        void gcd_WithLargeNumbers_CalculatesCorrectly() {
            int a = 1_234_567_890;
            int b = 987_654_321;
            int expected = 9;

            int actual = assertDoesNotThrow(
                    () -> calculator.gcd(a, b),
                    "gcd() should handle large integers without exceptions"
            );
            assertEquals(expected, actual,
                    () -> "gcd(%d, %d) should be %d, but was %d".formatted(a, b, expected, actual));
        }

        @ParameterizedTest(name = "gcd({0}, {1}) = gcd({1}, {0})")
        @CsvSource({
                "8, 12",
                "0, 5",
                "5, 0",
                "10, 15",
                "27, 36",
                "14, 49"
        })
        @DisplayName("gcd() is commutative")
        void gcd_WithParametersReversed_ReturnsSameResult(int a, int b) {
            int result1 = calculator.gcd(a, b);
            int result2 = calculator.gcd(b, a);
            assertEquals(result1, result2,
                    () -> "gcd(%d, %d) should equal gcd(%d, %d) due to commutativity".formatted(a, b, b, a));
        }

        @Test
        @DisplayName("gcd() satisfies divisibility property")
        void gcd_WhenCalculated_SatisfiesDivisibilityProperty() {
            int a = 48;
            int b = 18;
            int gcdValue = calculator.gcd(a, b);

            assertEquals(0, a % gcdValue,
                    "GCD must be a divisor of the first number");
            assertEquals(0, b % gcdValue,
                    "GCD must be a divisor of the second number");
        }
    }

    @Nested
    @DisplayName("Greatest Common Divisor - Invalid Cases")
    @Tag("gcd")
    @Tag("invalid")
    class GcdInvalidTests {

        @Test
        @DisplayName("gcd(0, 0) throws IllegalArgumentException")
        void gcd_WithBothInputsZero_ThrowsException() {
            Exception exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> calculator.gcd(0, 0),
                    "gcd(0, 0) must throw IllegalArgumentException"
            );

            assertTrue(exception.getMessage().contains("undefined"),
                    "Exception message should explain that GCD of 0 and 0 is undefined");
        }

        @ParameterizedTest(name = "gcd({0}, {1}) throws exception")
        @CsvSource({
                "-3, 1",
                "3, -1",
                "-3, -1"
        })
        @DisplayName("gcd() with negative inputs throws IllegalArgumentException")
        void gcd_WithNegativeInputs_ThrowsException(int a, int b) {
            Exception exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> calculator.gcd(a, b),
                    () -> "gcd(%d, %d) must throw IllegalArgumentException".formatted(a, b)
            );

            assertTrue(exception.getMessage().contains("negative"),
                    "Exception message should explain that negative inputs are not allowed");
        }
    }
}