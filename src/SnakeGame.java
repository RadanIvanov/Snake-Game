import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.ArrayList;
public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    int boardW;
    int boardH;
    int tileSize = 25;

    Random random;

    //Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //Food
    Tile apple;

    //Game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    SnakeGame(int boardW, int boardH){
        this.boardW = boardW;
        this.boardH = boardH;
        setPreferredSize(new Dimension(this.boardW, this.boardH));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<>();

        apple = new Tile(10, 10);
        random = new Random();
        placeFood();

        gameLoop = new Timer(100, this);
        gameLoop.start();
        velocityX = 0;
        velocityY = 0;

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //Grid
        for(int i = 0; i < boardW/tileSize; i++){
            g.drawLine(i*tileSize, 0, i*tileSize, boardW);
            g.drawLine(0, i*tileSize, boardH, i*tileSize);
        }

        //Snake Head
        g.setColor(Color.green);
        //g.fillRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize);
        g.fill3DRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize, true);

        //Snake Body
        for(int i = 0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            //g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        //Apple
        g.setColor(Color.red);
        //g.fillRect(apple.x*tileSize, apple.y*tileSize, tileSize, tileSize);
        g.fill3DRect(apple.x*tileSize, apple.y*tileSize, tileSize, tileSize, true);

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if(gameOver){
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), 20, 40);
        }else{
            g.setColor(Color.green);
            g.drawString("Score: " + String.valueOf(snakeBody.size()), 25, 25);
        }
    }

    public void placeFood(){
        apple.x = random.nextInt(boardW/tileSize);
        apple.y = random.nextInt(boardH/tileSize);
    }

    public boolean collisioins(Tile t1, Tile t2){
        return t1.x == t2.x && t1.y == t2.y;
    }

    public void move(){
        //Snake Head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //Snake Body eat apple
        if(collisioins(snakeHead, apple)){
            snakeBody.add(new Tile(apple.x, apple.y));
            placeFood();
        }

        //Snake Body grow
        for(int i = snakeBody.size() - 1; i >= 0; i--){
            Tile snakePart = snakeBody.get(i);
            if(i == 0){
                snakePart.x = snakeHead.x-velocityX;
                snakePart.y = snakeHead.y-velocityY;
            }else{
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //Game Over v1
        for(int i = 0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            if(collisioins(snakeHead, snakePart)){
                gameOver = true;
            }
        }

        //Game Over v2
        if(snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardW ||
                snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardH){
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityY == 0){
            velocityX = 0;
            velocityY = -1;
        }else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY == 0) {
            velocityX = 0;
            velocityY = 1;
        }else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX == 0) {
            velocityX = 1;
            velocityY = 0;
        }else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX == 0) {
            velocityX = -1;
            velocityY = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}
