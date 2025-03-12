package Inotia;

import Entity.Player;
import java.awt.*;

public class PlayerHUD {
    private Player player;

    public PlayerHUD(Player player) {
        this.player = player;
    }

    public void draw(Graphics g) {
        if (player == null) return;

        int hpBarWidth = 200;
        int mpBarWidth = 200; // Same width for MP bar
        int barHeight = 20;

        // HP Bar
        g.setColor(Color.RED);
        int currentHPWidth = (int) ((player.getHP() / (double) player.getMaxHP()) * hpBarWidth);
        g.fillRect(20, 20, currentHPWidth, barHeight);
        g.setColor(Color.WHITE);
        g.drawRect(20, 20, hpBarWidth, barHeight);

        // MP Bar (Blue)
        g.setColor(Color.BLUE); // Make MP bar blue
        int currentMPWidth = (int) ((player.getMP() / (double) player.getMaxMP()) * mpBarWidth);
        g.fillRect(20, 50, currentMPWidth, barHeight);
        g.setColor(Color.WHITE);
        g.drawRect(20, 50, mpBarWidth, barHeight);

        // Display text for HP & MP
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.setColor(Color.WHITE);
        g.drawString("HP: " + player.getHP() + "/" + player.getMaxHP(), 30, 35);
        g.drawString("MP: " + player.getMP() + "/" + player.getMaxMP(), 30, 65);
    }
}
