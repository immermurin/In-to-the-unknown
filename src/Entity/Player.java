package Entity;

import java.awt.*;

public abstract class Player {
    protected int worldX, worldY;
    protected Color color;
    protected int moveSpeed = 5;

    public Player(Color color) {
        this.color = color;
        this.worldX = 0;
        this.worldY = 0;
    }

    public void move(int dx, int dy) {
        if (dx != 0 && dy != 0) {
            dx = (int) (dx / Math.sqrt(2)); // Normalize diagonal speed
            dy = (int) (dy / Math.sqrt(2));
        }
        worldX += dx * moveSpeed;
        worldY += dy * moveSpeed;
    }

    public int getWorldX() { return worldX; }
    public int getWorldY() { return worldY; }

    public void draw(Graphics g, int screenX, int screenY) {
        g.setColor(color);
        g.fillRect(screenX, screenY, 64, 64);
    }
}
