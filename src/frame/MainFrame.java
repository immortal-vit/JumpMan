package frame;

import frame.panels.*;
import game.GameSettings;
import game.sounds.SoundPlayer;

import javax.swing.*;
import java.awt.*;

/**
 * class for frame and every panel in it
 */
public class MainFrame {

    private CardLayout cardLayout;
    private JPanel panelContainer;
    private JFrame frame;
    private MenuPanel menuPanel;
    private SettingsPanel settingsPanel;
    private GamePanel gamePanel;
    private PanelType panelType;
    private PausePanel pausePanel;
    private VictoryPanel victoryPanel;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private PanelType lastPanelType;
    private Cursor invisibleCursor;
    private SoundPlayer soundPlayer;


    /**
     * will initialize the game
     */
    public MainFrame() {
        initialize();
    }

    /**
     * will initialize every component
     */
    private void initialize(){
        GameSettings.getInstance();
        panelType = PanelType.MENU;
        initializeMusic();
        createPanels();
        createFrame();
        initializeCursor();


    }

    /**
     * this will create all panels
     */
    private void createPanels(){
        cardLayout = new CardLayout();
        panelContainer = new JPanel(cardLayout);

        menuPanel = new MenuPanel(this);
        settingsPanel = new SettingsPanel(this);
        gamePanel = new GamePanel(this);
        pausePanel = new PausePanel(this);
        victoryPanel = new VictoryPanel(this);

        panelContainer.add(menuPanel, PanelType.MENU.name());
        panelContainer.add(settingsPanel, PanelType.SETTING.name());
        panelContainer.add(gamePanel, PanelType.GAME.name());
        panelContainer.add(pausePanel, PanelType.PAUSE.name());
        panelContainer.add(victoryPanel, PanelType.VICTORY.name());

        cardLayout.show(panelContainer, getPanelType().name());

    }

    /**
     * this will create frame and set parameters to it
     */
    private void createFrame(){
        frame = new JFrame();
        frame.setSize(screenSize);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.add(panelContainer);
        frame.setVisible(true);
    }

    /**
     * will switch panel in frame
     * @param panelType panel type to switch
     */
    public void switchPanel(PanelType panelType){

        if (panelType == PanelType.EXIT){
            frame.dispose();
            System.exit(0);
            return;
        }

        cardLayout.show(panelContainer, panelType.name());
        setPanelType(panelType);

        managePanelFunctions(panelType);

        lastPanelType = panelType;

        frame.repaint();
    }

    /**
     * this will update cursor if it should show or not
     * @param panelType is used to detect which function will be used
     */
    private void managePanelFunctions(PanelType panelType){
        if (panelType == PanelType.GAME) {
            gamePanel.startGame();
            gamePanel.requestFocusInWindow();
            hideCursor();
        } else {
            showCursor();
            gamePanel.stopThread();
        }

        if (panelType == PanelType.SETTING){
            settingsPanel.updateRelocation();
        }
    }

    /**
     * will create cursor
     */
    private void initializeCursor(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.createImage("");
        invisibleCursor = toolkit.createCustomCursor(image, new Point(0, 0), "invisibleCursor");
    }

    /**
     * will hide cursor
     */
    public void hideCursor() {
        frame.setCursor(invisibleCursor);
    }

    /**
     * will show cursor
     */
    public void showCursor() {
        frame.setCursor(Cursor.getDefaultCursor());
    }

    /**
     * this will create a music and play it
     */
    private void initializeMusic(){
        soundPlayer = new SoundPlayer();
        soundPlayer.playMusic("/sounds/theme.wav");
        System.out.println("playSound");
    }

    /**
     * set volume for a music
     * @param newVolumePercent new volume which will be loaded from settings
     */
    public void setMusicVolume(int newVolumePercent) {
        GameSettings.getInstance().setMusicVolume(newVolumePercent);
        GameSettings.getInstance().save();
        soundPlayer.updateMusicVolume();
    }

    public PanelType getPanelType() {
        return panelType;
    }

    public void setPanelType(PanelType panelType) {
        this.panelType = panelType;
    }

    public PanelType getLastPanelType() {
        return lastPanelType;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public SoundPlayer getSoundPlayer() {
        return soundPlayer;
    }
}
