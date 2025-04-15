package frame.panels;

import game.TileMap;
import game.player.Direction;
import game.player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel implements Runnable {

    private final int VIRTUAL_WIDTH = 800;
    private final int VIRTUAL_HEIGHT = 600;

    private Player player;
    private BufferedImage playerSprite;

    private float titeSize = 32f;
    private TileMap tileMap;

    private Thread gameThread;
    private boolean running = false;


    public GamePanel() {
        setFocusable(true);
        tileMap = new TileMap("src/game/floorMap/floor0", titeSize);

        initializePlayer();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A:
                        player.setDirection(Direction.LEFT);
                        break;
                    case KeyEvent.VK_D:
                        player.setDirection(Direction.RIGHT);
                        break;
                    case KeyEvent.VK_S:
                        player.setDirection(Direction.STANDING);
                        break;
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    player.startCharging();
                    System.out.println("charging");
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    player.stopChargingAndJump();
                    System.out.println("jump");
                }
            }
        });
    }

    private void initializePlayer() {
        try {
            playerSprite = ImageIO.read(new File("src/game/player/player.png"));
        }catch (IOException e){
            System.out.println("nepodarilo se nacist hrace");
        }
        player = new Player(100f,490f, 1.5f * titeSize, playerSprite, tileMap);
        System.out.println("hrac se vytvoril");
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
        startGame();
    }

    public void startGame() {
        if (gameThread == null) {
            gameThread = new Thread(this);
            running = true;
            gameThread.start();
        }
    }


    public void run() {

        final int FPS = 60;
        final long frameTime = 1000 / FPS;

        while (running) {
            long start = System.currentTimeMillis();

            update();
            repaint();

            long duration = System.currentTimeMillis() - start;
            long sleep = frameTime - duration;

            if (sleep > 0) {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void stopThread (){
        running = false;
    }

    public void update() {
        player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        int realWidth = getWidth();
        int realHeight = getHeight();

        float scaleX = realWidth / (float) VIRTUAL_WIDTH;
        float scaleY = realHeight / (float) VIRTUAL_HEIGHT;

        g2d.scale(scaleX, scaleY);

        tileMap.draw(g2d);
        player.render(g2d);
    }


}
