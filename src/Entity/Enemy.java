package Entity;

import java.awt.*;

public abstract class Enemy 
{
    protected int worldX, worldY;
    protected Color color;
    protected int size = 64; // Default size
    protected int moveSpeed = 2;

    public Enemy(Color color, int worldX, int worldY) 
    {
        this.color = color;
        this.worldX = worldX;
        this.worldY = worldY;
    }
    
    public void chasePlayer(int playerX, int playerY) {
        int dx = playerX - worldX;
        int dy = playerY - worldY;

        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) { // Prevent division by zero
            worldX += (int) (moveSpeed * (dx / distance)); // Normalize movement
            worldY += (int) (moveSpeed * (dy / distance));
        }
    }



    public int getWorldX() 
    {
        return worldX;
    }

    public int getWorldY() 
    {
        return worldY;
    }

    public void draw(Graphics g, int screenX, int screenY) 
    {
        g.setColor(color);
        g.fillRect(screenX, screenY, size, size);
    }
}
