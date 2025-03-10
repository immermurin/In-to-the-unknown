package Inotia;

import java.awt.event.*;
import java.util.Set;

public class KeyHandler extends KeyAdapter 
{
    private final Set<Integer> keys; // Use `Set<Integer>` instead of `HashSet<Integer>`

    public KeyHandler(Set<Integer> keys) // Now accepts `Set<Integer>` for flexibility
    {
        this.keys = keys;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys.remove(e.getKeyCode());
    }
}
