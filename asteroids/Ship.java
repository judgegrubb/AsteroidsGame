package asteroids;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.*;

/**
 * Represents ship objects
 */
public class Ship extends Participant
{
    // The outline of the ship
    private Shape outline;

    // Constructs a ship
    public Ship ()
    {
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(20, 0);
        poly.lineTo(-20, 12);
        poly.lineTo(-12, 0);
        poly.lineTo(-20, -12);
        poly.closePath();
        outline = poly;
    }

    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose
     * is located.
     */
    public double getXNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getX();
    }

    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose
     * is located.
     */
    public double getYNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getY();
    }
    
    /**
     * Returns the direction that the ship is facing in radians
     */
    public double getDirection()
    {
        if (getYNose() > getY())
        {
            return Math.atan((getX() - getXNose())/ (getYNose() - getY())) + Math.PI/2;
        }
        else if (getYNose () < getY())
        {
            return Math.atan((getX() - getXNose())/ (getYNose() - getY())) - Math.PI/2;
        }
        else //YNose and Y must be equal (meaning ship is exactly horizontal)
        {
            //Facing to the right
            if (getXNose () > getX())
            {
                return 0;
            }
            else //Facing to the left
            {
                return Math.PI;
            }
        }
                   
    }
    
    /**
     * Returns the outline of the ship.
     */
    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    /**
     * Customizes the base move method by imposing friction
     */
    @Override
    public void move ()
    {
        super.move();
        friction();
    }
    
    /**
     * Changes the color of the ship
     */
    @Override
    public void draw (Graphics2D g)
    {
        g.setColor(Color.RED);
        super.draw(g);
    }
}