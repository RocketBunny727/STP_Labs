import com.rocketbunny.model.TMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TMemoryTest {

    private TMemory<Double> memory;

    @BeforeEach
    void setUp() {
        Supplier<Double> zeroSupplier = () -> 0.0;
        BiFunction<Double, Double, Double> addFunction = Double::sum;
        memory = new TMemory<>(zeroSupplier, addFunction);
    }

    @Test
    @DisplayName("Test constructor")
    void testConstructor() {
        assertEquals(0.0, memory.get());
        assertEquals("_OFF", memory.getStateAsString());
    }

    @Test
    @DisplayName("Test store operation")
    void testStore() {
        memory.store(5.0);
        assertEquals(5.0, memory.get());
        assertEquals("_ON", memory.getStateAsString());
    }

    @Test
    @DisplayName("Test add operation in ON state")
    void testAddInOn() {
        memory.store(5.0);
        memory.add(3.0);
        assertEquals(8.0, memory.get());
        assertEquals("_ON", memory.getStateAsString());
    }

    @Test
    @DisplayName("Test add operation in OFF state")
    void testAddInOff() {
        memory.add(3.0);
        assertEquals(3.0, memory.get());
        assertEquals("_ON", memory.getStateAsString());
    }

    @Test
    @DisplayName("Test clear operation")
    void testClear() {
        memory.store(5.0);
        memory.clear();
        assertEquals(0.0, memory.get());
        assertEquals("_OFF", memory.getStateAsString());
    }

    @Test
    @DisplayName("Test get in OFF state")
    void testGetInOff() {
        assertEquals(0.0, memory.get());
        assertEquals("_OFF", memory.getStateAsString());
    }

    @Test
    @DisplayName("Test multiple operations")
    void testMultiple() {
        memory.add(10.0);
        assertEquals(10.0, memory.get());
        assertEquals("_ON", memory.getStateAsString());

        memory.clear();
        assertEquals(0.0, memory.get());
        assertEquals("_OFF", memory.getStateAsString());

        memory.store(20.0);
        assertEquals(20.0, memory.get());

        memory.add(5.0);
        assertEquals(25.0, memory.get());
    }
}
