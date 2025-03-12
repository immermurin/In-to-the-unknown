package Inotia;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerSelectionPanel extends JPanel {
    private final ScreenManager screenManager;
    private JLabel previewLabel;
    private BufferedImage[][] azkiIdleFrames, elainaIdleFrames;
    private String selectedCharacter = "Azki"; // Default selection
    private int currentFrame = 0;
    private Timer animationTimer;

    public PlayerSelectionPanel(ScreenManager screenManager) {
        this.screenManager = screenManager;
        setLayout(new GridBagLayout());
        
        loadSprites();
        
        JLabel titleLabel = new JLabel("Select Your Character");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        previewLabel = new JLabel(new ImageIcon(new BufferedImage(48, 64, BufferedImage.TYPE_INT_ARGB)));
        startAnimation();
        
        JButton azkiButton = createButton("Azki (Purple)");
        JButton elainaButton = createButton("Elaina (Blue)");
        JButton confirmButton = createButton("Confirm");
        JButton backButton = createButton("Back");
        JButton futureClass1 = createButton("Future Class 1");
        JButton futureClass2 = createButton("Future Class 2");
        
        azkiButton.addActionListener(e -> updatePreview("Azki"));
        elainaButton.addActionListener(e -> updatePreview("Elaina"));
        confirmButton.addActionListener(e -> screenManager.startGame(selectedCharacter));
        backButton.addActionListener(e -> screenManager.showMenu());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        
        gbc.gridy = 0; add(titleLabel, gbc);
        gbc.gridy = 1; add(previewLabel, gbc);
        gbc.gridy = 2; add(azkiButton, gbc);
        gbc.gridy = 3; add(elainaButton, gbc);
        gbc.gridy = 4; add(futureClass1, gbc);
        gbc.gridy = 5; add(futureClass2, gbc);
        gbc.gridy = 6; add(confirmButton, gbc);
        gbc.gridy = 7; add(backButton, gbc);
    }

    private void loadSprites() {
        try {
            BufferedImage azkiSheet = ImageIO.read(getClass().getClassLoader().getResource("res/Azki_idle.png"));
            BufferedImage elainaSheet = ImageIO.read(getClass().getClassLoader().getResource("res/Elaina_idle.png"));
            
            azkiIdleFrames = extractFrames(azkiSheet);
            elainaIdleFrames = extractFrames(elainaSheet);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage[][] extractFrames(BufferedImage spriteSheet) {
        int frameWidth = 48;
        int frameHeight = 64;
        int imageWidth = spriteSheet.getWidth();
        int imageHeight = spriteSheet.getHeight();
        int rows = imageHeight / frameHeight;
        int cols = imageWidth / frameWidth;

        BufferedImage[][] frames = new BufferedImage[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Ensure the subimage is within bounds
                if ((col + 1) * frameWidth <= imageWidth && (row + 1) * frameHeight <= imageHeight) {
                    frames[row][col] = resizeSprite(spriteSheet.getSubimage(col * frameWidth, row * frameHeight, frameWidth, frameHeight),96, 128);
                } else {
                    System.out.println("Skipping out-of-bounds frame: Row " + row + ", Col " + col);
                }
            }
        }
        return frames; 
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

    private void updatePreview(String character) {
        selectedCharacter = character;
    }

    private void startAnimation() {
        animationTimer = new Timer(true);
        animationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                currentFrame = (currentFrame + 1) % 6; // Loop through 6 frames
                BufferedImage frame = selectedCharacter.equals("Azki") ? azkiIdleFrames[0][currentFrame] : elainaIdleFrames[0][currentFrame];
                previewLabel.setIcon(new ImageIcon(frame));
            }
        }, 0, 150); // Adjust delay for animation speed
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Arial", Font.BOLD, 20));
        return button;
    }
}
