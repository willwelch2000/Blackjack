import java.util.Scanner;

public class PlayBlackjack {
    public static void main(String args[]) {
        //Setup
        final int decks = 8;
        final int playersNumber = Integer.parseInt(args[0]);
        Blackjack game = new Blackjack(playersNumber, decks);
        int currentPlayer = 0;

        boolean askBet = true;

        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < playersNumber; i++) {
            System.out.println("Player " + i + " money: ");
            Double money = scanner.nextDouble();
            game.setMoney(i, money);
            System.out.println("---------------");
        }
        System.out.println(game);
        
        System.out.println("Player " + currentPlayer + (askBet? " bet: " : " move: "));
        String next = scanner.next();

        while (!next.equals("stop")) {
            if(askBet)
                game.setBet(currentPlayer, Double.parseDouble(next));
            else {
                if (next.equals("stand"))
                    game.stand();
                else if (next.equals("hit"))
                    game.hit();
                else if (next.equals("double"))
                    game.doubleDown();
                else if (next.equals("split"))
                    game.split();
            }

            currentPlayer++;
            if (currentPlayer == playersNumber) {
                currentPlayer = 0;
                if (askBet) {
                    game.deal();
                    askBet = false;
                }
            }

            System.out.println(game);
            if (game.getGameOver()) {
                currentPlayer = 0;
                askBet = true;
                System.out.println("Continue?");
                if (scanner.next().equals("no"))
                    break;
            }
            System.out.println("Player " + currentPlayer + (askBet? " bet: " : " move: "));
            
            next = scanner.next();
        }
        scanner.close();
    }
}