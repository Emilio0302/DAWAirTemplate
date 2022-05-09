/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pedro.ieslaencanta.com.dawairtemplate.model;

import java.util.ArrayList;
import java.util.function.Predicate;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import pedro.ieslaencanta.com.dawairtemplate.Background;
import pedro.ieslaencanta.com.dawairtemplate.IWarnClock;
import pedro.ieslaencanta.com.dawairtemplate.model.sprites.IDrawable;
import pedro.ieslaencanta.com.dawairtemplate.model.sprites.IKeyListener;


/**
 *
 * @author Pedro
 */
public class Level implements IDrawable, IWarnClock, IKeyListener {

    public enum Estado {
        PRE_STARTED,
        RUNNING,
        STOPPED,
        PAUSED,
        PRE_END,
        END
    }
    private static String[] msg = {"\"Pulsar una tecla para empezar", "Siguiente nivel..."};
    private String background_path;
    private int speed;
    private int position;
    private int fin;
    private Background background;
    
    private GraphicsContext bg_ctx;
    private MediaPlayer player;
    private float[] probabilidadenemigos;
    private Size s;
    private Fighter fighter;
    private Estado estado;
    private Player p;
    private ArrayList<Enemy> enemigos;
    private ArrayList<Bullet> balasEnemigos;
    
    public Level(String image_path, String music_path, Size s, int speed, Coordenada start_position, GraphicsContext bg_ctx, float[] probabilidad_enemigos,int fin) {
        this.background = new Background(image_path, s, speed, start_position);
        this.background.setBg(bg_ctx);
        this.position = 0;
        this.speed = speed;
        this.estado = Estado.PRE_STARTED;
        this.fin = fin;
        this.s = s;
        //crear el avion
          this.probabilidadenemigos = probabilidad_enemigos;
        this.initSound(music_path);
        this.fighter = new Fighter(
               3,
               new Size(74, 26),
               new Coordenada(20, s.getHeight() / 2),
               new Rectangle(new Coordenada(0, 0), new Coordenada(s.getWidth(), s.getHeight())));
        this.enemigos = new ArrayList();
        this.balasEnemigos = new ArrayList();
    }
    
    private void initSound(String music_path) {
        this.player = new MediaPlayer(new Media(getClass().getResource(music_path).toString()));

        player.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                player.seek(Duration.ZERO);
            }
        });
       
    }

    
    

    @Override
    public void draw(GraphicsContext gc) {
        
        this.background.paint(gc);
        this.fighter.draw(gc);
        if (this.estado == Estado.PRE_STARTED) {
            gc.setFill(Color.BROWN);
            gc.setStroke(Color.WHITE);
            gc.strokeText(Level.msg[0], 100, 200);
            gc.fillText(Level.msg[0], 100, 200);

        }
        this.fighter.draw(gc);
        this.enemigos.forEach(e -> e.draw(gc));
        this.balasEnemigos.forEach(b -> b.draw(gc));
    }

    @Override
    public void TicTac() {
        if (this.getEstado() == Estado.RUNNING) {
            //llamar a tictac de los hijos
           this.TicTacChildrens();
           //Este metodo genera los enemigos
           this.enemigosGenerados();
           //Este metodo detecta las colisiones
           this.detectCollisions();
            //posicion en la que termina
            if (this.position < this.fin) {
                this.position += this.speed;     
            } else {
                this.EndLevel();
            }
        }
    }

    
    private void enemigosGenerados() {
        int random = (int) (Math.random() * 60);
        int randomY = (int) (Math.random() * this.fighter.getBoard().getEnd().getY());
        int x = this.fighter.getBoard().getEnd().getX();
        if (random == 0) {
            Enemy tempo = new Enemy(new Size(62,28),new Coordenada(x, randomY), this.fighter.getBoard());
            this.enemigos.add(tempo);
            //System.out.println(this.enemigos.size());
        }
    }

    
    private void detectCollisions() {
        //se mira si las balas del avión le pegan a algún enemigo
        this.fighter.getBalas().forEach(b -> this.enemigos.forEach(e -> b.isCollision(e)));
        //ademá se borran los que se pasen por el lateral
        this.balasEnemigos.forEach(b -> b.isCollision(this.fighter));
    }

    private void TicTacChildrens() {
        //pintar el fondo
        this.background.TicTac();
        this.fighter.TicTac();
        
        this.enemigos.forEach(e -> e.TicTac());
        this.enemigos.removeIf(e -> e.getPosicion().getX() - e.getInc() <= e.getBoard().getStart().getX() || !e.isLive());

        moveBullets();
       
    }

    private void moveBullets() {
        //mover las balas
        //comprobar las condiciones para borrar y borrar las balas
        this.balasEnemigos.forEach(b -> b.move());
        this.balasEnemigos.removeIf(b -> b.getPosicion().getX() - b.getInc() <= b.getBoard().getStart().getX());
     
    }
   

  

    public boolean isEnd() {
        return this.getEstado() == Estado.STOPPED;
    }

    private void EndLevel() {
        this.player.stop();
        this.setEstado(Estado.END);
    }

    /**
     * @return the estado
     */
    public Estado getEstado() {
        return estado;
    }

    /**
     * @return the p
     */
    public Player getP() {
        return p;
    }

    /**
     * @param p the p to set
     */
    public void setP(Player p) {
        this.p = p;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public void onKeyPressed(KeyCode code) {
        //pasar el key code al avion
       this.fighter.onKeyPressed(code);
    }

    @Override
    public void onKeyReleased(KeyCode code) {
        //para iniciar el juego
        if (this.getEstado() == Level.Estado.PRE_STARTED) {
            this.setEstado(Level.Estado.RUNNING);
        }
        if (this.getEstado() == Level.Estado.RUNNING) {
            this.fighter.onKeyReleased(code);
            if (player.getStatus() == MediaPlayer.Status.READY) {
                player.play();
            }
        }

        
    }
}
