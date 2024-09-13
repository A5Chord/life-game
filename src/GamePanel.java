import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JFrame implements ActionListener {
    int row, col;
    private GameLogic cellPanel;
    JButton[][] buttons;
    private JLabel nowGeneration, tip;
    JButton randomInit;
    JButton beginAndOver;
    JButton stopAndContinue;
    JButton toNext;
    JButton adjustSpeed;
    boolean isRunning;
    private Thread thread;
    int speed = 500;

    public GamePanel(String name, GameLogic cellPanel) {
        super(name);
        this.row = cellPanel.getRow();
        this.col = cellPanel.getCol();
        this.cellPanel = cellPanel;
        initGameGUI();
    }

    public void initGameGUI() {
        JPanel mainPanel, topPanel, bottomPanel, centerPanel;
        mainPanel = new JPanel(new BorderLayout());
        topPanel = new JPanel();
        bottomPanel = new JPanel();
        centerPanel = new JPanel(new GridLayout(row, col));
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainPanel.add(topPanel, "North");
        mainPanel.add(centerPanel, "Center");
        mainPanel.add(bottomPanel, "South");

        buttons = new JButton[row][col];
        randomInit = new JButton("随机生成");
        beginAndOver = new JButton("开始演变");
        stopAndContinue = new JButton("暂停");
        toNext = new JButton("下一代");
        nowGeneration = new JLabel("当前代数：" + cellPanel.getGeneration());
        adjustSpeed = new JButton("速度：" + (double) 1 / this.speed * 500 + "x");
        tip = new JLabel("提示：点击格子以生成存活的细胞，或使用随机生成按钮！");

        for (int x = 0; x < row; x++) {
            for (int y = 0; y < col; y++) {
                buttons[x][y] = new JButton();
                buttons[x][y].setBackground(Color.gray);
                buttons[x][y].setActionCommand(x + "," + y); // 将坐标保存在 ActionCommand 中
                buttons[x][y].addActionListener(setCell);
                centerPanel.add(buttons[x][y]);
            }
        }

        topPanel.add(tip);
        bottomPanel.add(randomInit);
        bottomPanel.add(beginAndOver);
        bottomPanel.add(stopAndContinue);
        bottomPanel.add(adjustSpeed);
        bottomPanel.add(toNext);
        bottomPanel.add(nowGeneration);

        //设置窗口
        int sizeRow, sizeCol;
        sizeRow = (row + 1) * 32;
        sizeCol = (col + 1) * 30;
        this.setSize(sizeCol, sizeRow);
        this.setResizable(true);
        this.setLocationRelativeTo(null);//让窗口居中显示
        this.setVisible(true);

        //注册监听器
        this.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
        randomInit.addActionListener(this);
        beginAndOver.addActionListener(this);
        stopAndContinue.addActionListener(this);
        adjustSpeed.addActionListener(this);
        toNext.addActionListener(this);

        stopAndContinue.setEnabled(false);

        randomInit.setFont(new Font("幼圆", Font.BOLD, 20));
        beginAndOver.setFont(new Font("幼圆", Font.BOLD, 20));
        stopAndContinue.setFont(new Font("幼圆", Font.BOLD, 20));
        adjustSpeed.setFont(new Font("幼圆", Font.BOLD, 20));
        toNext.setFont(new Font("幼圆", Font.BOLD, 20));
        nowGeneration.setFont(new Font("幼圆", Font.BOLD, 20));
        tip.setFont(new Font("幼圆", Font.BOLD, 18));
    }

    public ActionListener setCell = e -> {
        if (!beginAndOver.getText().equals("结束演变")) {
            String[] coords = e.getActionCommand().split(","); // 解析出坐标
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            if (buttons[x][y].getBackground() == Color.gray) {
                buttons[x][y].setBackground(Color.cyan);
                cellPanel.setCellLive(x, y, true);
            } else {
                buttons[x][y].setBackground(Color.gray);
                cellPanel.setCellLive(x, y, false);
            }
        }
    };

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == randomInit && beginAndOver.getText().equals("开始演变")) {//随机生成第一代
            cellPanel.randomInitCells();
            showCellPanel();
            isRunning = false;
            thread = null;
        } else if (e.getSource() == beginAndOver && beginAndOver.getText().equals("开始演变")) {//开始
            randomInit.setEnabled(false);
            isRunning = true;
            thread = new Thread(() -> {
                while (isRunning) {
                    Change();
                    try {
                        Thread.sleep(speed);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            thread.start();
            beginAndOver.setText("结束演变");
            stopAndContinue.setEnabled(true);
        } else if (e.getSource() == beginAndOver && beginAndOver.getText().equals("结束演变")) {//结束
            isRunning = false;
            thread = null;
            cellPanel.deleteCells();
            Change();
            beginAndOver.setText("开始演变");
            stopAndContinue.setText("暂停");
            randomInit.setEnabled(true);
            stopAndContinue.setEnabled(false);
        } else if (e.getSource() == stopAndContinue && stopAndContinue.getText().equals("暂停")) {//暂停
            isRunning = false;
            thread = null;
            stopAndContinue.setText("继续");
        } else if (e.getSource() == stopAndContinue && stopAndContinue.getText().equals("继续")) {//继续
            isRunning = true;
            thread = new Thread(() -> {
                while (isRunning) {
                    Change();
                    try {
                        Thread.sleep(speed);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            thread.start();
            stopAndContinue.setText("暂停");
        } else if (e.getSource() == toNext) {//下一代
            if (stopAndContinue.getText().equals("继续")) {
                Change();
                isRunning = false;
                thread = null;
            } else {
                JOptionPane.showMessageDialog(null, "请暂停后再点击下一代！", "提示", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == adjustSpeed) {
            if (this.speed == 500)
                this.speed = 100;
            else if (this.speed == 100)
                this.speed = 1000;
            else
                this.speed = 500;
            adjustSpeed.setText("速度：" + (double) 1 / this.speed * 500 + "x");
        }
    }

    public void Change() {
        cellPanel.updateCells();
        showCellPanel();
        nowGeneration.setText("当前代数：" + cellPanel.getGeneration());
    }

    public void showCellPanel() {
        for (int x = 0; x < row; x++) {
            for (int y = 0; y < col; y++) {
                if (cellPanel.getCellLive(x, y)) {
                    buttons[x][y].setBackground(Color.cyan);
                } else {
                    buttons[x][y].setBackground(Color.gray);
                }
            }
        }
    }
}