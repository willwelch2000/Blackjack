# Blackjack
This project has a card-counting simulator, as well as the classes needed to simulate a blackjack game  

Blackjack class represents a playable blackjack game for another Java class to utilize.  
BlackjackPlayer class is a class used by Blackjack to set up individual virtual "players."  
BasicStrategy class simulates a "perfectly-played" blackjack strategy using established principles of basic strategy.  
CountCards class expands on BasicStrategy by using card-counting techniques, including keeping track of a running "count" of high vs low cards.  
PlayBlackjack is an interface to allow a user to play blackjack using the Blackjack class.

**Use:**  
1. Compile all classes
2. To play blackjack via command line: "Java PlayBlackjack _number of players_" starts a game with an input number of players
Example: "Java PlayBlackjack 5" starts game of blackjack with 5 players  
The program will ask for the starting amount of money for each player.  
  -For each round, the program will request a bet from each player, and then it will ask each player for a move.  
  -Possible moves:   
    --"stand" (be done with move)  
    --"hit" (be dealt another card)  
    --"double" (double down your bet and be dealt exactly one more card--only possible on first move)  
    --"split" (split cards into two hands and play twice--only possible for hands with exactly two identical cards)  
  -After each round, the program will ask "Continue?"  
    --To continue, type "yes"  
    --To stop, type "no"
3. To run the card-counting simulator: "Java CountCards _number of rounds_" starts a simulation with an input number of rounds to simulate
Example: "Java CountCards 100" simulates 100 rounds of blackjack using a card-counting strategy
