package tests;

import game.player.FloorDirection;
import game.player.Player;
import game.tiles.TileMap;
import frame.panels.GamePanel;
import frame.MainFrame;
import game.sounds.SoundPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * chatgpt helped me to make all the tests
 * there are tests for: floor changes, initial floor and tilemap, player update method and reset player position;
 */
public class PlayerTest {

    private DummyMainFrame mainFrame;
    private DummyGamePanel gamePanel;
    private DummyPlayer player;

    @BeforeEach
    public void setUp() {
        mainFrame = new DummyMainFrame();
        gamePanel = new DummyGamePanel(mainFrame);
        player = new DummyPlayer();

    }

    @Test
    public void testFloorUpChange() {
        Player player = gamePanel.getPlayer();
        float originalX = player.getX();

        player.getFloorChangeCallback().onFloorChange(FloorDirection.UP);

        assertEquals(1, gamePanel.getFloor());
        assertEquals(originalX, player.getX(), 0.001f);
        assertEquals(gamePanel.getFloorMap().get(1), player.getTileMap());
        assertEquals(
                gamePanel.getFloorMap().get(1).getTileSize() * gamePanel.getFloorMap().get(1).getRows() - player.getHEIGHT(),
                player.getY(),
                0.001f
        );
    }

    @Test
    public void testFloorDownChange() {
        Player player = gamePanel.getPlayer();

        player.getFloorChangeCallback().onFloorChange(FloorDirection.UP);
        assertEquals(1, gamePanel.getFloor());

        player.getFloorChangeCallback().onFloorChange(FloorDirection.DOWN);

        assertEquals(0, gamePanel.getFloor());
        assertEquals(gamePanel.getFloorMap().get(0), player.getTileMap());
        assertEquals(1, player.getY(), 0.001f);
    }
    @Test
    public void testRestartGameResetsPlayerAndFloor() {
        Player player = gamePanel.getPlayer();

        player.setX(200f);
        player.setY(100f);
        gamePanel.setFloor(2);

        gamePanel.restartGame();

        assertEquals(0, gamePanel.getFloor());
        assertEquals(gamePanel.getFloorMap().get(0), player.getTileMap());
        assertEquals(100f, player.getX(), 0.001f);
        assertEquals(608f - 2 * 32f, player.getY(), 0.001f);
    }

    @Test
    public void testUpdateCallsPlayerUpdate() {
        gamePanel.setPlayer(player);

        gamePanel.update();

        assertTrue(player.wasUpdated);
    }
    @Test
    public void testInitialFloorAndTileMap() {
        Player player = gamePanel.getPlayer();

        assertEquals(0, gamePanel.getFloor());
        assertEquals(gamePanel.getFloorMap().get(0), player.getTileMap());
    }


    static class DummyTileMap extends TileMap {
        public DummyTileMap() {
            super("dummy", 32f);
        }

        @Override public float getTileSize() { return 32f; }
        @Override public int getRows() { return 19; }
        @Override public void draw(Graphics2D g) { }
    }

    static class DummyMainFrame extends MainFrame {
        public DummyMainFrame() { }

        @Override public void switchPanel(frame.PanelType panelType) { }
        @Override public SoundPlayer getSoundPlayer() { return new SoundPlayer(); }
    }
    static class DummyPlayer extends Player {
        public boolean wasUpdated = false;

        public DummyPlayer() {
            super(0, 0, 0, new DummyTileMap(), null, new SoundPlayer());
        }

        @Override
        public void update() {
            wasUpdated = true;
        }
    }

    static class DummyGamePanel extends GamePanel {
        public DummyGamePanel(MainFrame mainFrame) {
            super(mainFrame);

            getFloorMap().clear();
            for (int i = 0; i < getMaxFloors(); i++) {
                getFloorMap().put(i, new DummyTileMap());
            }

            try {
                java.lang.reflect.Field playerField = GamePanel.class.getDeclaredField("player");
                playerField.setAccessible(true);
                Player dummyPlayer = new Player(
                        100f,
                        608f - 2 * 32f,
                        1.25f * 32f,
                        getFloorMap().get(0),
                        this,
                        new SoundPlayer()
                );
                dummyPlayer.setFloorChangeCallback(direction -> {
                    switch (direction) {
                        case UP -> {
                            setFloor(getFloor() + 1);
                            dummyPlayer.setTileMap(getFloorMap().get(getFloor()));
                            dummyPlayer.setY(getFloorMap().get(getFloor()).getTileSize() * getFloorMap().get(getFloor()).getRows() - dummyPlayer.getHEIGHT());
                        }
                        case DOWN -> {
                            setFloor(getFloor() - 1);
                            dummyPlayer.setTileMap(getFloorMap().get(getFloor()));
                            dummyPlayer.setY(1);
                        }
                    }
                });
                playerField.set(this, dummyPlayer);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

