package BlackJack;

import java.util.Arrays;

public class Card {

        /**
         * The value of the card.
         */
        private int value;
        private String _color = null;

        public static String[] Colors = {"Hearts" , "Spades" , "Diamonds" , "Clubs"};
        public static Integer[] Numbers = {2,3,4,5,6,7,8,9,10,10,10,10,11};
    
        /**
         * Constructs a card with the specified value.
         * @param val
         */
        public Card(int val) throws Exception{
            if (val > 1 && val < 12){
                this.value = val;
            } else {
                throw new Exception("Card values are not valid");
            }
        }
        public Card(String color, int number) throws Exception {
            if (Arrays.asList(Colors).contains(color) && Arrays.asList(Numbers).contains(number)) {
                this._color = color;
                this.value = number;
            } else {
                throw new Exception("Card values are not valid");
            }
        }
        /**
         * Returns the value of the card.
         * @return the value of the card
         */
        public int getValue() {
            return value;
        }
        
        /**
         * Returns a string representation of this card.
         * @return a string representation of this card
         */
        public String toString() {
            if(_color != null){
                return "" + value + " of " + _color;
            } else {
                return String.valueOf(value);
            }
        }
}
