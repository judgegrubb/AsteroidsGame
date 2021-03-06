package asteroids;

/**
 * Must be implemented by objects wishing to receive notifications of
 * collisions.
 */
public interface CollisionListener
{
    /**
     * Reports that the two participants have collided
     */
    public void collidedWith (Participant p1, Participant p2);
}
