package learning.junit;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Generate display names by replacing underscores in method names.
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
// Order tests by their display names.
@TestMethodOrder(MethodOrderer.DisplayName.class)
class TestMyCalculator {

    private MyCalculator calculator;

    @BeforeAll
    static void beforeAllTests() {
        System.out.println("Before all tests");
    }

    @AfterAll
    static void afterAllTests() {
        System.out.println("After all tests");
    }

    @BeforeEach
    void setUp() {
        calculator = new MyCalculator();
    }

    @AfterEach
    void tearDown() {
        System.out.println("After each test");
    }

    // --- Assertion Examples ---

    // assertEquals & assertNotEquals
    @Test
    @DisplayName("Addition: assertEquals and assertNotEquals")
    void testAdd() {
        int result = calculator.add(1, 3);
        assertEquals(4, result, "Expected 1 + 3 to equal 4");
        assertNotEquals(5, result, "Expected 1 + 3 not to equal 5");
    }

    // assertNull & assertNotNull
    @Test
    @DisplayName("Null check: assertNull and assertNotNull")
    void testNullAndNotNull() {
        Object obj = null;
        assertNull(obj, "Object should be null");
        obj = new Object();
        assertNotNull(obj, "Object should not be null");
    }

    // assertSame & assertNotSame
    @Test
    @DisplayName("Instance identity: assertSame and assertNotSame")
    void testSameNotSame() {
        MyCalculator instance1 = calculator.getInstance();
        MyCalculator instance2 = calculator.getInstance();
        assertSame(instance1, instance2, "Expected the same instance from getInstance");
        MyCalculator instance3 = calculator.getNewInstance();
        assertNotSame(instance1, instance3, "Expected different instances from getInstance and getNewInstance");
    }

    // assertTrue & assertFalse
    @Test
    @DisplayName("Boolean conditions: assertTrue and assertFalse")
    void testBooleanConditions() {
        assertTrue(calculator.isEven(4), "4 should be even");
        assertFalse(calculator.isEven(5), "5 should be odd");
    }

    // assertArrayEquals
    @Test
    @DisplayName("Array equality: assertArrayEquals")
    void testArrayEquals() {
        int[] expected = {1, 2, 3, 4, 5};
        assertArrayEquals(expected, calculator.getArray(), "The arrays should match");
    }

    // assertIterableEquals
    @Test
    @DisplayName("Iterable equality: assertIterableEquals")
    void testIterableEquals() {
        List<Integer> expected = List.of(1, 2, 3, 4, 5);
        assertIterableEquals(expected, calculator.getIterable(), "The iterables should match");
    }

    // assertLinesMatch
    @Test
    @DisplayName("Lines match: assertLinesMatch")
    void testLinesMatch() {
        List<String> expectedLines = List.of("first line", "second line", "third line");
        assertLinesMatch(expectedLines, calculator.getLines(), "Lines should match");
    }

    // assertThrows
    @Test
    @DisplayName("Division by zero: assertThrows")
    void testThrows() {
        assertThrows(ArithmeticException.class, () -> calculator.divide(10, 0), "Division by zero should throw ArithmeticException");
    }

    // assertDoesNotThrow
    @Test
    @DisplayName("Method does nothing: assertDoesNotThrow")
    void testDoesNotThrow() {
        assertDoesNotThrow(() -> calculator.doNothing(), "Method should not throw any exception");
    }

    // assertTimeoutPreemptively
    @Test
    @DisplayName("Delay operation within timeout: assertTimeoutPreemptively")
    void testTimeoutPreemptively() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> calculator.delayOperation(), "Method should complete within 1 second");
    }

    // ParameterizedTest: using multiple values
    @ParameterizedTest
    @ValueSource(ints = {0, 5, 10})
    @DisplayName("Parameterized add test: adding 0 should return the same number")
    void testAddParameterized(int number) {
        assertEquals(number, calculator.add(number, 0), "Adding 0 should return the same number");
    }

    // Conditional: Only run this test on Windows OS
    @Test
    @EnabledOnOs(OS.WINDOWS)
    @DisplayName("Test only on Windows OS")
    void testOnlyOnWindows() {
        assertTrue(true, "This test runs only on Windows");
    }

    // Conditional: Only run this test on a specified Java version
    @Test
    @EnabledOnJre(JRE.JAVA_17)
    @DisplayName("Test only on Java 17")
    void testOnlyOnJava17() {
        assertTrue(true, "This test runs only on Java 17");
    }

    // Conditional: Enable test if system property is set
    @Test
    @EnabledIfSystemProperty(named = "deployment.mode", matches = "ci-cd")
    @DisplayName("Test enabled on specific system property")
    void testEnabledIfSystemProperty() {
        assertTrue(true, "This test runs because the system property matches");
    }

    // Conditional: Enable test if environment variable is set
    @Test
    @EnabledIfEnvironmentVariable(named = "APP_ENV", matches = "dev")
    @DisplayName("Test enabled on specific environment variable")
    void testEnabledIfEnvironmentVariable() {
        assertTrue(true, "This test runs because the environment variable matches");
    }

    // Disabled test example
    @Test
    @Disabled("Example of a disabled test")
    @DisplayName("Disabled test example")
    void disabledTestExample() {
        fail("This test should be disabled");
    }
}
