package Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class Enemy {
    protected int worldX, worldY;
    protected int moveSpeed = 2;
    private BufferedImage[][] walkAnimations;
    private BufferedImage walkSpriteSheet;
    private int currentFrame = 0;
    private int animationRow = 0;
    private int frameDelay = 10;
    private int frameCounter = 0;

    public Enemy(String spritePath, int worldX, int worldY) {
        this.worldX = worldX;
        this.worldY = worldY;
        loadSprites(spritePath);
    }

    private void loadSprites(String walkPath) {
        try {
            walkSpriteSheet = ImageIO.read(getClass().getClassLoader().getResourceAsStream(walkPath));
            walkAnimations = new BufferedImage[8][4]; // 8 rows, 6 columns


            int spriteWidth = walkSpriteSheet.getWidth() / 8;  // 4 columns
            int spriteHeight = walkSpriteSheet.getHeight() / 4;
            
            int sheetHeight = walkSpriteSheet.getHeight(); // Ensure within image bounds

            for (int row = 0; row < 8; row++) {
                int y = row * spriteHeight;
                if (y + spriteHeight > sheetHeight) break; // Prevent out-of-bounds error
                for (int col = 0; col < 4; col++) 
                {
                    walkAnimations[row][col] = resizeSprite(walkSpriteSheet.getSubimage(col * spriteWidth, y, spriteWidth, spriteHeight),128,128);
                }
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void chasePlayer(int playerX, int playerY) {
        int dx = playerX - worldX;
        int dy = playerY - worldY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            worldX += (int) (moveSpeed * (dx / distance));
            worldY += (int) (moveSpeed * (dy / distance));
        }

        updateAnimationDirection(dx, dy);
    }

    private void updateAnimationDirection(int dx, int dy) 
    {
        // Only update direction if the movement is significant
        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 5) animationRow = 3; // Moving Right
            else if (dx < -5) animationRow = 2; // Moving Left
        } else {
            if (dy > 5) animationRow = 0; // Moving Down
            else if (dy < -5) animationRow = 1; // Moving Up
        }
    }


    public void updateAnimation() 
    {
        frameCounter++;
        if (frameCounter >= frameDelay) {
            currentFrame = (currentFrame + 1) % walkAnimations[animationRow].length; // Use actual array length
            frameCounter = 0;
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

    public void draw(Graphics g, int screenX, int screenY) {
        if (walkAnimations != null && walkAnimations.length > animationRow 
            && walkAnimations[animationRow].length > currentFrame) {

            g.drawImage(walkAnimations[animationRow][currentFrame], screenX, screenY, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(screenX, screenY, 64, 64);
        }
    }

    
    public int getWorldX() {
        return worldX;
    }

    public int getWorldY() {
        return worldY;
    }
}
