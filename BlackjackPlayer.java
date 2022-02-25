public class BlackjackPlayer {
    private int order;
    private double money;
    private double bet;
    private final BlackjackPlayer link;
    private boolean isLinked;
    private char[] cards = {'0'};

    public BlackjackPlayer(int order, double money, BlackjackPlayer link) {
        this.order = order;
        this.money = money;
        this.link = link;
        isLinked = true;
        this.bet = link.getBet();
    }

    public BlackjackPlayer(int order, double money) {
        this.order = order;
        this.money = money;
        link = null;
        isLinked = false;
    }

    public void setBet(double bet) {
        this.bet = bet;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    private void addMoney(double amount) {
        money += amount;
    }

    public void addCard(char card) {
        if (cards[0] == '0')
            cards[0] = card;
        else {
            char[] newCards = new char[cards.length + 1];
            for (int i = 0; i < cards.length; i++)
                newCards[i] = cards[i];
            newCards[cards.length] = card;
            cards = newCards;
        }
    }

    public void removeCard() {
        //Used exclusively to split, so the following is valid
        char card = cards[0];
        cards = new char[1];
        cards[0] = card;
    }

    public void win(Boolean blackjack) {
        if (isLinked)
            link.addMoney(blackjack ? 1.5*bet : bet);
        else
            money += blackjack ? 1.5*bet : bet;
        bet = 0;
        cards = new char[1];
        cards[0] = '0';
    }

    public void win() {
        win(false);
    }

    public void lose() {
        if (isLinked)
            link.addMoney(-bet);
        else
            money -= bet;
        bet = 0;
        cards = new char[1];
        cards[0] = '0';
    }

    public void push() {
        bet = 0;
        cards = new char[1];
        cards[0] = '0';
    }

    public boolean getIsLinked() {
        return isLinked;
    }

    public int getOrder() {
        return order;
    }

    public double getMoney() {
        return isLinked ? link.getMoney() : money;
    }

    public double getBet() {
        return bet;
    }

    public char[] getCards() {
        return cards;
    }

    public String toString() {
        String toReturn = "Player " + order + (isLinked ? ("") : ("\nMoney: " + money)) + "\nBet: " + bet + "\n" + (isLinked ? ("Link: " + link.getOrder()) + "\n": "") + "Cards: ";
        for (int i = 0; i < cards.length; i++)
            toReturn += cards[i] + " ";
        return toReturn;
    }
}