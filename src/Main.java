public class Main {
    public static void main(String[] args) {
        GameLogic cellPanel = new GameLogic(40, 60);
        new GamePanel("生命游戏", cellPanel);
    }
}
