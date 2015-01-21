package asteroids;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

import static asteroids.Constants.*;

/**
 * Controls a game of asteroids
 * 
 */
public class Controller implements CollisionListener, ActionListener,
        KeyListener, CountdownTimerListener
{
    // Shared random number generator
    private Random random;

    // The ship (if one is active) or null (otherwise)
    private Ship ship;

    // When this timer goes off, it is time to refresh the animation
    private Timer refreshTimer;

    // Count of how many transitions have been made. This is used to keep two
    // conflicting transitions from being made at almost the same time.
    private int transitionCount;

    // Number of lives left
    private int lives;
    
    // Your score in the current game
    private long score;
    private long scoreBonus;
    
    private int level;

    // The Game and Screen objects being controlled
    private Game game;
    private Screen screen;
    
    // List of asteroids collected together and the number of each size of asteroid
    private ArrayList<Asteroid> asteroidList = new ArrayList<Asteroid>();
    private int bigAsteroids;
    private int mediumAsteroids;
    private int smallAsteroids;
    
    // number of games played in this running of the program
    private int gameCount;
    
    // the initial bullet limit for the user
    private int bulletLimit = INITIAL_BULLET_LIMIT;
    
    // booleans as to whether certain keys are pressed
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    
    // whether you're currently starting a new level, replacing your ship,
    // or restarting the entire game
    private boolean startingNewLevel = false;
    private boolean replacingShip = false;
    private boolean restarting = false;
    
    // whether you're currently using a cheat code
    private boolean cheatShip = false;
    private boolean cheatAsteroids = false;
    
    // list and count of bullets and mines to make sure you don't
    // go over the bullet limit
    private int bulletCount = 0;
    private ArrayList<Bullet> bulletList = new ArrayList<Bullet>();
    private int mineCount = 0;
    private int mineLimit = MINE_LIMIT;
    
    // count to calculate your accuracy percentage
    private int hitCount;
    private int shotCount;
    private double accuracyPercentage;

    /**
     * Constructs a controller to coordinate the game and screen
     */
    public Controller (Game game, Screen screen)
    {
        // Record the game and screen objects
        this.game = game;
        this.screen = screen;
        
        // Start the game count for this session
        gameCount = 1;

        // Initialize the random number generator
        random = new Random();

        // Set up the refresh timer.
        refreshTimer = new Timer(FRAME_INTERVAL, this);
        transitionCount = 0;

        // Bring up the splash screen and start the refresh timer
        splashScreen();
        refreshTimer.start();
    }

    /**
     * Configures the game screen to display the splash screen
     */
    private void splashScreen ()
    {
        // Clear the screen and display the legend
        screen.clear();
        level = 1;
        screen.setLegend("Asteroids");

        // Place four asteroids near the corners of the screen.
        placeAsteroids();

        // Make sure there's no ship
        ship = null;
    }

    /**
     * Get the number of transitions that have occurred.
     */
    public int getTransitionCount ()
    {
        return transitionCount;
    }

    /**
     * The game is over. Displays a message to that effect and enables the start
     * button to permit playing another game.
     */
    private void finalScreen ()
    {
        screen.setLegend(GAME_OVER);
        screen.removeCollisionListener(this);
        screen.removeKeyListener(this);
    }

    /**
     * Places four asteroids near the corners of the screen. Gives them random
     * velocities and rotations.
     */
    private void placeAsteroids ()
    {
        Participant a = new Asteroid(0, 2, EDGE_OFFSET, EDGE_OFFSET);
        asteroidList.add((Asteroid) a);
        a.setVelocity(2 + level, random.nextDouble() * 2 * Math.PI);
        a.setRotation(2 * Math.PI * random.nextDouble());
        screen.addParticipant(a);

        a = new Asteroid(1, 2, SIZE - EDGE_OFFSET, EDGE_OFFSET);
        asteroidList.add((Asteroid) a);
        a.setVelocity(2 + level, random.nextDouble() * 2 * Math.PI);
        a.setRotation(2 * Math.PI * random.nextDouble());
        screen.addParticipant(a);

        a = new Asteroid(2, 2, EDGE_OFFSET, SIZE - EDGE_OFFSET);
        asteroidList.add((Asteroid) a);
        a.setVelocity(2 + level, random.nextDouble() * 2 * Math.PI);
        a.setRotation(2 * Math.PI * random.nextDouble());
        screen.addParticipant(a);

        a = new Asteroid(3, 2, SIZE - EDGE_OFFSET, SIZE - EDGE_OFFSET);
        asteroidList.add((Asteroid) a);
        a.setVelocity(2 + level, random.nextDouble() * 2 * Math.PI);
        a.setRotation(2 * Math.PI * random.nextDouble());
        screen.addParticipant(a);
        
        
    }

    /**
     * Set things up and begin a new game.
     */
    private void initialScreen ()
    {
        // Clear the screen
        screen.clear();
        
        //Clear cheat codes
        bulletLimit = INITIAL_BULLET_LIMIT;
        mineLimit = MINE_LIMIT;
        cheatShip = false;
        cheatAsteroids = false;
        
        // reset the asteroid count and update the proper labels
        asteroidList.clear();
        bigAsteroids = 4;
        mediumAsteroids = 0;
        smallAsteroids = 0;
        game.setBigAsteroids(bigAsteroids);
        game.setMediumAsteroids(mediumAsteroids);
        game.setSmallAsteroids(smallAsteroids);
        
        // reset the rest of the labels and data
        hitCount = 0;
        shotCount = 0;
        accuracyPercentage = 0;
        game.setShotCount(shotCount);
        game.setAccuracyPercentage(0);
        level = 1;
        
        // Place four asteroids
        placeAsteroids();
        // Place the ship
        placeShip();

        // Reset statistics
        game.setLevel(level);
        lives = 3;
        score = 0;
        scoreBonus = 0;
        mineCount = 0;
        game.setLives(lives);
        game.setScore(score);

        // Start listening to events. In case we're already listening, take
        // care to avoid listening twice.
        screen.removeCollisionListener(this);
        screen.removeKeyListener(this);
        screen.addCollisionListener(this);
        screen.addKeyListener(this);

        // Give focus to the game screen
        screen.requestFocusInWindow();
    }

    /**
     * Place a ship in the center of the screen.
     */
    private void placeShip ()
    {
        if (ship == null)
        {
            ship = new Ship();
        }
        if (!restarting || replacingShip)
        {
            new CountdownTimer(this, ship, 33);
        }
        restarting = false;
        replacingShip = false;
        ship.setVelocity(0, INITIAL_DIRECTION);
        ship.setPosition(SIZE / 2, SIZE / 2);
        ship.setRotation(INITIAL_DIRECTION);
        screen.addParticipant(ship);
    }

    /**
     * Deal with collisions between participants.
     */
    @Override
    public void collidedWith (Participant p1, Participant p2)
    {
        // check to see if civil war cheat code has been activated
        if (cheatAsteroids)
        {
            if (p1 instanceof Asteroid && p2 instanceof Asteroid)
            {
                asteroidCollision((Asteroid) p1);
                asteroidCollision((Asteroid) p2);
            }
        }
        if (p1 instanceof Asteroid && p2 instanceof Ship)
        {
            Asteroid a = (Asteroid) p1;
            if (a.getSize() > 0 || (a.getSize() == 0 && !cheatShip))
            {
                shipCollision((Ship) p2);
            }
            asteroidCollision((Asteroid) p1);
            
        }
        else if (p1 instanceof Ship && p2 instanceof Asteroid)
        {
            Asteroid a = (Asteroid) p2;
            if (a.getSize() > 0 || (a.getSize() == 0 && !cheatShip))
            {
                shipCollision((Ship) p1);
            }
            asteroidCollision((Asteroid) p2);
        }
        else if (p1 instanceof Bullet && p2 instanceof Asteroid)
        {
            asteroidCollision((Asteroid) p2);
            bulletCollision((Bullet) p1);
        }
        else if (p1 instanceof Asteroid && p2 instanceof Bullet)
        {
            asteroidCollision((Asteroid) p1);
            bulletCollision((Bullet) p2);
        }
        else if (p1 instanceof Mine && p2 instanceof Asteroid)
        {
            mineCollision((Mine) p1);
        }
        else if (p1 instanceof Asteroid && p2 instanceof Mine)
        {
            mineCollision((Mine) p2);
        }
    }
    
    /**
     * The mine has collided with something
     */
    private void mineCollision (Mine m)
    {
        if (ship != null)
        {
            for (int i = 0; i < bulletLimit; i++)
            {
                explodeMine(ship.getDirection() + (((Math.PI*2)/bulletLimit)* i),BULLET_SPEED/2,m);
            }
        }
        mineCount--;
        screen.removeParticipant(m);
        m = null;
    }

    /**
     * The bullet has collided with something
     * @param b
     */
    private void bulletCollision (Bullet b)
    {
        // record there's been another hit and update the accuracy
        hitCount++;
        game.setAccuracyPercentage(findAccuracy());
        
        // remove the bullet
        screen.removeParticipant(b);
        bulletList.remove(b);
        b = null;
        bulletCount--;
    }

    /**
     * calculates the accuracy of the the player's shooting
     * @return
     */
    private double findAccuracy ()
    {
        accuracyPercentage = ((double)hitCount/(double)shotCount) * 100;
        return accuracyPercentage;
    }

    /**
     * The ship has collided with something
     */
    private void shipCollision (Ship s)
    {
        // Remove the ship from the screen and null it out
        screen.removeParticipant(s);
        ship = null;

        // Display a legend and make it disappear in one second
        screen.setLegend("Ouch!");
        replacingShip = true;
        new CountdownTimer(this, null, 1000);

        // Decrement lives
        lives--;
        
        // Reset lives label
        game.setLives(lives);

        createShipDebrisPiece(s.getXNose(),s.getYNose(),10,-20);
        createShipDebrisPiece(s.getXNose(),s.getYNose(),-10,-20);
        createShipDebrisPiece(s.getX(),s.getY(),5,-10);
        createShipDebrisPiece(s.getX(),s.getY(),-5,-10);
        for (int i = 0; i < DEBRIS_AMOUNT; i++)
        {
            Debris d = new Debris(s.getX(),s.getY());
            d.setVelocity(DEBRIS_SPEED * Math.random(), d.getDirection());
            screen.addParticipant(d);
            new CountdownTimer (this, d, DEBRIS_DURATION);
        }
        

        // Start the timer that will cause the next round to begin.
        new TransitionTimer(END_DELAY, transitionCount, this);
    }

    /**
     * creates t
     * @param xNose
     * @param yNose
     * @param i
     * @param j
     */
    private void createShipDebrisPiece (double xNose, double yNose, int i, int j)
    {
        ShipDebris sd = new ShipDebris(xNose,yNose,i,j);
        sd.setVelocity(DEBRIS_SPEED*Math.random(),sd.getDirection());
        screen.addParticipant(sd);
        new CountdownTimer (this,sd,DEBRIS_DURATION);
        
    }

    /**
     * Something has hit an asteroid
     */
    private void asteroidCollision (Asteroid a)
    {
        // The asteroid disappears
        screen.removeParticipant(a);
        asteroidList.remove(a);
        
        // update score and asteroid count based on the size of the asteroid
        int size = a.getSize();
        if (size == 2)
        {
            bigAsteroids--;
            score += 20;
            scoreBonus += 20;
        }
        else if (size == 1)
        {
            mediumAsteroids--;
            score += 50;
            scoreBonus += 50;
        }
        else if (size == 0)
        {
            smallAsteroids--;
            score += 100;
            scoreBonus += 100;
        }
        game.setScore(score);
        
        // create debris based on size of asteroid
        for (int i = 0; i < (DEBRIS_AMOUNT * (size + 1)); i++)
        {
            Debris d = new Debris(a.getX(),a.getY());
            d.setVelocity(DEBRIS_SPEED * Math.random(), d.getDirection());
            screen.addParticipant(d);
            new CountdownTimer (this, d, DEBRIS_DURATION);
        }
        size = size - 1;
        
        // Create two smaller asteroids. Put them at the same position
        // as the one that was just destroyed and give them a random
        // direction.
        if (size >= 0)
        {
            int speed =  (int) (2/ASTEROID_SCALE[size]) + level;
            Asteroid a1 = new Asteroid(random.nextInt(4), size, a.getX(),
                    a.getY());
            Asteroid a2 = new Asteroid(random.nextInt(4), size, a.getX(),
                    a.getY());
            a1.setVelocity(speed, random.nextDouble() * 2 * Math.PI);
            a2.setVelocity(speed, random.nextDouble() * 2 * Math.PI);
            a1.setRotation(2 * Math.PI * random.nextDouble());
            a2.setRotation(2 * Math.PI * random.nextDouble());
            screen.addParticipant(a1);
            screen.addParticipant(a2);
            if (size == 1)
            {
                mediumAsteroids += 2;
            }
            else if (size == 0)
            {
                smallAsteroids += 2;
            }
            asteroidList.add(a1);
            asteroidList.add(a2);
            
        }
        
        // update the asteroid count labels
        game.setBigAsteroids(bigAsteroids);
        game.setMediumAsteroids(mediumAsteroids);
        game.setSmallAsteroids(smallAsteroids);
        
        // give them a new life if they scored high enough
        if (scoreBonus >= BONUS_LIFE)
        {
            lives++;
            game.setLives(lives);
            scoreBonus = 0;
        }
    }


    /**
     * This method will be invoked because of button presses and timer events.
     */
    @Override
    public void actionPerformed (ActionEvent e)
    {
        // The start button  or controls button has been pressed.
        if (e.getSource() instanceof JButton)
        {
            JButton button = (JButton) e.getSource();
            // it's the controls button that's been pressed so
            // bring up the controls screen
            if (button.getText().equals("Controls/Score"))
            {
                showControlBox();
                // Start listening to events. In case we're already listening, take
                // care to avoid listening twice.
                screen.removeCollisionListener(this);
                screen.removeKeyListener(this);
                screen.addCollisionListener(this);
                screen.addKeyListener(this);

                // Give focus to the game screen
                screen.requestFocusInWindow();
            }
            // Stop whatever we're doing
            // and bring up the initial screen
            else {
                transitionCount++;
                bulletCount = 0;
                bulletList = new ArrayList<Bullet>();
                if (gameCount > 1)
                {
                    restarting = true;
                }
                gameCount++;
                initialScreen();
            }
            
        }

        // Time to refresh the screen
        else if (e.getSource() == refreshTimer)
        {

            // Refresh screen
            screen.refresh();
        }
    }

    /**
     * Displays the controls of the game
     */
    private void showControlBox ()
    {
        JOptionPane.showMessageDialog(null, "In-Game Controls:\n"
                + "'SPACE' = Fire 1 bullet\n"
                + "'Z' = Fire " + bulletLimit + " bullets in a circular direction\n"
                + "'N' = Start new game\n"
                + "'T' = Teleport to random location on screen\n"
                + "'M' = Place a mine on that explodes with bullets when an asteroid hits it\n"
                + "UP,RIGHT,LEFT = Maneuver ship\n\n"
                + "Scoring:\n"
                + "Large Asteroid: 20 points\n"
                + "Medium Asteroid: 50 points\n"
                + "Small Asteroid: 100 points\n\n"
                + "BONUS:\n"
                + "*Score " + BONUS_LIFE +" points to receive an extra life!\n"
                + "*Shoot above the 'Sharpshooter Bonus' to \n"
                + "double your points at the end of each level!");
    }

    /**
     * Based on the state of the controller, transition to the next state.
     */
    public void performTransition ()
    {
        // Record that a transition was made. That way, any other pending
        // transitions will be ignored.
        transitionCount++;

        // If there are no lives left, the game is over. Show
        // the final screen.
        if (lives == 0)
        {
            finalScreen();
        }
        
        // Checks if there are no asteroids left because then
        // they must have beat a level!
        if (startingNewLevel && asteroidList.size() == 0 && lives != 0)
        {
            placeAsteroids();
            bigAsteroids = 4;
            game.setBigAsteroids(bigAsteroids);
            startingNewLevel = false;
        }

        // The ship must have been destroyed. Place a new one and
        // continue on the current level
        if (replacingShip && lives != 0)
        {
            placeShip();
        }
    }
    
    private void explodeMine(double direction, int speed, Mine m)
    {
        if (m != null)
        {
            Bullet b = new Bullet (m.getCenterX(),m.getCenterY());
            bulletCount++;
            shotCount++;
            game.setShotCount(shotCount);
            game.setAccuracyPercentage(findAccuracy());
            screen.addParticipant(b);
            b.setVelocity(speed, direction);
            new CountdownTimer (this,b,BULLET_DURATION);
        }
    }
    
    // Fires a bullet based on the direction and speed
    private void fireBullet (double direction, int speed)
    {
        // make sure that you're not shooting too many bullets at once
        if (bulletList.size() < bulletLimit)
        {
            // actually fires the bullet
            if (ship != null)
            {
                Bullet b = new Bullet (ship.getXNose(),ship.getYNose());
                bulletList.add(b);
                bulletCount++;
                shotCount++;
                game.setShotCount(shotCount);
                game.setAccuracyPercentage(findAccuracy());
                screen.addParticipant(b);
                b.setVelocity(speed, direction);
                new CountdownTimer (this,b,BULLET_DURATION);
            }
        }
        
    }


    /**
     * Deals with certain key presses
     */
    @Override
    public void keyPressed (KeyEvent e)
    {
        // rotate left
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            if (ship != null && !leftPressed)
            {
                leftPressed = true;
            }
        }
        // rotate right
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            if (ship != null && !rightPressed)
            {
                rightPressed = true;
            }
        } 
        // move forward
        else if (e.getKeyCode() == KeyEvent.VK_UP)
        {
            if (ship != null && !upPressed)
            {
                upPressed = true;
            }
        }
        // shoot a bullet
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            if (ship != null)
            {
                fireBullet(ship.getDirection(),BULLET_SPEED);
            }
        }
        // shotgun shot
        if (e.getKeyCode() == KeyEvent.VK_Z)
        {
            if (ship != null)
            {
                if (bulletList.size() == 0)
                {
                    for (int i = 0; i < bulletLimit; i++)
                    {
                        fireBullet(ship.getDirection() + (((Math.PI*2)/bulletLimit)* i),BULLET_SPEED/2);
                    }
                }  
            }
            
        }
        // teleport somewhere random
        if (e.getKeyCode() == KeyEvent.VK_T)
        {
            if (ship != null)
            {
                ship.setPosition(random.nextDouble()*SIZE, random.nextDouble()*SIZE);
            }
        }
        // Restart the game if the 'N' key is pressed
        else if (e.getKeyCode() == KeyEvent.VK_N)
        {
            transitionCount++;
            bulletCount = 0;
            bulletList = new ArrayList<Bullet>();
            if (gameCount > 1)
            {
                restarting = true;
            }
            gameCount++;
            initialScreen();
        }
        // bring up the cheat code entering box
        // depending on what they enter activate one of these cheat codes
        else if (e.getKeyCode() == KeyEvent.VK_C)
        {
            String code = JOptionPane.showInputDialog(null, "Enter Cheat Code:");
            
            // gives you extra lives
            if (code != null)
            {
                if (code.equalsIgnoreCase("Wolverine"))
                {
                    JOptionPane.showMessageDialog(null, "Cheat code activated. Lives increased by " + CHEAT_LIVES);
                    lives += CHEAT_LIVES;
                    game.setLives(lives);
                }
                // allows you to grow the bullet limit
                else if (code.equalsIgnoreCase("Legolas"))
                {
                    JOptionPane.showMessageDialog(null, "Cheat code activated. " + CHEAT_BULLET + " bullets now available.");
                    bulletLimit = CHEAT_BULLET;
                }
                // makes you invulnerable to small asteroids
                else if (code.equalsIgnoreCase("Superman"))
                {
                    JOptionPane.showMessageDialog(null, "Cheat code activated. Now invulnerable to small asteroids.");
                    cheatShip = true;
                }
                // makes asteroids die if they run into each other
                else if (code.equalsIgnoreCase("Civil War"))
                {
                    JOptionPane.showMessageDialog(null, "Cheat code activated. Asteroids now collide with other asteroids");
                    cheatAsteroids = true;
                }
                else if (code.equalsIgnoreCase("Doctor Octopus"))
                {
                    JOptionPane.showMessageDialog(null, "Cheat code activated. " + CHEAT_MINE_LIMIT + " mines now available");
                    mineLimit = CHEAT_MINE_LIMIT;
                }
            }
        }
        
        // Place a mine that explodes into bullets if hit by an asteroid
        else if (e.getKeyCode() == KeyEvent.VK_M)
        {
            if (mineCount < mineLimit && ship != null)
            {
                mineCount++;
                Mine m = new Mine (ship.getX(),ship.getY());
                screen.addParticipant(m);
            }
        }

    }

    /**
     * Deals with certain key released events
     */
    @Override
    public void keyReleased (KeyEvent e)
    {
        // signify they're no longer pressing down the left key
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            leftPressed = false;
        }
        // signify they're no longer pressing down the left key
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            rightPressed = false;
        }
        // signify they're no longer pressing down the left key
        else if (e.getKeyCode() == KeyEvent.VK_UP)
        {
            upPressed = false;
        }
    }

    @Override
    public void keyTyped (KeyEvent e)
    {
    }

    /**
     * Callback for countdown timer. Used to create transient effects.
     */
    @Override
    public void timeExpired (Participant p)
    {
        // resets the legend words to nothing
        if (p == null) {
            screen.setLegend("");
        }
        if (p instanceof Ship)
        {
            if (ship != null && lives > 0 && p != null)
            {
                if (p.equals(ship))
                {
                    // rotates you left if you're pressing left
                    if (leftPressed && !rightPressed && !upPressed)
                    {
                        ship.rotate(-ROTATION_SPEED);
                    } 
                    // rotates right if you're pressing right
                    else if (rightPressed && !leftPressed && !upPressed)
                    {
                        ship.rotate(ROTATION_SPEED);
                    } 
                    // moves you forward
                    else if (upPressed && !leftPressed && !rightPressed)
                    {
                        ship.accelerate(ACCELERATION_SPEED);
                    } 
                    // simultaneously move you forward and rotate right
                    else if (upPressed && leftPressed && !rightPressed)
                    {
                        ship.accelerate(ACCELERATION_SPEED);
                        ship.rotate(-ROTATION_SPEED);
                    } 
                    // simultaneously move you forward and rotate left
                    else if (upPressed && !leftPressed && rightPressed)
                    {
                        ship.accelerate(ACCELERATION_SPEED);
                        ship.rotate(ROTATION_SPEED);
                    } 
                    // only move you forward if you're pressing all three
                    else if (upPressed && rightPressed && leftPressed)
                    {
                        ship.accelerate(ACCELERATION_SPEED);
                    } 
                    // don't move if you're pressing both left and right
                    else if (rightPressed && leftPressed) 
                    {
                        ship.rotate(0);
                    }
                }
                new CountdownTimer(this, ship, 33);
            }
        }
        // removes bullet after a certain time if it hasn't hit anything
        if (p instanceof Bullet)
        {
            screen.removeParticipant(p);
            bulletList.remove(p);
            bulletCount--;
        }
        // activates if you're starting a new level
        if (asteroidList.size() == 0 && !startingNewLevel && lives != 0)
        {
            // start a new level
            startingNewLevel = true;
            level++;
            game.setLevel(level);
            
            // doubles your score if you shoot above a certain percentage
            if (accuracyPercentage >= SHARP_SHOOTER)
            {
                JOptionPane.showMessageDialog(null, "Sharpshooter Bonus!\n SCORE DOUBLED! Lives increased by 1");
                screen.setLegend("Level: " + level);
                score = score * 2;
                game.setScore(score);
                lives++;
                game.setLives(lives);
            }
            else
            {
                screen.setLegend("Level: " + level);
            }
            
            // sends it into transition with a pause between levels
            new TransitionTimer(END_DELAY, transitionCount, this);
            new CountdownTimer(this, null, 2000);
        }
        // removes debris once time expires
        if (p instanceof Debris)
        {
            screen.removeParticipant(p);
        }
        // removes ship debris once time expires
        if (p instanceof ShipDebris)
        {
            screen.removeParticipant(p);
        }
    }
}