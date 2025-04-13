package com.epic;

import com.epic.Game;
import com.epic.Controller;

public class Render3D extends Render {

    public double zBuffer[];
    private double renderDistance = 5000;
    private Render floor;

    public Render3D(int width, int height) {
        super(width, height);
        zBuffer = new double[width * height];
        floor = Texture.loadBitmap("/textures/floor.png");
    }

    public void floor(Game game) {

        double floorPosition = 15;
        double ceilingPosition = 800;
        double rotation = game.controls.rotation;
        double forward = game.controls.z;
        double right = game.controls.x;
        double up = game.controls.y;
        double bobbing = Math.sin(game.time / 45.0) * 0.5;

        if (Controller.walk) {
            bobbing = Math.sin(game.time / 6.0) * 0.5;
        }

        if (Controller.crouch) {
            bobbing = Math.sin(game.time / 7.0) * 0.25;
        }

        if (Controller.run && !Controller.crouch && Controller.forward) {
            bobbing = Math.sin(game.time / 4.0) * 0.5;
        }

        double sine = Math.sin(rotation);
        double cosine = Math.cos(rotation);

        for (int y = 0; y < height; y++) {
            double ceiling = (y - height / 2.0) / height;
            double z = (floorPosition + up + bobbing) / ceiling;

            if (ceiling < 0) {
                z = ceilingPosition / -ceiling;
            }

            for (int x = 0; x < width; x++) {
                double depth = (x - width / 2.0) / height;
                depth *= z;
                double xx = depth * cosine + z * sine;
                double yy = z * cosine - depth * sine;
                int xPix = (int) (xx + right);
                int yPix = (int) (yy + forward);
                zBuffer[x + y * width] = z;
                pixels[x + y * width] = floor.pixels[(xPix & 7) + (yPix & 7) * 8];
                pixels[x + y * width] = renderDistanceLimiter(x + y * width);
            }
        }
    }

    public int renderDistanceLimiter(int index) {
        int brightness = (int) (renderDistance / zBuffer[index]);

        if (brightness < 0) {
            brightness = 0;
        }

        if (brightness > 255) {
            brightness = 255;
        }

        int r = (pixels[index] >> 16) & 0xFF;
        int g = (pixels[index] >> 8) & 0xFF;
        int b = (pixels[index]) & 0xFF;

        r = r * brightness >>> 8;
        g = g * brightness >>> 8;
        b = b * brightness >>> 8;

        return r << 16 | g << 8 | b;
    }

}
