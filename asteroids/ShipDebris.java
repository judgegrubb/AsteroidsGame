package asteroids;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.Random;

public class ShipDebris extends Participant
{
    // the ship debri's outline
    private Shape outline;
    
    // direction the ship debris is going
    private double direction;
    
    /**
     * creates the ship debris based on origin's x and y
     * and it's x direction (dx) and y direction (dy)
     * @param x
     * @param y
     * @param dx
     * @param dy
     */
    public ShipDebris(double x, double y, double dx, double dy)
    {
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(x, y);
        poly.lineTo(x+dx, y+dy);
        outline = poly;
        Random random = new Random();
        direction = random.nextDouble() * 2 * Math.PI;
    }

    /**
     * return ship debri's outline
     */
    @Override
    Shape getOutline ()
    {
        // TODO Auto-generated method stub
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
        g.setColor(Color.RED);
        super.draw(g);
    }
    
}
