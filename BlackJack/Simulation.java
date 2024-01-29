package BlackJack;

import java.util.ArrayList;

public class Simulation {
    private static void println(String text) {
        System.out.println(text);
    }
    private static void println(Integer number) {
        System.out.println(number);
    }

    public static void main(String[] args) throws Exception {
        Blackjack game = new Blackjack(26, false, 10, 1000);

        ArrayList<Integer> games = new ArrayList<Integer>();

        float numberOfTries = 1000.F;

        for (int i = 0; i <= numberOfTries; i++) {
            games.add(game.game(false));
        }

        float push = games.stream().filter((x) -> x == 0).count();
        float playerWins = games.stream().filter((x) -> x == 1).count();
        float dealerWins = games.stream().filter((x) -> x == -1).count();

        println(String.format("Number of pushes %.0f, ( %.1f%% )", push, push / numberOfTries * 100));
        println(String.format("Number of player wins %.0f, ( %.1f%% )", playerWins, playerWins / numberOfTries * 100));
        println(String.format("Number of dealer wins %.0f, ( %.1f%% )", dealerWins, dealerWins / numberOfTries * 100));
    }
}
