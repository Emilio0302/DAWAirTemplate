/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pedro.ieslaencanta.com.dawairtemplate.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import pedro.ieslaencanta.com.dawairtemplate.model.sprites.SpriteMove;

/**
 *
 * @author emili
 */
public class Bullet extends SpriteMove {
    
    private Image image; 
    private Direction direction;
    private static String ImagePath = "/bullets/bullet_rigth.png";
    
    public Bullet(Size s,Coordenada p, Rectangle board,Direction d){
        super(8,s,p,true,true,board);
        this.image = new Image(this.getClass().getResource(ImagePath).toString());
        this.direction = d;
    }
    @Override
    public void draw(GraphicsContext gc) {
       int x = this.getPosicion().getX();
       int y = this.getPosicion().getY();
       gc.drawImage(this.getImage(), 0, 0, 12, 3, x, y, 12, 3);
    }
    public void move(){
        this.move(direction);
    }

    /**
     * @return the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * @return the direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * @return the ImagePath
     */
    public static String getImagePath() {
        return ImagePath;
    }

    /**
     * @param aImagePath the ImagePath to set
     */
    public static void setImagePath(String aImagePath) {
        ImagePath = aImagePath;
    }
    
}
