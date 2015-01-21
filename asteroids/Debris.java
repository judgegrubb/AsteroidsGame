package asteroids;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.util.Random;

public class Debris extends Participant
{
    // return the debris' outline
    private Shape outline;
    
    // return the debris' direction
    private double direction;
    
    /**
     * creates new debris based on its origin x and y
     * @param x
     * @param y
     */
    public Debris (double x, double y){
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(x,y);
        poly.lineTo(x+1,y);
        poly.lineTo(x+1, y+1);
        poly.lineTo(x, y+1);
        poly.closePath();
        outline = poly;
        Random random = new Random();
        direction = random.nextDouble() * 2 * Math.PI;
    }
    
    /**
     * return the debris' outline
     */
    @Override
    Shape getOutline ()
    {
        return outline;
    }
    
    /**
     * return the debris' direction
     * @return
     */
    protected double getDirection()
    {
        return direction;
    }
    
    /**
     * Changes the color of debris
     */
    @Override
    public void draw (Graphics2D g)
    {
        g.setColor(Color.YELLOW);
        super.draw(g);
    }
    
}
