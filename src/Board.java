import java.awt.event.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.*;

public class Board
{
    // Array to hold board cards
    public FlippableCard cards[];
    // Resource loader
    private ClassLoader loader = getClass().getClassLoader();

    public Board(int size, ActionListener AL)
    {
        // Allocate and configure the game board: an array of cards
        // Leave one extra space for the "happy face", added at the end
        cards = new FlippableCard[size];

        // Fill the Cards array
        int imageIdx = 1;
        for (int i = 0; i < (size-1); i += 2) {

            // Load the front image from the resources folder
            String imgPath = "res/hub" + imageIdx + ".jpg";
            ImageIcon img = new ImageIcon(loader.getResource(imgPath));

            // Setup two cards at a time
            FlippableCard c1 = new FlippableCard(img);
            c1.setID(imageIdx);
            c1.setCustomName(imgPath);
            FlippableCard c2 = new FlippableCard(img);
            c2.setID(imageIdx);
            c2.setCustomName(imgPath);

            imageIdx++;  // get ready for the next pair of cards

            // Add them to the array
            cards[i] = c1;
            cards[i + 1] = c2;
        }
        // Add the "happy face" image
        String imgPath = "res/happy-face.jpg";
        ImageIcon img = new ImageIcon(loader.getResource(imgPath));
        FlippableCard c1 = new FlippableCard(img);
        cards[size-1] = c1;
        c1.setID(100);
        c1.setCustomName(imgPath);

        /*
         * Randomize the card positions
         */
        List<FlippableCard> shuffler = Arrays.asList(cards);
        Collections.shuffle(shuffler);
        shuffler.toArray(cards);

    }

    public void fillBoardView(JPanel view)
    {
        //fill the panel with cards
        for (FlippableCard c : cards) {
            c.hideFront();

            view.add(c);

        }

    }

    public void resetBoard()
    {
        /*
         * Reset the flipped cards and randomize the card positions
         */

        new Board(25, new MemoryGame());
    }



}
