import com.rocketbunny.exception.PNumberException;
import com.rocketbunny.model.TPNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TPNumberTest {

    private TPNumber num1;
    private TPNumber num2;

    @BeforeEach
    void setUp() {
        num1 = new TPNumber(10.5, 10, 2);
        num2 = new TPNumber("A.F", "16", "1");
    }

    @Test
    @DisplayName("Test constructor with double and validation")
    void testConstructorDouble() {
        TPNumber valid = new TPNumber(5.0, 8, 3);
        assertEquals(5.0, valid.getNumber(), "Value should match");

        assertThrows(PNumberException.class, () -> new TPNumber(1.0, 1, 0));
        assertThrows(PNumberException.class, () -> new TPNumber(1.0, 17, 0));

        assertThrows(PNumberException.class, () -> new TPNumber(1.0, 10, -1));
    }

    @Test
    @DisplayName("Test constructor with string")
    void testConstructorString() {
        TPNumber valid = new TPNumber("2", "3", "2");
        assertEquals(2.0, valid.getNumber(), 0.001, "Value should be 2.0");

        TPNumber valid20 = new TPNumber("20", "3", "2");
        assertEquals(6.0, valid20.getNumber(), 0.001, "Value should be 6.0");

        assertThrows(PNumberException.class, () -> new TPNumber("1G", "16", "0"));
        assertThrows(PNumberException.class, () -> new TPNumber("1.2.3", "10", "0"));
    }

    @Test
    @DisplayName("Test arithmetic operations")
    void testArithmeticOperations() {
        num2.setBase(10);
        num2.setPrecision(2);

        TPNumber sum = num1.add(num2);
        assertEquals(21.4375, sum.getNumber(), 0.001, "Sum should be correct");

        TPNumber product = num1.multiply(num2);
        assertEquals(114.84375, product.getNumber(), 0.001, "Product should be correct");

        TPNumber diff = num1.subtract(num2);
        assertEquals(-0.4375, diff.getNumber(), 0.001, "Difference should be correct");

        TPNumber quotient = num1.divide(num2);
        assertEquals(0.96, quotient.getNumber(), 0.01, "Quotient should be correct");

        TPNumber inv = num1.inverse();
        assertEquals(1 / 10.5, inv.getNumber(), 0.001, "Inverse should be correct");

        TPNumber square = num1.square();
        assertEquals(110.25, square.getNumber(), 0.001, "Square should be correct");
    }

    @Test
    @DisplayName("Test exceptions in arithmetic")
    void testArithmeticExceptions() {
        TPNumber zero = new TPNumber(0.0, 10, 2);
        assertThrows(PNumberException.class, () -> zero.inverse(), "Inverse of zero should throw");
        assertThrows(PNumberException.class, () -> num1.divide(zero), "Division by zero should throw");
    }

    @Test
    @DisplayName("Test copy method")
    void testCopy() {
        TPNumber copy = num1.copy();
        assertEquals(num1.getNumber(), copy.getNumber(), "Copied value should match");
        assertEquals(num1.getBase(), copy.getBase(), "Copied base should match");
        assertEquals(num1.getPrecision(), copy.getPrecision(), "Copied precision should match");
    }

    @Test
    @DisplayName("Test getters and setters")
    void testGettersAndSetters() {
        assertEquals(10.5, num1.getNumber(), 0.001, "Get number should match");
        assertEquals("10", num1.getBaseAsString(), "Get base as string should match");
        assertEquals("2", num1.getPrecisionAsString(), "Get precision as string should match");

        num1.setBase(8);
        assertEquals(8, num1.getBase(), "Base should be updated");
        num1.setPrecision(4);
        assertEquals(4, num1.getPrecision(), "Precision should be updated");

        assertThrows(PNumberException.class, () -> num1.setBase(1));
        assertThrows(PNumberException.class, () -> num1.setPrecision(-1));
    }

    @Test
    @DisplayName("Test string conversion")
    void testStringConversion() {
        assertEquals("10.50", num1.getNumberAsString(), "String conversion should match");
        TPNumber hexNum = new TPNumber(10.9375, 16, 1);
        assertEquals("A.F", hexNum.getNumberAsString(), "Hex conversion should match");
    }
}
