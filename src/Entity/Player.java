package Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class Player {
    protected int worldX, worldY;
    protected int moveSpeed = 4;

    // Player stats
    protected int maxHP;
    protected int currentHP;
    protected int attackPower; // Can be used in combat
    
    protected int maxMP;
    protected int currentMP;

    // Sprite handling
    private BufferedImage walkSpriteSheet;
    private BufferedImage idleSpriteSheet;
    private BufferedImage[][] walkAnimations; 
    private BufferedImage[][] idleAnimations; 
    
    private int currentFrame = 0;
    private int animationRow = 0; 
    private int frameDelay = 10; 
    private int frameCounter = 0;
    
    private boolean isMoving = false;
    private int lastDirection = 0;

    public Player(String walkSpritePath, String idleSpritePath, int maxHP, int attackPower, int maxMP) {
        this.worldX = 0;
        this.worldY = 0;

        this.maxHP = maxHP;
        this.currentHP = maxHP; // Start at full health
        this.attackPower = attackPower;

        this.maxMP = maxMP;
        this.currentMP = maxMP;  // Start MP at full

        loadSprites(walkSpritePath, idleSpritePath);
    }

    private void loadSprites(String walkPath, String idlePath) {
        try {
            walkSpriteSheet = ImageIO.read(getClass().getClassLoader().getResourceAsStream(walkPath));
            idleSpriteSheet = ImageIO.read(getClass().getClassLoader().getResourceAsStream(idlePath));

            walkAnimations = new BufferedImage[8][6];
            idleAnimations = new BufferedImage[8][6];

            int spriteWidth = 48;
            int spriteHeight = 64;
            int sheetHeight = walkSpriteSheet.getHeight();

            for (int row = 0; row < 8; row++) {
                int y = row * spriteHeight;
                if (y + spriteHeight > sheetHeight) break; 
                for (int col = 0; col < 6; col++) {
                    walkAnimations[row][col] = resizeSprite(walkSpriteSheet.getSubimage(col * spriteWidth, y, spriteWidth, spriteHeight), 96, 128);
                    idleAnimations[row][col] = resizeSprite(idleSpriteSheet.getSubimage(col * spriteWidth, y, spriteWidth, spriteHeight), 96, 128);
                }
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }
    
    private BufferedImage resizeSprite(BufferedImage image, int newWidth, int newHeight) {
        Image temp = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(temp, 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }

    public void move(int dx, int dy) {
        if (dx == 0 && dy == 0) {
            isMoving = false;
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
        if (dx > 0 && dy == 0) return 5;
        else if (dx < 0 && dy == 0) return 1;
        else if (dx == 0 && dy > 0) return 0;
        else if (dx == 0 && dy < 0) return 3;
        else if (dx > 0 && dy < 0) return 4;
        else if (dx < 0 && dy < 0) return 2;
        else if (dx > 0 && dy > 0) return 5;
        else return 1;
    }

    public void updateAnimation() {
        frameCounter++;
        if (frameCounter >= frameDelay) {
            currentFrame = (currentFrame + 1) % 6;
            frameCounter = 0;
        }
    }

    public void draw(Graphics g, int screenX, int screenY) {
        BufferedImage[][] selectedAnimation = isMoving ? walkAnimations : idleAnimations;
        g.drawImage(selectedAnimation[lastDirection][currentFrame], screenX, screenY, null);
    }

    // **HP & Stats Methods**
    public int getHP() {
        return currentHP;
    }
    
    public int getMaxHP() {
        return maxHP;
    }
    
    public int getMP() {
        return currentMP;
    }
    
    public int getMaxMP() {
        return maxMP;
    }

    public void takeDamage(int damage) {
        currentHP -= damage;
        if (currentHP < 0) currentHP = 0;
    }

    public void heal(int amount) {
        currentHP += amount;
        if (currentHP > maxHP) currentHP = maxHP;
    }

    public boolean isAlive() {
        return currentHP > 0;
    }

    public int getWorldX() {
        return worldX;
    }

    public int getWorldY() {
        return worldY;
    }
    
    public void useMP(int amount) {
        if (currentMP >= amount) {
            currentMP -= amount;
        } else {
            System.out.println("Not enough MP!");
        }
    }

    public void regenerateMP(int amount) {
        currentMP += amount;
        if (currentMP > maxMP) {
            currentMP = maxMP;
        }
    }
}
