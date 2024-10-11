import javax.swing.*;

public class SnakeApp {
    public static void main(String[] args) throws Exception{
        int boardH = 600;
        int boardW = boardH;

        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(boardW, boardH);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        SnakeGame snakeGame = new SnakeGame(boardW, boardH);
        frame.add(snakeGame);
        frame.pack();
        snakeGame.requestFocus();
    }
}