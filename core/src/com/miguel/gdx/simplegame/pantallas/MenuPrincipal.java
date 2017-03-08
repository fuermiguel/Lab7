package com.miguel.gdx.simplegame.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.miguel.gdx.simplegame.Juego;

/**
 * Created by fuerm on 01/03/2017.
 */

public class MenuPrincipal implements Screen {
    //hgsdkfg

    final Juego juego;
    OrthographicCamera camera;

    public  MenuPrincipal(final Juego juego){
        this.juego = juego;

        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,480);


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        juego.batch.setProjectionMatrix(camera.combined);

        juego.batch.begin();
        juego.font.draw(juego.batch,"Bienvenido al Juego!!!",100,150);
        juego.font.draw(juego.batch,"Pulse donde quiera para comenzar",100,100);
        juego.batch.end();

        if(Gdx.input.isTouched()){
            juego.setScreen(new PantallaJuego(juego));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
