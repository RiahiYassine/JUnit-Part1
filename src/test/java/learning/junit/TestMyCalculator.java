package learning.junit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TestMyCalculator {

    MyCalculator myCalculator;

    @BeforeEach
    void setUpBeforeEach() {
        myCalculator = new MyCalculator();
    }

    @Test
    void testAdd(){
        assertEquals(4 , myCalculator.add(1,3), "Expected 1 + 3 to equal 4");
        assertNotEquals(5 , myCalculator.add(1,3), "Expected 1 + 3 not to equal 5");
    }



}
