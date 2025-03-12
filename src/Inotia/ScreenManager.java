package Inotia;

import Entity.Azki;
import Entity.Elaina;
import Entity.Player;

import javax.swing.*;

public class ScreenManager {
    private final JFrame window;
    private GamePanel gamePanel;
    private MenuPanel menuPanel;
    private PlayerSelectionPanel playerSelectionPanel;
    private Player selectedPlayer;
    private SoundManager soundManager;

    public ScreenManager(JFrame window) {
        this.window = window;
        showMenu();
    }

    public void showMenu() {
        menuPanel = new MenuPanel(this);
        window.setContentPane(menuPanel);
        window.revalidate();
        window.repaint();
    }

    public void showPlayerSelection() {
        playerSelectionPanel = new PlayerSelectionPanel(this);
        window.setContentPane(playerSelectionPanel);
        window.revalidate();
        window.repaint();
    }
    
    public Player getCurrentPlayer() 
    {
        return selectedPlayer;
    }

    public void startGame(String playerName) 
    {
        if (playerName.equals("Azki")) {
            selectedPlayer = new Azki();
        } else if (playerName.equals("Elaina")) {
            selectedPlayer = new Elaina();
        }

        if (selectedPlayer == null) {
            JOptionPane.showMessageDialog(window, "No player selected!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        gamePanel = new GamePanel(800, 600,this);
        gamePanel.setPlayer(selectedPlayer); // Pass the selected player
        window.setContentPane(gamePanel);
        window.revalidate();
        gamePanel.requestFocusInWindow();
        gamePanel.startGame();
    }
}
