package com.epic;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.epic.Screen;
import com.epic.Controller;
import com.epic.InputHandler;

public class Display extends Canvas implements Runnable {
    private static final long serialVersionUID = 1L;

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final String TITLE = "Game Pre-Alpha 0.01";

    private Thread thread;
    private Screen screen;
    private Game game;
    private JFrame frame;
    private InputHandler input;
    private BufferedImage img;

    private int newX = 0, oldX = 0;
    private int[] pixels;
    private boolean running = false;

    public Display() {
        Dimension size = new Dimension(WIDTH, HEIGHT);
        frame = new JFrame();
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        screen = new Screen(WIDTH, HEIGHT);
        game = new Game();
        img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

        input = new InputHandler();
        addKeyListener(input);
        addFocusListener(input);
        addMouseListener(input);
        addMouseMotionListener(input);
    }

    public void start() {
        if (running)
            return;

        running = true;
        thread = new Thread(this);
        thread.start();

        System.out.println("Starting!");
    }

    @Override
    public void run() {
        int frames = 0;
        double unprocessedSeconds = 0;
        long previousTime = System.nanoTime();
        double secondsPerTick = 1 / 60.0;
        int tickCount = 0;
        boolean ticked = false;

        while (running) {
            long currentTime = System.nanoTime();
            long passedTime = currentTime - previousTime;
            previousTime = currentTime;
            unprocessedSeconds += passedTime / 1_000_000_000.0;
            requestFocus();

            while (unprocessedSeconds > secondsPerTick) {
                tick();
                unprocessedSeconds -= secondsPerTick;
                ticked = true;
                tickCount++;
                if (tickCount % 60 == 0) {
                    frame.setTitle(TITLE + "  [ " + tickCount + " tps, " + frames + " fps ]");
                    previousTime += 1000;
                    tickCount = 0;
                    frames = 0;
                }
            }
            if (ticked) {
                render();
                frames++;
            }
            render();
            frames++;

            newX = InputHandler.MouseX;
            if (newX > oldX) {
                Controller.turnRight = true;
            }
            if (newX < oldX) {
                Controller.turnLeft = true;
            }
            if (newX == oldX) {
                Controller.turnRight = false;
                Controller.turnLeft = false;
            }
            oldX = newX;
        }
    }

    public void tick() {
        game.tick(input.key);
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.render(game);

        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            pixels[i] = screen.pixels[i];
        }

        Graphics g = bs.getDrawGraphics();
        g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0, 0), "blank");
        Display display = new Display();

        display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.frame.add(display);
        display.frame.setTitle(TITLE);
        display.frame.setResizable(false);
        display.frame.getContentPane().setCursor(blank);
        display.frame.pack();
        display.frame.setLocationRelativeTo(null);
        display.frame.setVisible(true);

        System.out.println("Running!");

        display.start();
    }

}
