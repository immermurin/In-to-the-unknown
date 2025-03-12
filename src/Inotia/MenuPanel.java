package Inotia;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.sound.sampled.*;

public class MenuPanel extends JPanel {
    private final ScreenManager screenManager;

    public MenuPanel(ScreenManager screenManager) 
    {
        this.screenManager = screenManager;
        setLayout(new GridBagLayout());

        JButton playButton = createButton("Play");
        JButton settingsButton = createButton("Settings");
        JButton aboutButton = createButton("About");
        JButton exitButton = createButton("Exit");

        playButton.addActionListener(e -> screenManager.showPlayerSelection());
        settingsButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Settings coming soon!"));
        aboutButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Developed by Inotia Team"));
        exitButton.addActionListener(e -> System.exit(0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;

        gbc.gridy = 0; add(playButton, gbc);
        gbc.gridy = 1; add(settingsButton, gbc);
        gbc.gridy = 2; add(aboutButton, gbc);
        gbc.gridy = 3; add(exitButton, gbc);
    }

    private JButton createButton(String text) 
    {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Arial", Font.BOLD, 20));
        return button;
    }
}
