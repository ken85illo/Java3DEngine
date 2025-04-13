package com.epic;

import com.epic.Game;

public class Screen extends Render {

    Render3D render;

    public Screen(int width, int height) {
        super(width, height);
        render = new Render3D(width, height);
    }

    public void render(Game game) {
        for (int i = 0; i < width * height; i++)
            pixels[i] = 0;

        render.floor(game);
        draw(render, 0, 0);
    }

}
