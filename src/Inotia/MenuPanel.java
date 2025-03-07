package Inotia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel 
{
    private final ScreenManager screenManager;

    public MenuPanel(ScreenManager screenManager) 
    {
        this.screenManager = screenManager;
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);

        // Create buttons
        JButton playButton = createButton("Play");
        JButton settingsButton = createButton("Settings");
        JButton aboutButton = createButton("About");

        // Add action listeners
        playButton.addActionListener(e -> screenManager.startGame());
        settingsButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Settings coming soon!"));
        aboutButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Developed by Inotia Team"));

        // Arrange buttons in a vertical layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;

        gbc.gridy = 0; add(playButton, gbc);
        gbc.gridy = 1; add(settingsButton, gbc);
        gbc.gridy = 2; add(aboutButton, gbc);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Arial", Font.BOLD, 20));
        return button;
    }
}
