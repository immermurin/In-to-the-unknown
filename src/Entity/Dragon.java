package Entity;

import java.awt.*;

public class Dragon extends Enemy 
{
    public Dragon(int worldX, int worldY) 
    {
        super(Color.RED, worldX, worldY);
    }
    
    public void update(int playerX, int playerY) {
        chasePlayer(playerX, playerY);
    }
}
