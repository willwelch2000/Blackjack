import java.util.Random;

public class Blackjack {
    private final int stopIndex;
    private int count = 0;
    private final int playersNumber;
    private int decksNumber;
    private int turn = 0; //Dealer is 1 turn after last player
    private int index = 0;
    private BlackjackPlayer dealer; //FYI, dealer hits on soft 17
    private BlackjackPlayer[] players;
    private char[] deck;
    private boolean gameOver = false;

    public Blackjack(int playersNumber, int decksNumber) {
        deck = deckMaker(decksNumber);
        this.playersNumber = playersNumber;
        this.decksNumber = decksNumber;
        stopIndex = 39*decksNumber;
        players = new BlackjackPlayer[playersNumber];
        dealer = new BlackjackPlayer(0, 0);
        for (int i = 0; i < playersNumber; i++)
            players[i] = new BlackjackPlayer(i, 0);
    }

    //A few static methods

    public static int sum(char[] hand) {
        boolean ace = false;
        int toReturn = 0;
        for (int i = 0; i < hand.length; i++)
            if (hand[i] == 'A') {
                ace = true;
                toReturn += 1;
            } else if (hand[i] == 'T' || hand[i] == 'J' || hand[i] == 'Q' || hand[i] == 'K')
                toReturn += 10;
            else
                toReturn += Integer.parseInt(String.valueOf(hand[i]));
        if (ace && toReturn < 12)
            toReturn += 10;
        return toReturn;
    }

    public static boolean soft(char[] hand) {
        boolean ace = false;
        int total = 0;
        for (int i = 0; i < hand.length; i++)
            if (hand[i] == 'A') {
                ace = true;
                total += 1;
            } else if (hand[i] == 'T' || hand[i] == 'J' || hand[i] == 'Q' || hand[i] == 'K')
                total += 10;
            else
                total += Integer.parseInt(String.valueOf(hand[i]));
        if (ace && total < 12)
            return true;
        return false;
    }

    public static boolean firstPlay(char[] hand) {
        return hand.length == 2;
    }


    //Getters

    public boolean getGameOver() {
        return gameOver;
    }

    public double getTrueCount() {
        double decksLeft = decksNumber - (index / 52);
        return (double) count / decksLeft;
    }

    public char[] getHand(int player) {
        return players[player].getCards();
    }

    public char getDealerUpCard() {
        return dealer.getCards()[0];
    }

    public int getTurn() {
        return turn;
    }

    public double getMoney(int player) {
        return players[player].getMoney();
    }

    public double getBet(int player) {
        return players[player].getBet();
    }


    //Setters

    public void setMoney(int player, double money) {
        players[player].setMoney(money);
    }

    public void setBet(int player, double bet) {
        players[player].setBet(bet);
    }
    

    //Information

    public boolean soft(int player) {
        return soft(getHand(player));
    }

    public boolean firstPlay(int player) {
        return firstPlay(players[player].getCards());
    }

    public int sum(int player) {
        return sum(players[player].getCards());
    }


    //Actions

    public void deal() {
        gameOver = false;
        turn = 0;
        //Clear split hands
        BlackjackPlayer[] newPlayers = new BlackjackPlayer[playersNumber];
        int indexCount = 0;
        for (int i = 0; i < players.length; i++)
            //Add all the nonlinked ones (not result of split)
            if (!players[i].getIsLinked()) {
                newPlayers[indexCount] = players[i];
                newPlayers[indexCount].setOrder(indexCount);
                indexCount++;
            }
        players = newPlayers;

        //If we are past the stopIndex, reset the deck, index, and count
        if (index > stopIndex) {
            index = 0;
            count = 0;
            deck = deckMaker(decksNumber);
        }

        //Add Dealer and Players and their cards
        dealer = new BlackjackPlayer(0, 0);
        dealer.addCard(deck[index]);
        adjustCount(deck[index]);
        index++;
        dealer.addCard(deck[index]);
        index++;
        for (int i = 0; i < players.length; i++) {
            players[i].addCard(deck[index]);
            adjustCount(deck[index]);
            index++;
            players[i].addCard(deck[index]);
            adjustCount(deck[index]);
            index++;

            //Check for blackjack
            if (sum(players[i].getCards()) == 21) {
                players[i].win(true);
                advancePlayer();
            }
        }
    }

    public void hit() {
        if (sum(players[turn].getCards()) > 20)
            return;
        
        //Add card
        players[turn].addCard(deck[index]);
        adjustCount(deck[index]);
        index++;
        
        //Test to see if they lose
        if (sum(players[turn].getCards()) > 21) {
            players[turn].lose();

            advancePlayer();
        }

        if (sum(players[turn].getCards()) == 21)
            advancePlayer();
    }

    public void stand() {
        advancePlayer();
    }

    public void doubleDown() {
        if (sum(players[turn].getCards()) > 20)
            return;

        //Double bet
        players[turn].setBet(2*players[turn].getBet());

        //Add card
        players[turn].addCard(deck[index]);
        adjustCount(deck[index]);
        index++;
        
        //Test to see if they lose
        if (sum(players[turn].getCards()) > 21)
            players[turn].lose();

        advancePlayer();

        if (sum(players[turn].getCards()) == 21)
            advancePlayer();
    }

