package Inotia;

import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;


public class BossFight extends javax.swing.JPanel implements KeyListener
{
    private int playerX, playerY;
    private int worldX = 0, worldY = 0;
    private int speed = 10;
    private final int worldWidth = 2000, worldHeight = 2000;
    private final Set<Integer> keysPressed = new HashSet<>();
    private Timer gameLoop;
    private final int playerSize = 128;

    // Character sprite animation
    private BufferedImage[] walkUpFrames, walkDownFrames, walkLeftFrames, walkRightFrames;
    private BufferedImage[] idleUpFrames, idleDownFrames, idleLeftFrames, idleRightFrames;
    private BufferedImage[] currentFrames;

    private int frameIndex = 0;
    private Timer animationTimer;

    private boolean isMoving = false;
    private int lastDirection = KeyEvent.VK_S; // Default facing down

    private JFrame parentFrame;

    public BossFight(JFrame parentFrame) 
    {
        initComponents();
        this.setFocusable(true);
        this.addKeyListener(this);
        this.parentFrame = parentFrame;
        this.requestFocusInWindow();
        this.setBackground(Color.black);
        
        loadCharacterSprites();
        
        playerX = getWidth() / 2 - playerSize / 2;
        playerY = getHeight() / 2 - playerSize / 2;
        

        
        gameLoop = new Timer(16, e -> updateMovement());
        gameLoop.start();
        
        // Animation loop for sprite frames
        animationTimer = new Timer(100, e -> {
            if (isMoving) {
                frameIndex = (frameIndex + 1) % currentFrames.length; // Cycle frames
            } else {
                frameIndex = 0; // Stay in the idle position
            }
            repaint();
        });
        animationTimer.start();
    }
    
    private void loadCharacterSprites() {
        try {
            walkUpFrames = loadSpriteFrames("/Inotia/Characters/Female/Walk/walk_Up.png");
            walkDownFrames = loadSpriteFrames("/Inotia/Characters/Female/Walk/walk_Down.png");
            walkLeftFrames = loadSpriteFrames("/Inotia/Characters/Female/Walk/walk_Left_Down.png");
            walkRightFrames = loadSpriteFrames("/Inotia/Characters/Female/Walk/walk_Right_Down.png");
            
            idleUpFrames = loadSpriteFrames("/Inotia/Characters/Female/Idle/Idle_Up.png");
            idleDownFrames = loadSpriteFrames("/Inotia/Characters/Female/Idle/Idle_Down.png");
            idleLeftFrames = loadSpriteFrames("/Inotia/Characters/Female/Idle/Idle_Left_Down.png");
            idleRightFrames = loadSpriteFrames("/Inotia/Characters/Female/Idle/Idle_Right_Down.png");

            currentFrames = walkDownFrames;
            System.out.println("All sprite animations loaded successfully!");
        } catch (Exception e) {
            System.err.println("Error loading character sprites: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private BufferedImage[] loadSpriteFrames(String path) throws IOException {
        InputStream imgStream = getClass().getResourceAsStream(path);
        if (imgStream == null) {
            throw new IOException("Sprite file not found: " + path);
        }

        BufferedImage spriteSheet = ImageIO.read(imgStream);
        int frameWidth = 48;
        int frameHeight = 64;
        int frameCount = 8; // Since the full width is 384px

        BufferedImage[] frames = new BufferedImage[frameCount];

        for (int col = 0; col < frameCount; col++) {
            frames[col] = spriteSheet.getSubimage(col * frameWidth, 0, frameWidth, frameHeight);
        }

        return frames;
    }

    private void updateMovement() 
    {
        boolean wasMoving = isMoving;
        isMoving = false;
        int dx = 0, dy = 0;

        if (keysPressed.contains(KeyEvent.VK_W)) { 
            dy -= speed;
            isMoving = true;
            currentFrames = walkUpFrames;
            lastDirection = KeyEvent.VK_W;
        }
        if (keysPressed.contains(KeyEvent.VK_S)) { 
            dy += speed;
            isMoving = true;
            currentFrames = walkDownFrames;
            lastDirection = KeyEvent.VK_S;
        }
        if (keysPressed.contains(KeyEvent.VK_A)) { 
            dx -= speed;
            isMoving = true;
            currentFrames = walkLeftFrames;
            lastDirection = KeyEvent.VK_A;
        }
        if (keysPressed.contains(KeyEvent.VK_D)) { 
            dx += speed;
            isMoving = true;
            currentFrames = walkRightFrames;
            lastDirection = KeyEvent.VK_D;
        }

        // Normalize diagonal movement
        if (dx != 0 && dy != 0) {
            dx *= 0.707;
            dy *= 0.707;
        }

        worldX += dx;
        worldY += dy;

        // If movement just stopped, switch to idle animation
        if (!isMoving && wasMoving) {
            switch (lastDirection) {
                case KeyEvent.VK_W:
                    currentFrames = idleUpFrames;
                    break;
                case KeyEvent.VK_S:
                    currentFrames = idleDownFrames;
                    break;
                case KeyEvent.VK_A:
                    currentFrames = idleLeftFrames;
                    break;
                case KeyEvent.VK_D:
                    currentFrames = idleRightFrames;
                    break;
            }
            frameIndex = 0; // Reset frame index so idle animation starts fresh
        }

        repaint();
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
    
    public void stopGame() 
    {
        gameLoop.stop();
        animationTimer.stop();
    }
    
    @Override
    public void addNotify() 
    {
        super.addNotify();
        requestFocusInWindow(); // Ensure focus is requested when the panel is added
    }
    
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        playerX = getWidth() / 2 - playerSize / 2;
        playerY = getHeight() / 2 - playerSize / 2;

        g.setColor(Color.black);
        g.fillRect(worldX, worldY, worldWidth, worldHeight);

        int circleX = 1000 - worldX;
        int circleY = 1000 - worldY;
        int circleSize = 50;

        g.setColor(Color.RED);
        g.fillOval(circleX, circleY, circleSize, circleSize);

        BufferedImage currentFrame = currentFrames[frameIndex];

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
            .addGap(0, 1232, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 791, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}