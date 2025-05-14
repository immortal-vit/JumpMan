package frame;

import frame.panels.*;

import javax.swing.*;
import java.awt.*;

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
    Cursor invisibleCursor;


    public MainFrame() {
        initialize();
    }

    private void initialize(){
        panelType = PanelType.MENU;
        createPanels();
        createFrame();
        initializeCursor();

    }
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
    public void switchPanel(PanelType panelType){

        if (panelType == PanelType.EXIT){
            frame.dispose();
            System.exit(0);
            return;
        }

        cardLayout.show(panelContainer, panelType.name());
        setPanelType(panelType);
        if (panelType == PanelType.GAME) {
            gamePanel.startGame();
            gamePanel.requestFocusInWindow();
            hideCursor();
        } else {
            showCursor();
        }

        if (panelType == PanelType.SETTING){
            settingsPanel.updateRelocation();
        }

        lastPanelType = panelType;

        frame.repaint();
    }
    private void initializeCursor(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.createImage("");
        invisibleCursor = toolkit.createCustomCursor(image, new Point(0, 0), "invisibleCursor");
    }

    public void hideCursor() {
        frame.setCursor(invisibleCursor);
    }

    public void showCursor() {
        frame.setCursor(Cursor.getDefaultCursor());
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
}
