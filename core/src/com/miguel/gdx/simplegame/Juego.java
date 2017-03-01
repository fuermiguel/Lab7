package com.miguel.gdx.simplegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by fuerm on 01/03/2017.
 */

public class Juego extends Game {

    public SpriteBatch batch;
    public BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();

        //Por defecto LibGDX usa Arial font.
        font = new BitmapFont();

        //Asignamos la pantalla actual.
        this.setScreen(new MenuPrincipal(this));
    }
    public void render(){
        super.render();
    }
    public void dispose(){
        batch.dispose();
        font.dispose();
    }
}
