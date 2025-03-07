package Inotia;

import javax.swing.*;

public class ScreenManager 
{
    private final JFrame window;

    public ScreenManager(JFrame window) 
    {
        this.window = window;
    }

    public void showMenu() 
    {
        window.setContentPane(new MenuPanel(this)); 
        window.revalidate();
        window.repaint();
    }

    public void startGame() 
    {
        GamePanel gamePanel = new GamePanel(window.getWidth(), window.getHeight());
        window.setContentPane(gamePanel);
        window.revalidate();
        window.repaint();
        gamePanel.requestFocusInWindow();
        gamePanel.startGame();
    }
}
