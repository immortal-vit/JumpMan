package frame.panels;

import frame.MainFrame;
import frame.PanelType;
import game.GameSettings;
import game.tiles.TileMap;
import game.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * panel where the game is located
 */
public class GamePanel extends JPanel implements Runnable {

    private final int VIRTUAL_WIDTH = 800;
    private final int VIRTUAL_HEIGHT = 608;

    private Player player;

    private final float tileSize = 32f;
    private HashMap<Integer, TileMap> floorMap = new HashMap<>();
    private final int MAX_FLOORS = 4;

    private Thread gameThread;
    private boolean running = false;
    private int floor = 0;
    private final MainFrame mainFrame;


    /**
     * there is key adapter for movement
     * @param mainFrame main frame is there for switching panels
     */
    public GamePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setFocusable(true);

        loadAllFloors();
        initializePlayer();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (GameSettings.getInstance().isDevModeOn()){
                    player.getPlayerControls().devSettingsPressed(e);
                } else {
                    player.getPlayerControls().normalSettingsPressed(e);
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    player.resetChargingJump();
                    mainFrame.switchPanel(PanelType.PAUSE);
                }


            }
            @Override
            public void keyReleased(KeyEvent e) {
                if (GameSettings.getInstance().isDevModeOn()){
                    player.getPlayerControls().devSettingsReleased(e);
                } else {
                    player.getPlayerControls().normalSettingsReleased(e);
                }
            }
        });
    }

    /**
     * chatgpt helped me with the callback and everything based of the callback.
     * creates the player.
     * player will call the callback when he wants to change the floor and game panel will change it for the player
     */
    private void initializePlayer() {
        player = new Player(100f,VIRTUAL_HEIGHT - 2 * tileSize, 1.25f * tileSize, floorMap.get(floor), this, mainFrame.getSoundPlayer());
        player.setFloorChangeCallback(direction -> {
            switch (direction) {
                case UP:
                    floor++;
                    player.setTileMap(floorMap.get(floor));
                    player.setY(floorMap.get(floor).getTileSize() * floorMap.get(floor).getRows() - player.getHEIGHT());
                    break;
                case DOWN:
                    floor--;
                    player.setTileMap(floorMap.get(floor));
                    player.setY(1);
                    break;
            }
        });
    }

    /**
     * chatgpt helped me with this
     * will tell the jFrame that the game will be started when Player switches from a different panel to game panel
     */
    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
        startGame();
    }

    /**
     * starts game
     */
    public void startGame() {
        if (gameThread == null || !gameThread.isAlive()) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }


    /**
     * chatGPT helped me with this
     * the game logic is updating 60 times in a second
     * the repainting is updating 120 times in a second
     */
    public void run() {
        final int TARGET_FPS_UPDATE = 60;
        final int TARGET_FPS_RENDER = 120;

        final double TIME_BETWEEN_UPDATES = 1_000_000_000.0 / TARGET_FPS_UPDATE;
        final double TIME_BETWEEN_RENDERS = 1_000_000_000.0 / TARGET_FPS_RENDER;

        long lastUpdateTime = System.nanoTime();
        long lastRenderTime = System.nanoTime();

        double deltaUpdate = 0;
        double deltaRender = 0;

        while (running) {
            long now = System.nanoTime();

            deltaUpdate += (now - lastUpdateTime) / TIME_BETWEEN_UPDATES;
            deltaRender += (now - lastRenderTime) / TIME_BETWEEN_RENDERS;

            lastUpdateTime = now;

            if (deltaUpdate >= 1) {
                update();
                deltaUpdate--;
            }

            if (deltaRender >= 1) {
                repaint();
                deltaRender--;
                lastRenderTime = now;
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("something went wrong in thread");
            }
        }
    }

    /**
     * stops thread and the game
     */
    public void stopThread (){
        running = false;
        try {
            if (gameThread != null) {
                Thread tmp = gameThread;
                gameThread = null;
                tmp.join();
            }
        } catch (InterruptedException e) {
            System.out.println("something went wrong when stopping a thread");
        }
    }

    public void update() {
        player.update();
    }

    /**
     * Render the game
     * and drawing the current floor and player.
     *
     * @param g the Graphics object used for rendering
     */
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
            floorMap.put(i, new TileMap("/floor/floor" + i, tileSize));
        }
    }
    /**
     * Triggers the victory screen by switching to the VICTORY panel.
     */
    public void triggerVictory(){
        mainFrame.switchPanel(PanelType.VICTORY);
    }
    /**
     * Resets the game to its initial state, restarting the game and player stats.
     */
    public void restartGame(){
        floor = 0;
        player.movePlayerToStartPosition();
        player.resetStats();
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Map<Integer, TileMap> getFloorMap() {
        return floorMap;
    }

    public int getMaxFloors() {
        return MAX_FLOORS;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

