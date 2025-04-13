package com.epic;

import java.awt.event.KeyEvent;

import com.epic.Controller;

public class Game {

    public int time;
    public Controller controls;

    public Game() {
        controls = new Controller();
    }

    public void tick(boolean[] key) {
        time++;
        boolean forward = key[KeyEvent.VK_W];
        boolean backward = key[KeyEvent.VK_S];
        boolean right = key[KeyEvent.VK_D];
        boolean left = key[KeyEvent.VK_A];
        boolean jump = key[KeyEvent.VK_SPACE];
        boolean crouch = key[KeyEvent.VK_CONTROL];
        boolean run = key[KeyEvent.VK_SHIFT];

        controls.tick(forward, backward, right, left, jump, crouch, run);
    }
}
