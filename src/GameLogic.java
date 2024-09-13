public class GameLogic {
    private final Cell[][] cells = new Cell[40][60];
    private final Cell[][] nextCells = new Cell[40][60];
    private int generation;
    private int row;
    private int col;

    public GameLogic(int row, int col) {
        this.row = row;
        this.col = col;
        generation = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                cells[i][j] = new Cell(i, j);
                cells[i][j].setLive(false);
            }
        }
    }

    public GameLogic(){}

    public void randomInitCells() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                cells[i][j].setLive(Math.random() > 0.7);
            }
        }
    }

    public void deleteCells() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                cells[i][j].setLive(false);
            }
        }
        generation = -1;
    }

    public void updateCells() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                nextCells[i][j] = new Cell(i, j);
            }
        }

        //判断周围细胞存活数，根据存活数量更改下一代该位置细胞的状态
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int ct = 0;
                for (int x = i - 1; x <= i + 1; x++) {
                    for (int y = j - 1; y <= j + 1; y++) {
                        if (x >= 0 && x < row && y >= 0 && y < col && cells[x][y].isLive()) ct++;
                    }
                }
                if (cells[i][j].isLive()) ct--;
                if (ct == 3) nextCells[i][j].setLive(true); //周围细胞数量等于3时，死亡的细胞会复活（模拟繁殖）
                else if (ct > 3) nextCells[i][j].setLive(false);    //大于3时，产生竞争，细胞死亡
                else if (ct < 2) nextCells[i][j].setLive(false);    //小于2时，繁殖能力低，细胞死亡
                else nextCells[i][j].setLive(cells[i][j].isLive()); //等于2时，细胞状态不变
            }
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                cells[i][j] = nextCells[i][j];
            }
        }
        generation++;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }


    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getGeneration() {
        return generation;
    }

    public boolean getCellLive(int row, int col) {
        return cells[row][col].isLive();
    }

    public void setCellLive(int row, int col, boolean live) {
        cells[row][col].setLive(live);
    }
}
