package BlackJack;

// 2 players are available
// ace is 11 points in this case
// P = player and H = house
// P & H receive 2 cards each
// P should either take or hit
// V = value
// V > 21 => lose V == 21 => win; biggest V wins
// H hits until V >= 17

public class Blackjack {

    interface Result {
        void print();

        int getValue();
    }

    private static class PlayerWin implements Result {
        public void print() {
            System.out.println("PLAYER WON");
        }

        public int getValue() {
            return 1;
        }
    }

    private static class DealerWin implements Result {
        public void print() {
            System.out.println("DEALER WON");
        }

        public int getValue() {
            return -1;
        }
    }

    private static class Push implements Result {
        public void print() {
            System.out.println("PUSH");
        }

        public int getValue() {
            return 0;
        }
    }

    private static int defaultReshuffleCutoff = 26;


    private Deck deck = new Deck();
    private Hand playerHand;
    private Hand dealerHand;
    private boolean _useBasicStrategy = false;
    private int _reshuffleCutoff;
    private float _bankroll;
    private float _betValue;
    private float _activeBet;

    private final int playerStopAt = 16;
    private final int dealerStopAt = 17;

    public Blackjack() throws Exception {
        setReshuffleCutoff(defaultReshuffleCutoff);
        reset();
    }

    public Blackjack(int reshuffleCutoff) throws Exception {
        this(reshuffleCutoff, false);
    }

    public Blackjack(int reshuffleCutoff, float betValue, float bankroll) throws Exception {
        this(reshuffleCutoff, true, betValue, bankroll);
    }

    public Blackjack(int reshuffleCutoff, boolean useBasicStrategy) throws Exception {
        this(reshuffleCutoff, useBasicStrategy, 0, 0);
    }

    public Blackjack(int reshuffleCutoff, boolean useBasicStrategy, float betValue, float bankroll) throws Exception {
        setReshuffleCutoff(reshuffleCutoff);
        setUseBasicStrategy(useBasicStrategy);
        setBetValue(betValue);
        setBankroll(bankroll);
        reset();
    }

    private boolean isNotBust(int value) {
        return value <= 21;
    }

    public void reset() throws Exception {
        if (deck.size() <= getReshuffleCutoff()) deck = new Deck();
        playerHand = new Hand();
        dealerHand = new Hand();
        setActiveBet(_betValue);
    }

    public void deal() {
        playerHand.add(deck.deal());
        dealerHand.add(deck.deal());
        playerHand.add(deck.deal());
        dealerHand.add(deck.deal());
    }

    public int getCount(){
        return deck.getCount();
    }

    public boolean playerTurn() {
        while (playerHand.getTotalValue() < playerStopAt) playerHand.add(deck.deal());
        return isNotBust(playerHand.getTotalValue());
    }

    public boolean dealerTurn() {
        while (dealerHand.getTotalValue() < dealerStopAt) dealerHand.add(deck.deal());
        return isNotBust(dealerHand.getTotalValue());
    }

    private BasicStrategy.Action getPlayableAction(Hand somePlayerHand, Card someDealerCard) {
        if (somePlayerHand.getTotalValue() < 21) {
            if ((somePlayerHand.getCard(0).getValue() == somePlayerHand.getCard(1).getValue()) && (somePlayerHand.size() == 2)) {
                Card pair = somePlayerHand.getCard(0);
                return BasicStrategy.Pairs[someDealerCard.getValue() - 2][pair.getValue() - 2];
            } else if (somePlayerHand.containsAce() && (somePlayerHand.size() == 2)) {
                return BasicStrategy.SoftTotals[somePlayerHand.getTotalValue() - 11 - 2][someDealerCard.getValue() - 2];
            } else {
                return BasicStrategy.HardTotals[somePlayerHand.getTotalValue() - 5][someDealerCard.getValue() - 2];
            }
        } else {
            return BasicStrategy.S;
        }
    }

    private Result evaluateResult() {
        if (!isNotBust(playerHand.getTotalValue())) return new DealerWin();
        if (!isNotBust(dealerHand.getTotalValue())) return new PlayerWin();
        int playerValue = playerHand.getTotalValue();
        int dealerValue = dealerHand.getTotalValue();
        if (playerValue > dealerValue) return new PlayerWin();
        else if (dealerValue > playerValue) return new DealerWin();
        else return new Push();
    }

    private Result playBasicStrategy(Hand somePlayerHand, Card someDealerCard) {
        BasicStrategy.Action action = getPlayableAction(somePlayerHand, someDealerCard);
        if (action == BasicStrategy.H) {
            playerHand.add(deck.deal());
            playBasicStrategy(playerHand, someDealerCard);
        } else if (action == BasicStrategy.D) {
            playerHand.add(deck.deal());
            playBasicStrategy(playerHand, someDealerCard);
            setActiveBet(2 * _activeBet);
        } else if (action == BasicStrategy.S) {
            ;
        } else if (action == BasicStrategy.U) {
            setActiveBet(_activeBet / 2);
            return new DealerWin();
        } else if (action == BasicStrategy.P) {
            ;
        }
        dealerTurn();
        return evaluateResult();

    }

    public void setReshuffleCutoff(int cutoff) {
        _reshuffleCutoff = cutoff;
    }

    public int getReshuffleCutoff() {
        return _reshuffleCutoff;
    }

    public void setUseBasicStrategy(boolean useBasicStrategy) {
        _useBasicStrategy = useBasicStrategy;
    }

    public void setBetValue(float betValue) {
        _betValue = betValue;
    }

    public void setActiveBet(float activeBet) {
        _activeBet = activeBet;
    }

    public void setBankroll(float bankroll) {
        _bankroll = bankroll;
    }

    public float getBankroll() {
        return _bankroll;
    }

    private void winBet() {
        setBankroll(_bankroll + _activeBet);
    }

    private void loseBet() {
        setBankroll(_bankroll - _activeBet);
    }


    public String toString() {
        return String.format("Player Hand -> %s \nDealer Hand -> %s", playerHand.toString(), dealerHand.toString());
    }

    private Result playGame() throws Exception {
        if (_useBasicStrategy) {
            return playBasicStrategy(playerHand, dealerHand.getCard(1));
        } else {
            playerTurn();
            dealerTurn();
            return evaluateResult();
        }
    }

    public int game(boolean verbose) throws Exception {
        Result result = game();
        if (verbose) {
            result.print();
        }
        return result.getValue();


    }

    private Result game() throws Exception {
        reset();
        deal(); // dealer's first card is hidden, second is visible

        Result result = playGame();

        if (_betValue > 0) {
            if (result instanceof DealerWin) {
                loseBet();
            } else if (result instanceof PlayerWin) {
                winBet();
            }
        }

        return result;
    }
}