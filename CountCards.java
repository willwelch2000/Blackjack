public class CountCards {
    public static void main(String[] args) {
        //Setup
        final boolean print = false;
        final int decks = 8;
        final double startMoney = 20000;
        final double minimum = .5;
        final int roundsNumber = Integer.parseInt(args[0]);
        Blackjack game = new Blackjack(1, decks);
        game.setMoney(0, startMoney);

        //For loop for input number
        for (int i = 0; i < roundsNumber; i++) {
            game.setBet(0, bet(game.getMoney(0), game.getTrueCount(), minimum));
            game.deal();
            if (print)
                System.out.println(game);
            //Play while game isn't over
            while (!game.getGameOver()) {
                int decision = move(game.getHand(game.getTurn()), game.getDealerUpCard(), game.getTrueCount());
                if (print)
                    System.out.println("Move: " + decision + "\n\n");
                if (decision == 0)
                    game.stand();
                else if (decision == 1)
                    game.hit();
                else if (decision == 2)
                    game.doubleDown();
                else if (decision == 3)
                    game.split();
                if (print)
                    System.out.println(game);
            }
        }

        System.out.println("Money remaining: " + game.getMoney(0));
        System.out.println("Percentage: " + 100 * (game.getMoney(0) / startMoney));
        System.out.println("Money gained per round: " + ((game.getMoney(0) - startMoney) / roundsNumber));
    }

    public static int move(char[] hand, char dealerUpCard, double trueCount) {
        final int stand = 0;
        final int hit = 1;
        final int doubleDown = 2;
        final int split = 3;
        int sum = Blackjack.sum(hand);
        boolean soft = Blackjack.soft(hand);
        boolean firstPlay = Blackjack.firstPlay(hand);
        int dealerUpCardI = (int) dealerUpCard;

        //If we should split
        if (firstPlay && hand[0] == hand[1]) {
            if (hand[0] == 'A' || hand[0] == '8')
                return split;
            if (hand[0] == '9' && !(dealerUpCard == '7' || dealerUpCard == 'T' || dealerUpCard == 'J' || dealerUpCard == 'Q' || dealerUpCard == 'K' || dealerUpCard == 'A'))
                return split;
            if (hand[0] == '7' && (dealerUpCardI < 56))
                return split;
            if (hand[0] == '6' && (dealerUpCardI < 55))
                return split;
            if (hand[0] == '4' && (dealerUpCard == '5' || dealerUpCard == '6'))
                return split;
            if ((hand[0] == '2' || hand[0] == '3') && (dealerUpCardI < 56))
                return split;
        }
        
        //Soft totals
        if (soft) {
            if (sum > 19)
                return stand;
            if (sum == 19) {
                if (dealerUpCard == '6' && firstPlay)
                    return doubleDown;
                return stand;
            }
            if (sum == 18) {
                if (dealerUpCardI < 55 && firstPlay)
                    return doubleDown;
                if (dealerUpCardI < 57)
                    return stand;
                return hit;
            }
            if (sum == 17) {
                if (dealerUpCardI > 50 && dealerUpCardI < 55 && firstPlay)
                    return doubleDown;
                return hit;
            }
            if (sum == 15 || sum == 16) {
                if (dealerUpCardI > 51 && dealerUpCardI < 55 && firstPlay)
                    return doubleDown;
                return hit;
            }
            if (dealerUpCardI > 52 && dealerUpCardI < 55 && firstPlay)
                return doubleDown;
            return hit;
        } else {
            //Hard totals
            if (sum > 16)
                return stand;
            if (sum > 12 && sum < 17) {
                if (dealerUpCardI < 55)
                    return stand;
                return hit;
            }
            if (sum == 12) {
                if (dealerUpCardI > 51 && dealerUpCardI < 55)
                    return stand;
                return hit;
            }
            if (sum == 11) {
                if (firstPlay)
                    return doubleDown;
                return hit;
            }
            if (sum == 10) {
                if (dealerUpCardI < 58 && firstPlay)
                    return doubleDown;
                return hit;
            }
            if (sum == 9) {
                if (dealerUpCardI > 50 && dealerUpCardI < 55)
                    return doubleDown;
                return hit;
            }
            return hit;
        }
    }

    public static int deviatedMove(char[] hand, char dealerUpCard, double trueCount) {
        //Assumptions
            //Dealer stands on soft 17
            //Double after split is allowed
        
        final int stand = 0;
        final int hit = 1;
        final int doubleDown = 2;
        final int split = 3;
        int sum = Blackjack.sum(hand);
        boolean soft = Blackjack.soft(hand);
        boolean firstPlay = Blackjack.firstPlay(hand);
        int dealerUpCardI = (int) dealerUpCard;

        //If we should split
        if (firstPlay && hand[0] == hand[1]) {
            //Deviations
            if ((hand[0] == 'T' || hand[0] == 'J' || hand[0] == 'Q' || hand[0] == 'K') && ((dealerUpCard == '4' && trueCount >= 6) || (dealerUpCard == '5' && trueCount >= 5) || (dealerUpCard == '6' && trueCount >= 4)))
                return split;
            //Others
            if (hand[0] == 'A' || hand[0] == '8')
                return split;
            if (hand[0] == '9' && !(dealerUpCard == '7' || dealerUpCard == 'T' || dealerUpCard == 'J' || dealerUpCard == 'Q' || dealerUpCard == 'K' || dealerUpCard == 'A'))
                return split;
            if (hand[0] == '7' && (dealerUpCardI < 56))
                return split;
            if (hand[0] == '6' && (dealerUpCardI < 55))
                return split;
            if (hand[0] == '4' && (dealerUpCard == '5' || dealerUpCard == '6'))
                return split;
            if ((hand[0] == '2' || hand[0] == '3') && (dealerUpCardI < 56))
                return split;
        }
        
        //Soft totals
        if (soft) {
            //Deviations
            if (sum == 19) {
                if (firstPlay && ((dealerUpCard == '4' && trueCount >= 3) || ((dealerUpCard == '5' || dealerUpCard == '5') && trueCount >= 1)))
                    return doubleDown;
            }
            if (sum == 17 && firstPlay && dealerUpCard == '2' && trueCount >= 1)
                return doubleDown;
            //Others
            if (sum > 19)
                return stand;
            if (sum == 19) {
                if (dealerUpCard == '6' && firstPlay)
                    return doubleDown;
                return stand;
            }
            if (sum == 18) {
                if (dealerUpCardI < 55 && firstPlay)
                    return doubleDown;
                if (dealerUpCardI < 57)
                    return stand;
                return hit;
            }
            if (sum == 17) {
                if (dealerUpCardI > 50 && dealerUpCardI < 55 && firstPlay)
                    return doubleDown;
                return hit;
            }
            if (sum == 15 || sum == 16) {
                if (dealerUpCardI > 51 && dealerUpCardI < 55 && firstPlay)
                    return doubleDown;
                return hit;
            }
            if (dealerUpCardI > 52 && dealerUpCardI < 55 && firstPlay)
                return doubleDown;
            return hit;
        } else {
            //Hard totals
            //Deviations
            if (sum == 16 && ((dealerUpCard == '9' && trueCount >= 4) || (dealerUpCardI > 73 && trueCount > 0)))
                return stand;
            if (sum == 15 && dealerUpCardI > 73 && trueCount >= 4)
                return stand;
            if (sum == 13 && trueCount <= -1)
                return hit;
            if (sum == 12) {
                if ((dealerUpCard == '2' && trueCount >= 3) || (dealerUpCard == '3' && trueCount >= 2))
                    return stand;
                if (dealerUpCard == '4' && trueCount < 0)
                    return hit;
            }
            if (sum == 11 && dealerUpCard == 'A' && trueCount >= 1)
                return hit;
            if (sum == 10 && firstPlay && ((dealerUpCardI > 73 && trueCount >= 4) || (dealerUpCard == 'A' && trueCount >= 4)))
                return doubleDown;
            if (sum == 9 && firstPlay && ((dealerUpCard == '2' && trueCount >= 1) || (dealerUpCard == '7' && trueCount >= 3)))
                return doubleDown;
            if (sum == 8 && firstPlay && dealerUpCard == '6' && trueCount >= 2)
                return doubleDown;
            //Others
            if (sum > 16)
                return stand;
            if (sum > 12 && sum < 17) {
                if (dealerUpCardI < 55)
                    return stand;
                return hit;
            }
            if (sum == 12) {
                if (dealerUpCardI > 51 && dealerUpCardI < 55)
                    return stand;
                return hit;
            }
            if (sum == 11) {
                if (firstPlay)
                    return doubleDown;
                return hit;
            }
            if (sum == 10) {
                if (dealerUpCardI < 58 && firstPlay)
                    return doubleDown;
                return hit;
            }
            if (sum == 9) {
                if (dealerUpCardI > 50 && dealerUpCardI < 55)
                    return doubleDown;
                return hit;
            }
            return hit;
        }
    }

    public static double bet(double money, double trueCount, double minimum) {
        double unit = money/1000;
        if (trueCount < 0)
            return minimum;
        if (trueCount < 2)
            if (unit > minimum)
                return unit;
            else
                return minimum;
        if ((trueCount - 1) * unit > minimum)
            return unit;
        else
            return minimum;
    }
}