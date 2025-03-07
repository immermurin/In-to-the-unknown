package Inotia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class GamePanel extends JPanel implements Runnable 
{
    private final int originalTileSize = 16;
    private final int scale = 4;
    private final int tileSize = originalTileSize * scale;
    private int worldX = 0, worldY = 0;
    private final Set<Integer> keys = new HashSet<>(); // 🔹 Added missing `keys`
    private Thread gameThread;
    private final int moveSpeed = 5;

    public GamePanel(int width, int height) 
    {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyHandler(keys)); // 🔹 Pass keys correctly
    }

    public void startGame() 
    {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() 
    {
        while (true) 
        {
            update();
            repaint();
            try { Thread.sleep(16); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    private void update() 
    {
        int dx = 0, dy = 0;

        if (keys.contains(KeyEvent.VK_W)) dy += 1;
        if (keys.contains(KeyEvent.VK_S)) dy -= 1;
        if (keys.contains(KeyEvent.VK_A)) dx += 1;
        if (keys.contains(KeyEvent.VK_D)) dx -= 1;

        if (dx != 0 && dy != 0) {
            dx *= (int) (moveSpeed / Math.sqrt(2));
            dy *= (int) (moveSpeed / Math.sqrt(2));
        } else {
            dx *= moveSpeed;
            dy *= moveSpeed;
        }

        worldX -= dx;
        worldY -= dy;
    }

    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);

        g.setColor(Color.RED);
        int debugSize = tileSize / 2;
        g.fillRect(getWidth() / 2 - debugSize / 2 - worldX, getHeight() / 2 - debugSize / 2 - worldY, debugSize, debugSize);

        g.setColor(Color.BLUE);
        g.fillRect(getWidth() / 2 - tileSize / 2, getHeight() / 2 - tileSize / 2, tileSize, tileSize);

        g.setColor(Color.WHITE);
        g.drawString("World Position: " + worldX + ", " + worldY, 10, 20);
    }
}
