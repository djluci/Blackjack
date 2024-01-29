package BlackJack;

import java.util.ArrayList;
import java.util.Arrays;

public class Hand {

    private ArrayList<Card> cards;

    /**
     * Creates an empty hand as an ArrayList of Cards.  
     */
    public Hand() {
        cards = new ArrayList();
    }

    /**
     * Removes any cards currently in the hand. 
     */
    public void reset() {
        cards = new ArrayList();
    }

    /**
     * Adds the specified card to the hand.
     * @param card the card to be added to the hand
     */
    public void add(Card card) {
        cards.add(card);
    }

    /**
     * Returns the number of cards in the hand.
     * @return the number of cards in the hand
     */
    public int size() {
        return cards.size();
    }

    /**
     * Returns the card in the hand specified by the given index. 
     * @param index the index of the card in the hand.
     * @return the card in the hand at the specified index.
     */
    public Card getCard(int index){
        return cards.get(index);
    }

    /**
     * Returns the summed value over all cards in the hand.
     * @return the summed value over all cards in the hand
     */
    public int getTotalValue() {
        return cards.stream()
                .map((Card card) -> card.getValue())
                .reduce(0, Integer::sum);
    }    

    /**
     * Returns a string representation of the hand.
     * @return a string representation of the hand
     */
    public String toString() {
        String cardsArrayString = cards.stream()
                .map((Card card) -> card.toString())
                .reduce("", (buffer, card) -> buffer == "" ? card : String.format("%s, %s", buffer, card));
        return String.format("[%s]: %s", cardsArrayString, getTotalValue());
    }
}
