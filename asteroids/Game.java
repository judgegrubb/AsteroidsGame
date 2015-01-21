package asteroids;

import javax.swing.*;

import java.awt.*;
import java.text.DecimalFormat;

import static asteroids.Constants.*;

/**
 * Implements an asteroid game.
 *
 */
public class Game extends JFrame
{
    // label for number of lives left
    private JLabel livesLeft;
    
    // label for your score in the game
    private JLabel score;
    
    // label for what level you're on
    private JLabel level;
    
    // labels for the number of each size of asteroid
    private JLabel bigAsteroids;
    private JLabel mediumAsteroids;
    private JLabel smallAsteroids;
    
    // label for your number of shots
    private JLabel shotCount;
    
    // label for your number of hits
    private JLabel hitCount;
    
    // label for your accuracy of shots to hits
    private JLabel accuracy;
    
    /**
     * Launches the game
     */
    public static void main (String[] args)
    {
        Game a = new Game();
        a.setVisible(true);
    }

    /**
     * Lays out the game and creates the controller
     */
    public Game ()
    {
        // Title at the top
        setTitle(TITLE);

        // Default behavior on closing
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // The main playing area and the controller
        Screen screen = new Screen();
        Controller controller = new Controller(this, screen);

        // This panel contains the screen to prevent the screen from being
        // resized
        JPanel screenPanel = new JPanel();
        screenPanel.setLayout(new GridBagLayout());
        screenPanel.add(screen);

        // This panel contains buttons and labels
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(1,5));
        
        // The button that starts the game
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2,1));
        JButton startGame = new JButton(START_LABEL);
        buttonPanel.add(startGame);
        
        // The button that displays the controls and how-to
        JButton seeControls = new JButton("Controls/Score");
        seeControls.addActionListener(controller);
        buttonPanel.add(seeControls);
        controlPanel.add(buttonPanel);
        
        // Panel containing all of the stats
        JPanel statPanel = new JPanel();
        statPanel.setLayout(new GridLayout(3,1));
        
        // The label for the number of lives left
        livesLeft = new JLabel("Lives: 3");
        livesLeft.setForeground(Color.BLUE);
        statPanel.add(livesLeft);
        
        // The label for the score
        score = new JLabel ("Score: 0");
        score.setForeground(Color.BLUE);
        statPanel.add(score);
        
        // The label for the level
        level = new JLabel ("Level: 1");
        level.setForeground(Color.BLUE);
        statPanel.add(level);
        controlPanel.add(statPanel);
        
        // Organize everything
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(screenPanel, "Center");
        
        setContentPane(mainPanel);
        
        // Labels for displaying number of asteroids left
        JPanel asteroidPanel = new JPanel();
        asteroidPanel.setLayout(new GridLayout(3,1));
        bigAsteroids = new JLabel("Big Asteroids: 4");
        mediumAsteroids = new JLabel("Medium Asteroids: 0");
        smallAsteroids = new JLabel("Small Asteroids: 0");
        asteroidPanel.add(bigAsteroids);
        asteroidPanel.add(mediumAsteroids);
        asteroidPanel.add(smallAsteroids);
        controlPanel.add(asteroidPanel);
        
        // Labels for displaying shots fired and accuracy percentage
        JPanel shotPanel = new JPanel();
        shotPanel.setLayout(new GridLayout(3,1));
        shotCount = new JLabel("Shots fired: 0");
        JLabel sharpShooter = new JLabel("Sharpshooter Bonus: " + SHARP_SHOOTER + "%");
        sharpShooter.setForeground(Color.RED);
        accuracy = new JLabel("Accuracy: 0%");
        shotPanel.add(shotCount);
        shotPanel.add(accuracy);
        shotPanel.add(sharpShooter);
        
        // Finish setting up the main panel
        controlPanel.add(shotPanel);
        controlPanel.setBackground(Color.GRAY);
        mainPanel.add(controlPanel, "North");
        pack();

        // Connect the controller to the start button
        startGame.addActionListener(controller);
    }
    
    /**
     * displays the number of lives left for the user to see
     * @param i
     */
    public void setLives(int i) {
        livesLeft.setText("Lives: " + i);
    }
    
    /**
     * displays the current score for the user
     * @param i
     */
    public void setScore(long i) {
        score.setText("Score: " + i);
    }
    
    /**
     * displays the level the user is currently on
     * @param i
     */
    public void setLevel(int i) {
        level.setText("Level: " + i);
    }
    
    /**
     * displays how many big asteroids are currently on the screen
     * @param i
     */
    public void setBigAsteroids(int i) {
        bigAsteroids.setText("Big Asteroids: " + i);
    }
    
    /**
     * displays how many medium asteroids are currently on the screen
     * @param i
     */
    public void setMediumAsteroids(int i){
        mediumAsteroids.setText("Medium Asteroids: " + i);
    }
    
    /**
     * displays how many small asteroids are currently on the screen
     * @param i
     */
    public void setSmallAsteroids (int i) {
        smallAsteroids.setText("Small Asteroids: " + i);
    }

    /**
     * displays the number of shots fired
     * @param i
     */
    public void setShotCount (int i)
    {
        shotCount.setText("Shots fired: " + i);
    }
    
    /**
     * displays your accuracy
     * @param i
     */
    public void setAccuracyPercentage (double i)
    {
        
        accuracy.setText("Accuracy: " + String.format("%.2f", i) + "%");
    }
}
