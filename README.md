# Blackjack
This project has a card-counting simulator, as well as the classes needed to simulate a blackjack game  

Blackjack class represents a playable blackjack game for another Java class to utilize.  
BlackjackPlayer class is a class used by Blackjack to set up individual virtual "players."  
BasicStrategy class simulates a "perfectly-played" blackjack strategy using established principles of basic strategy.  
CountCards class expands on BasicStrategy by using card-counting techniques, including keeping track of a running "count" of high vs low cards.  
PlayBlackjack is an interface to allow a user to play blackjack using the Blackjack class.

## Installation

Download the 5 Java classes. Ensure that Java is installed.
To compile the classes, do the following on the command line:
```bash
javac BlackjackPlayer.java
javac Blackjack.java
javac PlayBlackjack.java
javac BasicStrategy.java
javac CountCards.java
```

## Usage for playing blackjack with user input

```bash
# starts game with 5 players
Java PlayBlackjack 5
```

Program asks for starting money for all players
```bash
# gives player 1 $5 to start
Player 1 money:
5
```

For every round, program asks for bet for each player
```bash
# gives player 1 a bet of $2
Player 1 bet:
2
```

Then, program will ask each player for moves until that turn is over  
Possible moves:
1. stand--_stand_
2. hit--_hit_
3. double down bet--_double_
4. split hand--_split_
```bash
# stands (ends turn) for player 1
Player 1 move:
stand
```

Program asks "Continue?" Valid responses are _yes_ or _no_
```bash
Continue?
no
```

## Usage for simulating card counting

```bash
# runs simulation of 100 rounds of blackjack for a single player
Java CountCards 100
```
