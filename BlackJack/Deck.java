package BlackJack;

import java.util.ArrayList;
import java.util.Random;

public class Deck {

    private ArrayList<Card> deck;
    private int count; 

    /**
     * Creates the underlying deck as an ArrayList of Card objects. 
     * Calls build() as a subroutine to build the deck itself.
     */
    public Deck() throws Exception {
        build();
    }

    /**
     * Builds the underlying deck as a standard 52 card deck. 
     * Replaces any current deck stored. 
     */
    public void build() throws Exception{
        deck = new ArrayList();
        for(Integer i: Card.Numbers){
            for(String j: Card.Colors){
                deck.add(new Card(j, i));
            }
        }
        shuffle();
    }

    /**
     * Returns the number of cards left in the deck. 
     * @return the number of cards left in the deck
     */
    public int size() {
        return deck.size();
    }

    /**
     * Returns and removes the first card of the deck.
     * @return the first card of the deck
     */
    public Card deal() {
        Card card = deck.get(0);
        deck.remove(0);
        if(card.getValue() < 7) count +=1;
        else if(card.getValue() < 10) count +=0; 
        else count -=1;
        return card;
    }

    /**
     * Shuffles the cards currently in the deck. 
     */
    public void shuffle() {
        Random random = new Random();
        int size = size();
        for (int i = size - 1; i > 0; --i){
            int end = random.nextInt(i);
            Card temp = deck.get(i);
            deck.set(i, deck.get(end));
            deck.set(end, temp);
        }
        count = 0;
    }

    /**
     * Returns a string representation of the deck.
     * @return a string representation of the deck
     */
    public String toString() {
        return deck.toString();
    }
}
