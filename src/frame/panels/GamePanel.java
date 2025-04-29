package frame.panels;

import frame.MainFrame;
import frame.PanelType;
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
import java.util.HashMap;
import java.util.Map;

public class GamePanel extends JPanel implements Runnable {

    private final int VIRTUAL_WIDTH = 800;
    private final int VIRTUAL_HEIGHT = 608;

    private Player player;
    private BufferedImage playerSprite;

    private float tileSize = 32f;
    private Map<Integer, TileMap> floorMap = new HashMap<>();
    private final int MAX_FLOORS = 3;

    private Thread gameThread;
    private boolean running = false;
    private int floor = 0;


    public GamePanel(MainFrame mainFrame) {

        setFocusable(true);

        loadAllFloors();
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
                    case KeyEvent.VK_ESCAPE:
                        mainFrame.switchPanel(PanelType.MENU);
                        player.resetChargingJump();
                        stopThread();
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
        player = new Player(100f,500, 1.25f * tileSize, floorMap.get(floor), this);
        System.out.println("hrac se vytvoril");
        player.setFloorChangeCallback(direction -> {
            switch (direction) {
                case UP:
                    floor++;
                    player.setTileMap(floorMap.get(floor));
                    player.setY(floorMap.get(floor).getTileSize() * floorMap.get(floor).getRows() - player.getHeight());
                    break;
                case DOWN:
                    floor--;
                    player.setTileMap(floorMap.get(floor));
                    player.setY(1);
                    break;
            }
        });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
        startGame();
    }

    public void startGame() {
        if (!running) {
            gameThread = new Thread(this);
            running = true;
            gameThread.start();
        }
    }


    /**
     * chatGPT helped me with this
     * the game logic is updating 60 times in a second
     * the repainting is updating as soon as it can so if we have monitor that supports more than 60hz it will run at the monitor max fps
     */
    public void run() {
        final int TARGET_FPS = 60;
        final double TARGET_TIME_BETWEEN_UPDATES = 1000000000.0 / TARGET_FPS;

        long lastUpdateTime = System.nanoTime();
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            double elapsedTime = now - lastUpdateTime;
            delta += elapsedTime / TARGET_TIME_BETWEEN_UPDATES;
            lastUpdateTime = now;

            while (delta >= 1) {
                update();
                delta--;
            }

            repaint();

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("something went wrong in thread");
            }
        }
    }
    public void stopThread (){
        running = false;
        try {
            if (gameThread != null) {
                gameThread.join();
                gameThread = null;
            }
        } catch (InterruptedException e) {
            System.out.println("something went wrong when pausing a thread");
        }
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

        floorMap.get(floor).draw(g2d);
        player.render(g2d);
    }

    /**
     * this will load all floors
     */
    private void loadAllFloors() {
        for (int i = 0; i < MAX_FLOORS; i++) {
            floorMap.put(i, new TileMap("src/game/floorMap/floor" + i, tileSize));


        }
    }






}
