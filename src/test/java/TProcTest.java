import com.rocketbunny.model.TProc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TProcTest {

    private TProc<Double> proc;

    @BeforeEach
    void setUp() {
        Supplier<Double> zero = () -> 0.0;
        BiFunction<Double, Double, Double> add = Double::sum;
        BiFunction<Double, Double, Double> sub = (a, b) -> a - b;
        BiFunction<Double, Double, Double> mul = (a, b) -> a * b;
        BiFunction<Double, Double, Double> div = (a, b) -> {
            if (b == 0) throw new ArithmeticException("Division by zero");
            return a / b;
        };
        Function<Double, Double> inv = (a) -> {
            if (a == 0) throw new ArithmeticException("Inverse of zero");
            return 1 / a;
        };
        Function<Double, Double> sqr = (a) -> a * a;
        proc = new TProc<>(zero, add, sub, mul, div, inv, sqr);
    }

    @Test
    @DisplayName("Test constructor and reset")
    void testConstructorAndReset() {
        assertEquals(0.0, proc.getLeftOperand());
        assertEquals(0.0, proc.getRightOperand());
        assertEquals(TProc.Operation.NONE, proc.getOperation());

        proc.setLeftOperand(5.0);
        proc.setRightOperand(3.0);
        proc.setOperation(TProc.Operation.ADD);
        proc.reset();
        assertEquals(0.0, proc.getLeftOperand());
        assertEquals(0.0, proc.getRightOperand());
        assertEquals(TProc.Operation.NONE, proc.getOperation());
    }

    @Test
    @DisplayName("Test operationClear")
    void testOperationClear() {
        proc.setOperation(TProc.Operation.ADD);
        proc.operationClear();
        assertEquals(TProc.Operation.NONE, proc.getOperation());
    }

    @Test
    @DisplayName("Test operationRun")
    void testOperationRun() {
        proc.setLeftOperand(5.0);
        proc.setRightOperand(3.0);

        proc.setOperation(TProc.Operation.ADD);
        proc.operationRun();
        assertEquals(8.0, proc.getLeftOperand());

        proc.setLeftOperand(5.0);
        proc.setOperation(TProc.Operation.SUB);
        proc.operationRun();
        assertEquals(2.0, proc.getLeftOperand());

        proc.setLeftOperand(5.0);
        proc.setOperation(TProc.Operation.MUL);
        proc.operationRun();
        assertEquals(15.0, proc.getLeftOperand());

        proc.setLeftOperand(5.0);
        proc.setOperation(TProc.Operation.DIV);
        proc.operationRun();
        assertEquals(5.0 / 3.0, proc.getLeftOperand());

        proc.setOperation(TProc.Operation.NONE);
        proc.operationRun();
        assertEquals(5.0 / 3.0, proc.getLeftOperand()); // No change

        proc.setRightOperand(0.0);
        proc.setOperation(TProc.Operation.DIV);
        assertThrows(ArithmeticException.class, proc::operationRun);
    }

    @Test
    @DisplayName("Test functionRun")
    void testFunctionRun() {
        proc.setRightOperand(4.0);

        proc.functionRun(TProc.FunctionType.SQR);
        assertEquals(16.0, proc.getRightOperand());

        proc.setRightOperand(2.0);
        proc.functionRun(TProc.FunctionType.REV);
        assertEquals(0.5, proc.getRightOperand());

        proc.setRightOperand(0.0);
        assertThrows(ArithmeticException.class, () -> proc.functionRun(TProc.FunctionType.REV));
    }

    @Test
    @DisplayName("Test getters and setters")
    void testGettersSetters() {
        proc.setLeftOperand(10.0);
        assertEquals(10.0, proc.getLeftOperand());

        proc.setRightOperand(20.0);
        assertEquals(20.0, proc.getRightOperand());

        proc.setOperation(TProc.Operation.MUL);
        assertEquals(TProc.Operation.MUL, proc.getOperation());
    }

    @Test
    @DisplayName("Test example calculation from spec")
    void testExampleCalculation() {
        assertEquals(0.0, proc.getLeftOperand());
        assertEquals(0.0, proc.getRightOperand());
        assertEquals(TProc.Operation.NONE, proc.getOperation());

        proc.setLeftOperand(2.0);
        assertEquals(2.0, proc.getLeftOperand());

        proc.setOperation(TProc.Operation.ADD);
        assertEquals(TProc.Operation.ADD, proc.getOperation());

        proc.setRightOperand(3.0);
        assertEquals(3.0, proc.getRightOperand());

        proc.operationRun();
        assertEquals(5.0, proc.getLeftOperand());
        proc.setOperation(TProc.Operation.MUL);

        proc.setRightOperand(4.0);

        proc.functionRun(TProc.FunctionType.SQR);
        assertEquals(16.0, proc.getRightOperand());

        proc.operationRun();
        assertEquals(80.0, proc.getLeftOperand());

        proc.reset();
        assertEquals(0.0, proc.getLeftOperand());
        assertEquals(0.0, proc.getRightOperand());
        assertEquals(TProc.Operation.NONE, proc.getOperation());
    }
}
