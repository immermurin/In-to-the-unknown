package Inotia;

import javax.swing.*;


public class Main 
{
    public static void main(String[] args) 
    {
        JFrame window = new JFrame("Game");
        BossFight b = new BossFight(window);
        
        window.setUndecorated(true); //Full screen
        window.setExtendedState(JFrame.MAXIMIZED_BOTH); //Full screen
        
        window.add(b);
        //window.pack();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        
        window.setVisible(true);
        b.requestFocusInWindow();
    } 
}
