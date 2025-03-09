package Inotia;

import javax.swing.*;
import java.awt.*;

public class PlayerSelectionPanel extends JPanel 
{
    private final ScreenManager screenManager;

    public PlayerSelectionPanel(ScreenManager screenManager) 
    {
        this.screenManager = screenManager;
        setLayout(new GridBagLayout());

        JLabel titleLabel = new JLabel("Select Your Character");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JButton azkiButton = createButton("Azki (Purple)");
        JButton elainaButton = createButton("Elaina (Blue)");
        JButton backButton = createButton("Back");

        azkiButton.addActionListener(e -> screenManager.startGame("Azki"));
        elainaButton.addActionListener(e -> screenManager.startGame("Elaina"));
        backButton.addActionListener(e -> screenManager.showMenu());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;

        gbc.gridy = 0; add(titleLabel, gbc);
        gbc.gridy = 1; add(azkiButton, gbc);
        gbc.gridy = 2; add(elainaButton, gbc);
        gbc.gridy = 3; add(backButton, gbc);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Arial", Font.BOLD, 20));
        return button;
    }
}
