package Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class Player {
    protected int worldX, worldY;
    protected int moveSpeed = 5;

    // Sprite handling
    private BufferedImage spriteSheet;
    private BufferedImage[][] animations; // 2D array [rows][columns]
    private int currentFrame = 0;
    private int animationRow = 0; // Default: walking down
    private int frameDelay = 10; // Adjust for animation speed
    private int frameCounter = 0;
    private int lastDirection = 0;

    public Player(String spritePath) {
        this.worldX = 0;
        this.worldY = 0;
        loadSprites(spritePath);
    }

    private void loadSprites(String path) {
        try {
            spriteSheet = ImageIO.read(getClass().getClassLoader().getResourceAsStream(path));
            animations = new BufferedImage[8][6]; // 8 rows, 6 columns

            int spriteWidth = 48;
            int spriteHeight = 64;
            int imageHeight = spriteSheet.getHeight(); // Ensure we don't go beyond bounds

            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 6; col++) {
                    int y = row * spriteHeight;
                    if (y + spriteHeight > imageHeight) continue; // Prevent out-of-bounds error

                    animations[row][col] = spriteSheet.getSubimage(col * spriteWidth, y, spriteWidth, spriteHeight);
                }
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void move(int dx, int dy) {
        if (dx == 0 && dy == 0) {
            animationRow = 0; // Idle animation
        } else {
            if (dx > 0 && dy == 0) animationRow = 5; // Right
            else if (dx < 0 && dy == 0) animationRow = 1; // Left
            else if (dx == 0 && dy > 0) animationRow = 0; // Down
            else if (dx == 0 && dy < 0) animationRow = 3; // Up
            else if (dx > 0 && dy < 0) animationRow = 4; // Up-Right
            else if (dx < 0 && dy < 0) animationRow = 2; // Up-Left
            else if (dx > 0 && dy > 0) animationRow = 0; // Diagonal Down-Right
            else if (dx < 0 && dy > 0) animationRow = 0; // Diagonal Down-Left
        }

        if (dx != 0 && dy != 0) {
            dx = (int) (dx / Math.sqrt(2)); // Normalize diagonal speed
            dy = (int) (dy / Math.sqrt(2));
        }

        worldX += dx * moveSpeed;
        worldY += dy * moveSpeed;
    }


    private void selectAnimation(int dx, int dy) {
        if (dx == 0 && dy > 0) animationRow = 0;  // Walking down
        else if (dx < 0 && dy > 0) animationRow = 1; // Left-down
        else if (dx < 0 && dy == 0) animationRow = 2; // Left
        else if (dx < 0 && dy < 0) animationRow = 3; // Up-left
        else if (dx == 0 && dy < 0) animationRow = 4; // Walking up
        else if (dx > 0 && dy < 0) animationRow = 5; // Up-right
        else if (dx > 0 && dy == 0) animationRow = 6; // Right
        else if (dx > 0 && dy > 0) animationRow = 7; // Right-down
    }

    public void updateAnimation() {
        frameCounter++;
        if (frameCounter >= frameDelay) {
            currentFrame = (currentFrame + 1) % 6; // Loop through animation frames
            frameCounter = 0;
        }
    }

    public void draw(Graphics g, int screenX, int screenY) {
        if (animations[animationRow][currentFrame] != null) {
            g.drawImage(animations[animationRow][currentFrame], screenX, screenY, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(screenX, screenY, 48, 64); // Debugging - Shows a red box if null
        }
    }

    public int getWorldX() {
        return worldX;
    }

    public int getWorldY() {
        return worldY;
    }
}
