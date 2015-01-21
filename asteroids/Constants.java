package asteroids;

/**
 * Provides constants governing the game
 * 
 */

public class Constants
{
    /**
     * The height and width of the game area.
     */
    public final static int SIZE = 750;

    /**
     * Game title
     */
    public final static String TITLE = "CS 1410 Asteroids";

    /**
     * Label on start game button
     */
    public final static String START_LABEL = "Start Game";

    /**
     * Speed beyond which participants may not accelerate
     */
    public final static double SPEED_LIMIT = 15;

    /**
     * Limit of number of mines you can place
     */
    public final static int MINE_LIMIT = 1;
    
    /**
     * limit of number of mines you can place after using the cheat code doctor octopus
     */
    public final static int CHEAT_MINE_LIMIT = 3;
    
    /**
     * Amount of "friction" that can be applied to ships so that they eventually
     * stop. Should be negative.
     */
    public final static double FRICTION = -0.05;

    /**
     * The number of milliseconds between the beginnings of frame refreshes
     */
    public final static int FRAME_INTERVAL = 33;

    /**
     * The number of milliseconds between the end of a life and the display of
     * the next screen.
     */
    public final static int END_DELAY = 2500;

    /**
     * The offset in pixels from the edges of the screen of newly-placed
     * asteroids.
     */
    public final static int EDGE_OFFSET = 100;

    /**
     * The game over message
     */
    public final static String GAME_OVER = "Game Over";

    /**
     * Number of asteroids that must be destroyed to complete a level.
     */
    public final static int ASTEROID_COUNT = 28;

    /**
     * Duration in milliseconds of a bullet before it disappears.
     */
    public final static int BULLET_DURATION = 1000;

    /**
     * Speed, in pixels per frame, of a bullet.
     */
    public final static int BULLET_SPEED = 20;
    
    /**
     * Number of bullets that can appear on the screen at the same time
     */
    public final static int INITIAL_BULLET_LIMIT = 8;
    
    /**
     * Amount of lives added when cheat code is applied
     */
    public final static int CHEAT_LIVES = 5;
    
    /**
     * Amount of bullets available when user inputs correct cheat code
     */
    public final static int CHEAT_BULLET = 40;
    
    /**
     * Sharpshooter bonus for added life (accuracy percentage)
     */
    public final static double SHARP_SHOOTER = 55;
    
    /**
     * Score user must obtain to receive a bonus life. Resets every time it is obtained
     */
    public final static int BONUS_LIFE = 1500;
    
    /**
     * Speed, in pixels per frame, of debris
     */
    public final static double DEBRIS_SPEED = 2;
   
    /**
     * Amount of debris particles that result from a collision
     */
    public final static int DEBRIS_AMOUNT = 10;
    
    /**
     * Duration in milliseconds of the debris
     */
    public final static int DEBRIS_DURATION = 2000;
    
    /**
     * Scaling factors used for asteroids of size 0, 1, and 2.
     */
    public final static double[] ASTEROID_SCALE = { 0.5, 1.0, 2.0 };
    
    /**
     * Speed of rotation of ship
     */
    public final static double ROTATION_SPEED = Math.PI/16;
    
    /**
     * Speed of acceleration forward of ship
     */
    public final static double ACCELERATION_SPEED = Math.PI/4;
    
    /**
     * Sets ship facing forward at the start of the game
     */
    public final static double INITIAL_DIRECTION = -Math.PI / 2;
    
    /**
     * Size of alien ships
     */
    public final static double ALIEN_SIZE = 10;
}
