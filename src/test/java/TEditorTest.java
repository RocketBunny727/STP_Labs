import com.rocketbunny.exception.EditorException;
import com.rocketbunny.model.TEditor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TEditorTest {
    private TEditor editor;

    @BeforeEach
    void setUp() {
        editor = new TEditor();
    }

    @Test
    @DisplayName("Test constructor and clear")
    void testConstructorAndClear() {
        assertEquals("0 + i*0", editor.getString());
        editor.setString("1 + i*2");
        editor.clear();
        assertEquals("0 + i*0", editor.getString());
    }

    @Test
    @DisplayName("Test addDigit and edit")
    void testAddDigit() {
        editor.clear();
        editor.addDigit(1);
        assertEquals("1", editor.getString());

        editor.addDigit(2);
        assertEquals("12", editor.getString());

        editor.edit(1);
        assertEquals("121", editor.getString());

        editor.setString("1 + i*2");
        editor.addDigit(3);
        assertEquals("1 + i*23", editor.getString());
    }

    @Test
    @DisplayName("Test addSign")
    void testAddSign() {
        editor.clear();
        editor.addSign();
        assertEquals("-0 + i*0", editor.getString());

        editor.addSign();
        assertEquals("-0 + i*-0", editor.getString());

        editor.setString("1 + i*2");
        editor.addSign();
        assertEquals("1 + i*-2", editor.getString());

        editor.addSign();
        assertEquals("1 + i*2", editor.getString());
    }

    @Test
    @DisplayName("Test backspace")
    void testBackspace() {
        editor.setString("12,3 + i*4,5");
        editor.backspace();
        assertEquals("12,3 + i*4,", editor.getString());

        while (editor.getString().length() > 0) {
            editor.backspace();
        }
        assertEquals("", editor.getString());
        editor.backspace();
        assertEquals("", editor.getString());
    }

    @Test
    @DisplayName("Test isComplexZero")
    void testIsComplexZero() {
        assertTrue(editor.isComplexZero());

        editor.setString("0 + i*0");
        assertTrue(editor.isComplexZero());

        editor.setString("0,0 + i*0,0");
        assertTrue(editor.isComplexZero());

        editor.setString("1 + i*0");
        assertFalse(editor.isComplexZero());
    }

    @Test
    @DisplayName("Test edit method")
    void testEdit() {
        editor.clear();
        editor.edit(1);
        assertEquals("1", editor.getString());

        editor.edit(11);
        assertEquals("1,", editor.getString());

        editor.edit(2);
        assertEquals("1,2", editor.getString());

        editor.edit(12);
        assertEquals("1,2 + i*", editor.getString());

        editor.edit(10);
        assertEquals("1,2 + i*-", editor.getString());

        editor.edit(3);
        assertEquals("1,2 + i*-3", editor.getString());

        editor.edit(13);
        assertEquals("1,2 + i*-", editor.getString());

        editor.edit(14);
        assertEquals("0 + i*0", editor.getString());

        assertThrows(EditorException.class, () -> editor.edit(15));
    }

    @Test
    @DisplayName("Test exceptions and invalid sets")
    void testExceptions() {
        assertThrows(EditorException.class, () -> editor.setString("invalid"));
        assertThrows(EditorException.class, () -> editor.addDigit(10)); // Not 0-9
    }
}
