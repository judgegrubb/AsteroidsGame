package asteroids;

/**
 * Must be implemented by classes that wish to receive callbacks from a
 * CountdownTimer.
 */

public interface CountdownTimerListener
{
    /**
     * Called when a countdown timer reaches zero
     */
    public void timeExpired (Participant p);
}
