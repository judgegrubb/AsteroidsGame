package asteroids;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.util.Timer;

public class Bullet extends Participant
{
    // bullet's coordinates
    private double originX;
    private double originY;
    
    // bullet's outline
    private Shape outline;
    
    // Ship the bullet came from
    private Ship ship;
    
    /**
     * create a new bullet based on the constructor
     * @param d
     * @param e
     */
    public Bullet (double d, double e)
    {
        originX = d;
        originY = d;
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(d,e);
        poly.lineTo(d+1,e);
        poly.lineTo(d+1, e+1);
        poly.lineTo(d, e+1);
        poly.closePath();
        outline = poly;
    }

    /**
     * return the outline of the bullet
     */
    @Override
    protected Shape getOutline ()
    {
        return outline;
    }
    
    /**
     * return the bullet's x coord
     * @return
     */
    protected double getOriginX()
    {
        return originX;
    }
    
    /**
     * return the bullet's y coord
     * @return
     */
    protected double getOriginY()
    {
        return originY;
    }
    
    
    /**
     * Changes the color of bullets
     */
    @Override
    public void draw (Graphics2D g)
    {
        g.setColor(Color.MAGENTA);
        super.draw(g);
    }
    
}
