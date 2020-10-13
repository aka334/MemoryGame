/*
       Author: Aayush Karki
       Code: Memory Game
       Description: Memory game test your memory
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class MemoryGame extends JFrame implements ActionListener
{
    // Core game play objects
    private Board gameBoard;
    //storing two cards for comparison
    private FlippableCard prevCard1, prevCard2;
    //count clicks from 0 to 2 and resets
    private int count;
    // Labels to display game info
    private JLabel errorLabel, GuessesMade, ElapsedTime;
    //checks two cards if match
    boolean match = true;
    //boolean to record first click
    boolean firstClick = true;
    // layout objects: Views of the board and the label area
    private JPanel boardView, labelView;
    //Pop up
    JFrame popup = new JFrame();
    // Record keeping counts and times
    int clickCount = 0, gameTime = 0, errorCount = 0;
    int pairsFound = 0;

    // Game timer: will be configured to trigger an event every second
    private Timer gameTimer;
    //Constructor of the code
    public MemoryGame()
    {
        // Call the base class constructor
        super("Hubble Memory Game");
        count = 0;

        // Allocate the interface elements
        // Creating Buttons
        JButton restart = new JButton("Restart");
        JButton quit = new JButton("Quit");
        GuessesMade = new JLabel("Guesses: "+ errorCount);
        errorLabel = new JLabel("Matches: "+ pairsFound);
        ElapsedTime = new JLabel("Time: " + gameTime);
        /*
         * Setup the interface objects (timer, error counter, buttons)
         * and any event handling they need to perform
         */
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ElapsedTime.setText("Time: " + gameTime++);
            }
        });

        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Allocate two major panels to hold interface
        labelView = new JPanel();  // used to hold labels
        boardView = new JPanel();  // used to hold game board


        // get the content pane, onto which everything is eventually added
        Container c = getContentPane();

        // Setup the game board with cards
        gameBoard = new Board(25, this);
        // Add the game board to the board layout area
        boardView.setLayout(new GridLayout(5, 5, 2, 0));
        //fill the gameboard with boardview
        gameBoard.fillBoardView(boardView);
        FlippableCard buttons[] = gameBoard.cards;
        //listen to each card on the screen
        for(FlippableCard button: buttons){
            button.addActionListener(this);
        }

        // Add required interface elements to the "label" JPanel
        labelView.setLayout(new GridLayout(1, 4, 2, 2));
        labelView.add(quit);
        labelView.add(restart);
        labelView.add(GuessesMade);
        labelView.add(errorLabel);
        labelView.add(ElapsedTime);
        // Both panels should now be individually layed out
        // Add both panels to the container
        c.add(labelView, BorderLayout.NORTH);
        c.add(boardView, BorderLayout.SOUTH);

        setSize(745, 500);
        setVisible(true);
    }

    /* Handle anything that gets clicked and that uses MemoryGame as an
     * ActionListener */
    public void actionPerformed(ActionEvent e)
    {
        //if first click start the timer
        if(firstClick){
            gameTimer.start();
            ElapsedTime.setText("Time: " + gameTime++);
            firstClick = false;
        }
        // Get the currently clicked card from a click event
        FlippableCard currCard = (FlippableCard)e.getSource();
        /*
         * What happens when a card gets clicked?
         */
        //clicks counts increases
        count++;
        if(count == 1){
            //First click
            if(!match){
                //Resetting two buttons
                //if two cards doesn't match
                prevCard1.clicked = false;
                prevCard2.clicked = false;
                //hiding the card
                prevCard2.hideFront();
                prevCard1.hideFront();
                match = true;
            }
            //Setting it to present
            prevCard1 = currCard;
            //If the card is not clicked
            if(!prevCard1.clicked){
                //Setting the card to click
                prevCard1.clicked = true;
                //show front of the card
                prevCard1.showFront();
                //if its a smile face
                if(prevCard1.id() == 100){
                    //set the count to 0 and add pair found
                    count = 0;
                    pairsFound += 1;
                }
            }else {
                //if its first click and the card is clicked again set it to 0
                count = 0;
            }
        }else {
            //Second Click
            prevCard2 = currCard;
            //If the card is not clicked
            if(!prevCard2.clicked) {
                //Setting the card to clicked and show the hidden picture
                prevCard2.clicked = true;
                prevCard2.showFront();
                //Setting the count to 1st click
                count = 0;
                //if two card matches
                if (prevCard2.id() == prevCard1.id()){
                    pairsFound += 1;
                    match = true;
                } else {
                    //if cards doesn't match increase error count
                    errorCount += 1;
                    match = false;
                }

            }else{
                //if the card is already clicked set it to second click again
                count = 1;
            }

        }
        //Update the scoreboard
        GuessesMade.setText("Guesses: "+ errorCount);
        errorLabel.setText("Matches: "+ pairsFound);
        //if all pairs are found then display score
        if(pairsFound == 13){
                new GameOver(this);
        }

    }
    //Restarts the game setting everything to initial stage
    public void restartGame()
    {
        pairsFound = 0;
        gameTime = 0;
        clickCount = 0;
        errorCount = 0;
        GuessesMade.setText("Timer: 0");
        errorLabel.setText("Errors: 0");

        // Clear the boardView and have the gameBoard generate a new layout
        boardView.removeAll();
        dispose();
        gameBoard.resetBoard();
        gameBoard.fillBoardView(boardView);
    }

    //Main block of the code
    public static void main(String args[])
    {
        MemoryGame M = new MemoryGame();
        M.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });
    }
}
//GameOver creates a new frame and shows the stats of the game
class GameOver{
    public GameOver(MemoryGame game){
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.add(panel);
        //Title of the GUI
        frame.setTitle("Register!");

        panel.setBackground(Color.darkGray);
        panel.setLayout(null);
        //General layout
        JLabel text = new JLabel("GAME STATS!");
        text.setBounds(10, 30, 300, 25);
        text.setForeground(Color.CYAN);
        panel.add(text);
        JLabel GuessesMade = new JLabel("Guesses: "+ game.errorCount);
        GuessesMade.setBounds(10, 80, 80, 25);
        GuessesMade.setForeground(Color.CYAN);
        JLabel errorLabel = new JLabel("Matches: "+ game.pairsFound);
        errorLabel.setBounds(10, 120, 165, 25);
        errorLabel.setForeground(Color.cyan);
        JLabel ElapsedTime = new JLabel("Time: " + game.gameTime);
        ElapsedTime.setBounds(10, 150, 80, 25);
        ElapsedTime.setForeground(Color.cyan);
        panel.add(GuessesMade);
        panel.add(errorLabel);
        panel.add(ElapsedTime);

        JLabel text1 = new JLabel("Please close this window and restart to play again otherwise Exit!");
        text1.setBounds(10, 180, 400, 25);
        text1.setForeground(Color.cyan);
        panel.add(text1);

        JButton button = new JButton("EXIT");

        button.setBounds(150, 250, 120, 25);
        panel.add(button);
        //when button pressed
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //NEED To implement and store in database!
                System.exit(0);

            }
        });


        frame.add(panel);
        frame.setVisible(true);
    }
}
