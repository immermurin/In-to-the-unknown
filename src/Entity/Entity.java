package Entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class Entity 
{
    protected int worldX, worldY;
    protected int speed;
    protected BufferedImage sprite;

    public Entity(int worldX, int worldY, int speed) 
    {
        this.worldX = worldX;
        this.worldY = worldY;
        this.speed = speed;
    }

    public abstract void update();
    public abstract void draw(Graphics g, int screenX, int screenY);
}
