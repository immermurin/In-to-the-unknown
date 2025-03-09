package Inotia;

import Entity.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class GamePanel extends JPanel implements Runnable {
    private final int originalTileSize = 16;
    private final int scale = 4;
    private final int tileSize = originalTileSize * scale;
    private Player player; // 🔹 Reference to the selected player
    private final Set<Integer> keys = new HashSet<>();
    private Thread gameThread;
    private final int moveSpeed = 2;
    private Dragon dragon;

    public GamePanel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow(); // 🔹 Ensure focus

        addKeyListener(new KeyHandler(keys)); // 🔹 Attach key listener
        this.player = player;
        this.dragon = new Dragon(500, 500);
    }

    public void setPlayer(Player player) {
        this.player = player;
        System.out.println("Player set: " + player.getClass().getSimpleName()); // 🔹 Debug log
    }

    public void startGame() {
        if (player == null) {
            throw new IllegalStateException("Player has not been set!");
        }
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (true) {
            update();
            repaint();
            try { Thread.sleep(16); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    private void update() {
        if (player == null) return;

        int dx = 0, dy = 0;
        if (keys.contains(KeyEvent.VK_W)) dy -= moveSpeed;
        if (keys.contains(KeyEvent.VK_S)) dy += moveSpeed;
        if (keys.contains(KeyEvent.VK_A)) dx -= moveSpeed;
        if (keys.contains(KeyEvent.VK_D)) dx += moveSpeed;

        player.move(dx, dy); // 🔹 Move the player
        dragon.update(player.getWorldX(), player.getWorldY());
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
        
        // 🔹 Display World Coordinates on the upper left
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("World Position: " + player.getWorldX() + ", " + player.getWorldY(), 10, 20);
    }
}
