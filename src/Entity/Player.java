package Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class Player {
    protected int worldX, worldY;
    protected int moveSpeed = 4;

    // Sprite handling
    private BufferedImage walkSpriteSheet;
    private BufferedImage idleSpriteSheet;
    private BufferedImage[][] walkAnimations; // Walking animations [rows][columns]
    private BufferedImage[][] idleAnimations; // Idle animations [rows][columns]
    
    private int currentFrame = 0;
    private int animationRow = 0; // Default: walking down
    private int frameDelay = 10; // Adjust for animation speed
    private int frameCounter = 0;
    
    private boolean isMoving = false;
    private int lastDirection = 0; // Stores the last direction

    public Player(String walkSpritePath, String idleSpritePath) {
        this.worldX = 0;
        this.worldY = 0;
        loadSprites(walkSpritePath, idleSpritePath);
    }

    private void loadSprites(String walkPath, String idlePath) {
        try {
            walkSpriteSheet = ImageIO.read(getClass().getClassLoader().getResourceAsStream(walkPath));
            idleSpriteSheet = ImageIO.read(getClass().getClassLoader().getResourceAsStream(idlePath));

            walkAnimations = new BufferedImage[8][6]; // 8 rows, 6 columns
            idleAnimations = new BufferedImage[8][6]; // 8 rows, 6 columns

            int spriteWidth = 48;
            int spriteHeight = 64;

            int sheetHeight = walkSpriteSheet.getHeight(); // Ensure within image bounds

            for (int row = 0; row < 8; row++) {
                int y = row * spriteHeight;
                if (y + spriteHeight > sheetHeight) break; // Prevent out-of-bounds error
                for (int col = 0; col < 6; col++) 
                {
                    walkAnimations[row][col] = resizeSprite(walkSpriteSheet.getSubimage(col * spriteWidth, y, spriteWidth, spriteHeight),96,128);
                    idleAnimations[row][col] = resizeSprite(idleSpriteSheet.getSubimage(col * spriteWidth, y, spriteWidth, spriteHeight),96, 128);
                }
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }
    
    private BufferedImage resizeSprite(BufferedImage image, int newWidth, int newHeight) 
    {
        Image temp = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(temp, 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }

    public void move(int dx, int dy) 
    {
        if (dx == 0 && dy == 0) {
            isMoving = false; // Stop walking
        } else {
            isMoving = true;
            lastDirection = getDirection(dx, dy);
            animationRow = lastDirection;
        }

        double length = Math.sqrt(dx * dx + dy * dy);
        if (length != 0) {
            dx = (int) Math.round((dx / length) * moveSpeed);
            dy = (int) Math.round((dy / length) * moveSpeed);
        }

        worldX += dx;
        worldY += dy;
    }

    private int getDirection(int dx, int dy) {
        if (dx > 0 && dy == 0) return 5; // Right
        else if (dx < 0 && dy == 0) return 1; // Left
        else if (dx == 0 && dy > 0) return 0; // Down
        else if (dx == 0 && dy < 0) return 3; // Up
        else if (dx > 0 && dy < 0) return 4; // Up-Right
        else if (dx < 0 && dy < 0) return 2; // Up-Left
        else if (dx > 0 && dy > 0) return 5; // Diagonal Down-Right (Don't change)
        else return 1; // Diagonal Down-Left (Don't change)
    }

    public void updateAnimation() 
    {
        frameCounter++;
        if (frameCounter >= frameDelay) 
        {
            currentFrame = (currentFrame + 1) % 6; // Loop animation frames
            frameCounter = 0;
        }
    }
    

    public void draw(Graphics g, int screenX, int screenY) {
        BufferedImage[][] selectedAnimation = isMoving ? walkAnimations : idleAnimations;
        g.drawImage(selectedAnimation[lastDirection][currentFrame], screenX, screenY, null);
    }

    public int getWorldX() {
        return worldX;
    }

    public int getWorldY() {
        return worldY;
    }
}