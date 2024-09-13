import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GamePanelTest {
    private GamePanel gamePanel;
    private GameLogic mockGameLogic;

    @BeforeEach
    void setUp() {
        mockGameLogic = mock(GameLogic.class);
        when(mockGameLogic.getRow()).thenReturn(5);
        when(mockGameLogic.getCol()).thenReturn(5);
        when(mockGameLogic.getGeneration()).thenReturn(0);
        gamePanel = new GamePanel("生命游戏", mockGameLogic);
    }

    @Test
    void actionPerformed_randomInit() { //测试随机初始化按钮的响应动作
        ActionEvent event = new ActionEvent(gamePanel.randomInit, ActionEvent.ACTION_PERFORMED, null);
        gamePanel.actionPerformed(event);
        verify(mockGameLogic, times(1)).randomInitCells();
    }

    @Test
    void actionPerformed_beginAndOver() {   //测试开始演变按钮的响应动作
        ActionEvent event = new ActionEvent(gamePanel.beginAndOver, ActionEvent.ACTION_PERFORMED, null);
        gamePanel.actionPerformed(event);
        assertTrue(gamePanel.isRunning);
        assertEquals("结束演变", gamePanel.beginAndOver.getText());
    }

    @Test
    void actionPerformed_stopAndContinue() {    //测试暂停和继续按钮的响应动作
        //开始游戏
        gamePanel.actionPerformed(new ActionEvent(gamePanel.beginAndOver, ActionEvent.ACTION_PERFORMED, null));
        //结束游戏
        ActionEvent event = new ActionEvent(gamePanel.stopAndContinue, ActionEvent.ACTION_PERFORMED, null);
        gamePanel.actionPerformed(event);
        assertFalse(gamePanel.isRunning);
        assertEquals("继续", gamePanel.stopAndContinue.getText());
    }

    @Test
    void actionPerformed_toNext() { //测试下一代按钮的响应动作
        //先暂停游戏
        gamePanel.actionPerformed(new ActionEvent(gamePanel.beginAndOver, ActionEvent.ACTION_PERFORMED, null));
        gamePanel.actionPerformed(new ActionEvent(gamePanel.stopAndContinue, ActionEvent.ACTION_PERFORMED, null));
        //再点击下一代
        ActionEvent event = new ActionEvent(gamePanel.toNext, ActionEvent.ACTION_PERFORMED, null);
        gamePanel.actionPerformed(event);
        verify(mockGameLogic, times(1)).updateCells();
    }

    @Test
    void actionPerformed_adjustSpeed() {    //测试调整速度按钮的响应动作
        ActionEvent event = new ActionEvent(gamePanel.adjustSpeed, ActionEvent.ACTION_PERFORMED, null);
        gamePanel.actionPerformed(event);
        assertEquals(100, gamePanel.speed);
        gamePanel.actionPerformed(event);
        assertEquals(1000, gamePanel.speed);
        gamePanel.actionPerformed(event);
        assertEquals(500, gamePanel.speed);
    }

    @Test
    void change() { //测试 Change 方法是否正确调用 GameLogic 的 updateCells 方法
        gamePanel.Change();
        verify(mockGameLogic, times(1)).updateCells();
    }

    @Test
    void showCellPanel() { //测试 showCellPanel 方法是否正确更新按钮颜色
        when(mockGameLogic.getCellLive(anyInt(), anyInt())).thenReturn(true);
        gamePanel.showCellPanel();
        for (int x = 0; x < gamePanel.row; x++) {
            for (int y = 0; y < gamePanel.col; y++) {
                assertEquals(Color.cyan, gamePanel.buttons[x][y].getBackground());
            }
        }
    }
}