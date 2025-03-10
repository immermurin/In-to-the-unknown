package Inotia;

import Entity.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GamePanel extends JPanel implements Runnable {
    private final int originalTileSize = 16;
    private final int scale = 4;
    private final int tileSize = originalTileSize * scale;
    private Player player; // Reference to the selected player
    private final Set<Integer> keys = new HashSet<>();
    private Thread gameThread;
    private final int moveSpeed = 2;
    private Dragon dragon;
    private boolean isPaused = false;
    private ScreenManager screenManager;
    private JDialog pauseDialog; // Store the pause menu reference

    public GamePanel(int width, int height,ScreenManager screenManager) {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow(); // Ensure focus

        addKeyListener(new KeyHandler(keys)); // Attach key listener
        this.screenManager = screenManager;
        this.dragon = new Dragon(500, 500);
    }

    public void setPlayer(Player player) {
        
        this.player = player;
        System.out.println("Player set: " + player.getClass().getSimpleName()); // Debug log
    }

    public void startGame() {
        if (player == null) {
            throw new IllegalStateException("Player has not been set!");
        }
        gameThread = new Thread(this);
        gameThread.start();
    }

     @Override
    public void run() 
    {
        while (true) 
        {
            if (!isPaused) { // Only update when not paused
                update();
            }
            repaint();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        if (player == null) return;

        int dx = 0, dy = 0;
        if (keys.contains(KeyEvent.VK_W)) dy -= moveSpeed;
        if (keys.contains(KeyEvent.VK_S)) dy += moveSpeed;
        if (keys.contains(KeyEvent.VK_A)) dx -= moveSpeed;
        if (keys.contains(KeyEvent.VK_D)) dx += moveSpeed;

        player.move(dx, dy); // Move the player
        dragon.update(player.getWorldX(), player.getWorldY());
        player.updateAnimation();
        
        // Pause game when ESC is pressed
        if (keys.contains(KeyEvent.VK_ESCAPE)) 
        {
            togglePause();
            keys.remove(KeyEvent.VK_ESCAPE); // Prevent multiple triggers
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw Player
        int screenX = getWidth() / 2 - tileSize / 2;
        int screenY = getHeight() / 2 - tileSize / 2;
        player.draw(g, screenX, screenY);

        int dragonScreenX = dragon.getWorldX() - player.getWorldX() + screenX;
        int dragonScreenY = dragon.getWorldY() - player.getWorldY() + screenY;
        dragon.draw(g, dragonScreenX, dragonScreenY);
        
        // Display World Coordinates on the upper left
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("World Position: " + player.getWorldX() + ", " + player.getWorldY(), 10, 20);
        
        // Draw Pause Overlay
        if (isPaused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Paused", getWidth() / 2 - 100, getHeight() / 2 - 100);
        }
    }
    
    private void togglePause() {
        if (isPaused) {
            isPaused = false;
            if (pauseDialog != null) {
                pauseDialog.dispose(); // Close the menu if it's open
            }
        } else {
            isPaused = true;
            showPauseMenu();
        }
    }
    
    
     private void showPauseMenu() 
     {
        SwingUtilities.invokeLater(() -> 
        {
            pauseDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Paused", false);
            pauseDialog.setUndecorated(true);
            pauseDialog.setLayout(new GridLayout(2, 1));
            pauseDialog.setSize(300, 150);
            pauseDialog.setLocationRelativeTo(this);

            JButton resumeButton = new JButton("Resume");
            JButton exitButton = new JButton("Exit to Menu");

            resumeButton.addActionListener(e -> togglePause());
            exitButton.addActionListener(e -> {
                screenManager.showMenu();
                pauseDialog.dispose();
            });

            pauseDialog.add(resumeButton);
            pauseDialog.add(exitButton);
            pauseDialog.setVisible(true);
        });
    }
}