    public void split() {
        if (players[turn].getCards().length != 2 || players[turn].getCards()[0] != players[turn].getCards()[1])
            return;
        
        players[turn].removeCard();

        //Create new player array, copying the old one until turn, then moving the players after turn +1
        BlackjackPlayer[] newPlayers = new BlackjackPlayer[players.length + 1];
        for (int i = 0; i <= turn; i++)
            newPlayers[i] = players[i];
        for (int i = turn + 2; i < newPlayers.length; i++) {
            newPlayers[i] = players[i - 1];
            newPlayers[i].setOrder(i);
        }
        
        //Create a new player in the spot after turn
        newPlayers[turn + 1] = new BlackjackPlayer(turn + 1, players[turn].getMoney(), players[turn]);
        newPlayers[turn + 1].setBet(players[turn].getBet());

        //Set the old array equal to new one
        players = newPlayers;

        //Give this player the same card as the original
        char originalCard = players[turn].getCards()[0];
        players[turn + 1].addCard(originalCard);

        //Hit both (stay on first player's turn)
        int originalTurn = turn;
        hit();
        turn = originalTurn + 1;
        hit();
        turn = originalTurn;
    }


    public String toString() {
        String toReturn = "Players: " + playersNumber + "\nCount: " + count + "\nTurn: " + turn + "\nDealer: " + getDealerUpCard();
        if (gameOver)
            for (int i = 1; i < dealer.getCards().length; i++) {
                toReturn += " " + dealer.getCards()[i];
            }
        toReturn += /*"\nIndex: " + index + */"\n\n";
        for (int i = 0; i < players.length; i++)
            toReturn += players[i] + "\nSum: " + sum(players[i].getCards()) + "\n\n";
        /*toReturn += "\nCards: ";
        for (int i = 0; i < index; i++)
            toReturn += deck[i] + " ";
        if (gameOver)
            toReturn += "\nGame is over";*/
        toReturn += "\n------------------------\n";
        return toReturn;
    }


    //Private methods

    private char[] deckMaker(int number) {
        //Make organized deck
        char unshuffledDeck[] = new char[52*number];
        for (int i = 0; i < number; i++) {
            unshuffledDeck[52*i] = 'A';
            unshuffledDeck[52*i + 1] = 'A';
            unshuffledDeck[52*i + 2] = 'A';
            unshuffledDeck[52*i + 3] = 'A';
            for (int j = 2; j < 10; j++) {
                unshuffledDeck[52*i + 4*(j-1)] = (char) (j + '0');
                unshuffledDeck[52*i + 4*(j-1) + 1] = (char) (j + '0');
                unshuffledDeck[52*i + 4*(j-1) + 2] = (char) (j + '0');
                unshuffledDeck[52*i + 4*(j-1) + 3] = (char) (j + '0');
            }
            unshuffledDeck[52*i + 36] = 'T';
            unshuffledDeck[52*i + 37] = 'T';
            unshuffledDeck[52*i + 38] = 'T';
            unshuffledDeck[52*i + 39] = 'T';
            unshuffledDeck[52*i + 40] = 'J';
            unshuffledDeck[52*i + 41] = 'J';
            unshuffledDeck[52*i + 42] = 'J';
            unshuffledDeck[52*i + 43] = 'J';
            unshuffledDeck[52*i + 44] = 'Q';
            unshuffledDeck[52*i + 45] = 'Q';
            unshuffledDeck[52*i + 46] = 'Q';
            unshuffledDeck[52*i + 47] = 'Q';
            unshuffledDeck[52*i + 48] = 'K';
            unshuffledDeck[52*i + 49] = 'K';
            unshuffledDeck[52*i + 50] = 'K';
            unshuffledDeck[52*i + 51] = 'K';
        }
        
        //Initialize new deck (all 0s)
        char newDeck[] = new char[52*number];
        for (int i = 0; i < 52*number; i++) {
            newDeck[i] = '0';
        }

        //Replace each element with a random one from the unshuffled deck
        for (int i = 0; i < 52*number; i++) {
            while (newDeck[i] == '0') {
                Random random = new Random();
                int randomIndex = random.nextInt(52*number);
                if (unshuffledDeck[randomIndex] != '0') {
                    newDeck[i] = unshuffledDeck[randomIndex];
                    unshuffledDeck[randomIndex] = '0';
                }
            }
        }

        return newDeck;
    }

    private void adjustCount(char card) {
        if (card == '2' || card == '3' || card == '4' || card == '5' || card == '6')
            count++;
        if (card == 'T' || card == 'J' || card == 'Q' || card == 'K' || card == 'A')
            count--;
    }

    private void advancePlayer() {
        turn++;
        if (turn == players.length) {
            turn = 0;
            dealerMove();
        } else {
            if (players[turn].getCards()[0] == '0')
                advancePlayer();
        }
    }

    private void dealerMove() {
        //'Show' the dealer downcard
        adjustCount(dealer.getCards()[1]);

        //Check to see if it's necessary for the dealer to move
            //Like if all players have already lost
        boolean dealerShouldPlay = false;
        for (int i = 0; i < playersNumber; i++)
            if (players[i].getCards()[0] != '0')
                dealerShouldPlay = true;

        //Play as the dealer
        if (dealerShouldPlay)
            while (sum(dealer.getCards()) < 17 || (sum(dealer.getCards()) == 17 && soft(dealer.getCards()))) {
                dealer.addCard(deck[index]);
                adjustCount(deck[index]);
                index++;
            }

        //Check each player's result
        for (int i = 0; i < players.length; i++) {
            if (players[i].getCards()[0] == '0')
                break;

            //From this point on, we can assume that the player hasn't busted
            //So if the dealer busted, the player wins
            if (sum(dealer.getCards()) > 21)
                players[i].win();

            //If player's sum is higher, player wins
            if (sum(players[i].getCards()) > sum(dealer.getCards()))
                players[i].win();

            //If dealer' sum is higher, player loses
            if (sum(players[i].getCards()) < sum(dealer.getCards()))
                players[i].lose();

            //If tie, push
            if (sum(players[i].getCards()) == sum(dealer.getCards()))
                players[i].push();
        }

        gameOver = true;
    }
}