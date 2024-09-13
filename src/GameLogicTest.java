import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameLogicTest {
    private GameLogic gameLogic;

    @BeforeEach
    public void setUp() {
        gameLogic = new GameLogic(10, 10); //初始化一个10x10的细胞皿用于测试
    }

    @Test
    public void testInitialization() {  //测试是否正确初始化
        assertEquals(10, gameLogic.getRow());
        assertEquals(10, gameLogic.getCol());
        assertEquals(0, gameLogic.getGeneration());
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                assertFalse(gameLogic.getCellLive(i, j));
            }
        }
    }

    @Test
    public void testRandomInitCells() { //测试随机初始化后有一些细胞是存活的
        gameLogic.randomInitCells();
        int liveCount = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (gameLogic.getCellLive(i, j)) {
                    liveCount++;
                }
            }
        }
        assertTrue(liveCount > 0);
    }

    @Test
    public void testDeleteCells() { //测试清空细胞皿
        gameLogic.randomInitCells();
        gameLogic.deleteCells();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                assertFalse(gameLogic.getCellLive(i, j));
            }
        }
        assertEquals(-1, gameLogic.getGeneration());    //清空后generation应该为-1，因为清空后接着是更新方法，更新方法中代数会自增
    }

    @Test
    public void testUpdateCells() { //测试更新细胞皿
        gameLogic.setCellLive(1, 0, true);
        gameLogic.setCellLive(1, 1, true);
        gameLogic.setCellLive(1, 2, true);
        gameLogic.updateCells();
        assertTrue(gameLogic.getCellLive(0, 1));
        assertTrue(gameLogic.getCellLive(1, 1));
        assertTrue(gameLogic.getCellLive(2, 1));
        assertEquals(1, gameLogic.getGeneration()); //第一次更新后，代数应该为1
    }
}