package simple.game;

import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;


public class GamePanel extends javax.swing.JPanel implements KeyListener
{
    private int playerX, playerY;
    private int worldX = 0, worldY = 0;
    private int speed = 10;
    private final int worldWidth = 2000, worldHeight = 2000;
    private final Set<Integer> keysPressed = new HashSet<>();
    private Timer gameLoop;
    private final int playerSize = 64;

    private final Rectangle portalTrial1 = new Rectangle(1000, 1000, 50, 50);
    private final Rectangle portalBossFight = new Rectangle(1500, 1500, 50, 50);

    // Character sprite animation
    private BufferedImage spriteSheet;
    private BufferedImage[] walkingFrames;
    private int frameIndex = 0;
    private Timer animationTimer;
    private int startFrame = 0, endFrame = 1;

    private boolean isMoving = false;
    private int directionRow = 2;
    private int lastDirection = 0;

    private JFrame parentFrame;
    
    public GamePanel(JFrame parentFrame) 
    {
        initComponents();
        this.setFocusable(true);
        this.addKeyListener(this);
        this.parentFrame = parentFrame;
        this.requestFocusInWindow();
        this.setBackground(Color.black);
        
        loadCharacterSprite();
        
        playerX = getWidth() / 2 - playerSize / 2;
        playerY = getHeight() / 2 - playerSize / 2;
        

        
        gameLoop = new Timer(16, e -> updateMovement());
        gameLoop.start();
        
        // Animation loop for sprite frames
        animationTimer = new Timer(80, e -> { // Adjusted for smoother transitions
            if (isMoving) {
                // Ensure frame stays in range
                if (frameIndex < startFrame || frameIndex > endFrame) {
                    frameIndex = startFrame;
                } else {
                    frameIndex++;
                    if (frameIndex > endFrame) {
                        frameIndex = startFrame; // Loop back
                    }
                }
            } else {
                // Keep last frame direction for better idle transitions
                frameIndex = (lastDirection == 3) ? 3 : lastDirection; 
            }
            repaint();
        });
        animationTimer.start();
    }
    
   private void loadCharacterSprite() {
    try {
            InputStream imgStream = getClass().getResourceAsStream("/simple/game/Character.png");

            if (imgStream == null) {
                throw new IOException("Sprite file not found in resources.");
            }

            spriteSheet = ImageIO.read(imgStream);

            int frameWidth = spriteSheet.getWidth() / 24; // 24 columns
            int frameHeight = spriteSheet.getHeight(); // Single row

            walkingFrames = new BufferedImage[24]; // 24 frames

            for (int col = 0; col < 24; col++) {
                walkingFrames[col] = spriteSheet.getSubimage(col * frameWidth, 0, frameWidth, frameHeight);
            }

            System.out.println("Sprite sheet loaded successfully!");

        } catch (IOException e) {
            System.err.println("Error loading character sprite: " + e.getMessage());
            e.printStackTrace();
            walkingFrames = new BufferedImage[1]; // Prevents NullPointerException
        }
    }
    

    private void updateMovement() 
    {
        isMoving = false;

        int dx = 0, dy = 0;

        if (keysPressed.contains(KeyEvent.VK_W)) { // Move UP
            dy += speed;
            isMoving = true;
            startFrame = 18; // Frames 19-24 (UP)
            endFrame = 23;
            lastDirection = 18;
        }
        if (keysPressed.contains(KeyEvent.VK_S)) { // Move DOWN
            dy -= speed;
            isMoving = true;
            startFrame = 0; // Frames 2-6 (DOWN)
            endFrame = 5;
            lastDirection = 0;
        }
        if (keysPressed.contains(KeyEvent.VK_A)) { // Move LEFT
            dx += speed;
            isMoving = true;
            startFrame = 12; // Frames 13-18 (LEFT)
            endFrame = 17;
            lastDirection = 12;
        }
        if (keysPressed.contains(KeyEvent.VK_D)) { // Move RIGHT
            dx -= speed;
            isMoving = true;
            startFrame = 6; // Frames 7-12 (RIGHT)
            endFrame = 11;
            lastDirection = 6;
        }

        // Normalize diagonal movement
        if (dx != 0 && dy != 0) 
        {
            dx *= 0.707; // 1 / sqrt(2)
            dy *= 0.707;
        }

        worldX += dx;
        worldY += dy;

        checkPortalCollision();
        repaint();
    }
    
    private void checkPortalCollision()
    {
        int playerWorldX = -worldX + playerX;
        int playerWorldY = -worldY + playerY;

        if (playerWorldX < portalTrial1.x + portalTrial1.width &&
            playerWorldX + playerSize > portalTrial1.x &&
            playerWorldY < portalTrial1.y + portalTrial1.height &&  
            playerWorldY + playerSize > portalTrial1.y) 
        {
            System.out.println("Player entered the Trial portal! Teleporting...");
            teleportToNewWorld(new GameStart(parentFrame));
        }
        
        if (playerWorldX < portalBossFight.x + portalBossFight.width &&
            playerWorldX + playerSize > portalBossFight.x &&
            playerWorldY < portalBossFight.y + portalBossFight.height &&
            playerWorldY + playerSize > portalBossFight.y) 
        {
            System.out.println("Player entered the Boss Fight portal! Teleporting...");
            teleportToNewWorld(new BossFight(parentFrame));
        }  
    }
    
    
    private void teleportToNewWorld(javax.swing.JPanel newPanel) 
    {
        gameLoop.stop();       // Stop game update loop
        animationTimer.stop(); // Stop animation loop Prevent from lagging
        
        parentFrame.getContentPane().removeAll();
        parentFrame.add(newPanel); // Switch to the new panel
        parentFrame.revalidate();
        parentFrame.repaint();
        
        newPanel.requestFocusInWindow();
    }
    
    
    @Override
    public void keyPressed(KeyEvent e) {
        keysPressed.add(e.getKeyCode());
    }
    
    @Override
    public void keyReleased(KeyEvent e)
    {
        keysPressed.remove(e.getKeyCode());
    }
    
    @Override
    public void keyTyped(KeyEvent e){}
    
    

    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        
        if (isMoving) {
            repaint(); 
        }

        playerX = getWidth() / 2 - playerSize / 2;
        playerY = getHeight() / 2 - playerSize / 2;

        g.setColor(Color.black);
        g.fillRect(worldX, worldY, worldWidth, worldHeight);

        g.setColor(Color.magenta);
        g.fillRect(portalTrial1.x + worldX, portalTrial1.y + worldY, portalTrial1.width, portalTrial1.height);
        
        g.setColor(Color.red);
        g.fillRect(portalBossFight.x + worldX, portalBossFight.y + worldY, portalBossFight.width, portalBossFight.height);

        BufferedImage currentFrame = walkingFrames[frameIndex]; 

        if (currentFrame != null) {
            g.drawImage(currentFrame, playerX, playerY, playerSize, playerSize, null);
        } else {
            System.out.println("Warning: Current frame is null!");
        }
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
