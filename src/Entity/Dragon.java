package Entity;

public class Dragon extends Enemy {
    public Dragon(int worldX, int worldY) 
    {
        super("res/Orc.png", worldX, worldY); // Load dragon sprite
    }

    public void update(int playerX, int playerY) {
        chasePlayer(playerX, playerY);
    }
}
