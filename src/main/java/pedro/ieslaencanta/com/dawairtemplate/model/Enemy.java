/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pedro.ieslaencanta.com.dawairtemplate.model;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import pedro.ieslaencanta.com.dawairtemplate.model.sprites.SpriteMove;

/**
 *
 * @author emili
 */
public class Enemy extends SpriteMove {

    protected Image image;
    protected static String ImagePath = "/enemigos/e1.png";
    private ArrayList<Bullet> balas;

    public Enemy(Size s, Coordenada p, Rectangle board) {
        super(5, s, p, true, true, board);
        this.image = new Image(this.getClass().getResource(ImagePath).toString());
        this.balas = new ArrayList<>();
    }
     public void TicTac() {
        this.move(Direction.LEFT);
        this.disparar();
    }

    public void disparar() {
        if ((int) (Math.random() * 40) == 0) {
            Bullet b = new Bullet(new Size(12, 3), new Coordenada(this.getPosicion().getX(), this.getPosicion().getY()), this.getBoard(), Direction.LEFT);
            this.balas.add(b);
            //System.out.println(this.balas.size());
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        int x = this.getPosicion().getX();
        int y = this.getPosicion().getY();
        gc.drawImage(this.image, 0, 0, 31, 14, x, y, 62, 28);
    }
    
    public void setBullets(ArrayList<Bullet> balas) {
        this.balas = balas;
    }
    
}
