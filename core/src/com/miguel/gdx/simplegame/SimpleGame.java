package com.miguel.gdx.simplegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class SimpleGame extends ApplicationAdapter {

    private Texture gotaImagen, cuboImagen;
    private Sound gotaSonido; //Sonidos de menos de 10 segundos
    private Music lluviaSonido;//Resto de sonidos
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Rectangle rectanguloCubo; //Almacenamiento de posición y tamaño.
    private Array<Rectangle> rectangulosGotasLluvia;//Almacena los rectangulos de cada una de las gotas generadas.
    private long tiempoDesdeUltimaGota;
    private int marcador;
    private String textoMarcador;
    private BitmapFont tipoLetraMarcador;

    @Override
    public void create() {
        //Cargamos las imagenes de la gota y el cubo, 64x64 pixels cada una
        gotaImagen = new Texture(Gdx.files.internal("gota.png"));
        cuboImagen = new Texture(Gdx.files.internal("cubo.png"));

        //Cargamos los efectos de sonido de la lluvia y la gota.
        gotaSonido = Gdx.audio.newSound(Gdx.files.internal("gota.wav"));
        lluviaSonido = Gdx.audio.newMusic(Gdx.files.internal("lluvia.mp3"));

        //Arrancamos el sonido de la lluvia(lo primero al cargar el juego)
        lluviaSonido.setLooping(true);
        lluviaSonido.play();

        //OrthograficCamera nos muestra un área del juego
        camera = new OrthographicCamera();
        //Definimos el tamaño del área a mostrar y el posicionamiento.En este caso centrado
        camera.setToOrtho(false, 800, 480);

        batch = new SpriteBatch();

        //clase rectangle que almacena posición y tamaño de la textura a utilizar.
        rectanguloCubo = new Rectangle();
        rectanguloCubo.x = 800 / 2 - 64 / 2;
        rectanguloCubo.y = 20;
        rectanguloCubo.width = 64;
        rectanguloCubo.height = 64;

        //Instanciamos el array rectangulosGotasLluvia
        rectangulosGotasLluvia = new Array<Rectangle>();
        generGotaLluvia();

        //Inicializamos el marcador
        marcador = 0;
        textoMarcador = "Marcador: 0";
        tipoLetraMarcador = new BitmapFont();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Limpiamos la matriz de posicionado de la cámara.(no es necesario en este
        // juego pero es recomendable hacerlo siempre.
        camera.update();

        //Asignamos la matriz de proyección al batch
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        //Añadimos el marcador
        tipoLetraMarcador.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        tipoLetraMarcador.draw(batch, textoMarcador, 10, 450);
        batch.draw(cuboImagen, rectanguloCubo.x, rectanguloCubo.y);
        for (Rectangle rectanguloGota : rectangulosGotasLluvia) {
            batch.draw(gotaImagen, rectanguloGota.x, rectanguloGota.y);
        }
        batch.end();


        if (Gdx.input.isTouched()) {
            //Utilizamos un vector tridimiensional con la Z=0 para almacenar las coordenadas de
            // donde se ha tocado la pantalla.
            Vector3 posicionTocada = new Vector3();
            /*
            Podemos tener un problema instaciando constantemente a Vector3. Podríamos utilizar
            un patrón Singleton para instanciar el mismo objeto cada vez. todo ??????
            Otra manera es que posicionTocada se un campo de la clase gota.
             */
            posicionTocada.set(Gdx.input.getX(), Gdx.input.getY(), 0);

            //Trasforma las cordenadas del punto tocado en la pantalla a
            // las coordenadas del systema de camera.
            camera.unproject(posicionTocada);
            rectanguloCubo.x = posicionTocada.x - (64 / 2);
            //Añadimos el movimiento en vertical
            rectanguloCubo.y = posicionTocada.y - (64 / 2);
        }
        //Código para el movimiento del cubo con el teclado.
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            rectanguloCubo.x -= 300 * Gdx.graphics.getDeltaTime();
        //Deltatime es el tiempo entre frames. sería 1/60 Por lo que cada segundo (200*(1/60))*60 = 200.
        //Buena manera de conseguir número de pixeles por segundo.
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            rectanguloCubo.x += 300 * Gdx.graphics.getDeltaTime();
        //Añadimos movimineto vertical por teclado
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            rectanguloCubo.y += 300 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            rectanguloCubo.y -= 300 * Gdx.graphics.getDeltaTime();


        //Ahora hay que poner los límites para que el cubo no los sobrepase.
        if (rectanguloCubo.x < 0) rectanguloCubo.x = 0;
        if (rectanguloCubo.x > 800 - 64) rectanguloCubo.x = 800 - 64;
        //Añadimos limites para el movimiento vertical
        if (rectanguloCubo.y < 0) rectanguloCubo.y = 0;
        if (rectanguloCubo.y > 400 - 64) rectanguloCubo.x = 400 - 64;

        //Vemos si ha pasado el tiempo necesario(1segundo) para generar otra gota
        if (TimeUtils.nanoTime() - tiempoDesdeUltimaGota > 1000000000) generGotaLluvia();

        //Tenemos que mover las gotas a 20pixels por segundo. Las gotas las tenemos almacenadas
        //en el array. Movemos cada una de las gotas.
        Iterator<Rectangle> iter = rectangulosGotasLluvia.iterator();
        while (iter.hasNext()) {
            Rectangle rectanguloGotaLluvia = iter.next();
            rectanguloGotaLluvia.y -= 200 * Gdx.graphics.getDeltaTime();
            //Cuando la gota desaparece por abajo la borramos.
            if (rectanguloGotaLluvia.y + 64 < 0) {
                iter.remove();
                if (marcador > 0) {
                    marcador--;
                    textoMarcador = "Marcador: " + marcador;
                }
            }
            //Hay que poner un sonido a cuando la gota choca con el cubo y borrar la gota
            //Lo que comprobamos es si los rectangulos se superponen
            if (rectanguloGotaLluvia.overlaps(rectanguloCubo)) {
                marcador++;
                textoMarcador = "Marcador: " + marcador;
                gotaSonido.play();
                iter.remove();
            }
        }

    }

    /*Método de genearción de gotas de lluvia*/
    private void generGotaLluvia() {
        Rectangle rectanguloGota = new Rectangle();
        rectanguloGota.x = MathUtils.random(0, 800 - 64);
        rectanguloGota.y = 480;
        rectanguloGota.width = 64;
        rectanguloGota.height = 64;
        rectangulosGotasLluvia.add(rectanguloGota);
        tiempoDesdeUltimaGota = TimeUtils.nanoTime();
    }

    @Override
    public void dispose() {
        super.dispose();
        gotaImagen.dispose();
        cuboImagen.dispose();
        gotaSonido.dispose();
        lluviaSonido.dispose();
        batch.dispose();
    }
}
