public class Cell {
    private int x;
    private int y;
    private boolean isLive;
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.isLive = false;
    }
    public Cell() {
        this.isLive = false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }
}
