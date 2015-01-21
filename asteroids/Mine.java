package asteroids;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class Mine extends Participant
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
    public Mine (double d, double e)
    {
        setPosition(d,e);
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(0,0);
        poly.lineTo(10,0);
        poly.lineTo(10, 10);
        poly.lineTo(0, 10);
        poly.closePath();
        poly.moveTo(5, -3);
        poly.lineTo(13, 5);
        poly.lineTo(5, 13);
        poly.lineTo(-3, 5);
        poly.closePath();
        poly.moveTo(4, 4);
        poly.lineTo(6, 4);
        poly.lineTo(6, 6);
        poly.lineTo(4, 6);
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
    
    public double getCenterX()
    {
        Point2D.Double point = new Point2D.Double(originX + 5, originY + 5);
        transformPoint(point);
        return point.getX();
    }
    
    public double getCenterY()
    {
        Point2D.Double point = new Point2D.Double(originX + 5, originY + 5);
        transformPoint(point);
        return point.getY();
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
        g.setColor(Color.GREEN);
        super.draw(g);
    }

}
