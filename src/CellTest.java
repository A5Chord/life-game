import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {
    private Cell cell;
    @BeforeEach
    void setUp() {
        cell = new Cell(0, 0);
    }

    @Test
    void setLive() {
        cell.setLive(true);
        assertTrue(cell.isLive());
    }
}