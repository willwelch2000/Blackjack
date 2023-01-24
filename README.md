# Blackjack
This project contains all the files for a card-counting simulator, a basic-strategy simulator, and a command-line Blackjack game. 

## Installation
From a command line, with Java installed:
```bash
javac CountCards.java
javac PlayBlackjack.java
javac BasicStrategy.java
```

## Usage for playing blackjack with user input

```bash
# starts game with 5 players
java PlayBlackjack 5
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

Then, program will ask each player for moves until that turn is over. 
The possible moves are:
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

## Usage for simulating basic strategy
```bash
# runs simulation of 100 rounds of blackjack for a single player starting with $1000
java BasicStrategy 1000 100
```

## Usage for simulating card counting
```bash
# runs simulation of 100 rounds of blackjack for a single player
java CountCards 100
```

## Explanation of the separate classes created
### BasicStrategy.java
* Class that can be directly run to simulate playing blackjack with basic strategy.

### Blackjack.java
* This acts as an interface to play blackjack and is used by the other classes. 

### BlackjackPlayer.java
* This represents a single player in a blackjack game and is used by Blackjack.java.

### CountCards.java
* Class that can be directly run to simulate playing blackjack while counting cards.
* Card counting is a legal (yet sometimes frowned upon) strategy in which the player keeps track of previously played cards and uses that knowledge to their advantage. The user adjusts their gameplay and betting value to take advantage of high-value situations. 

### PlayBlackjack.java
* Class that allows the user to play blackjack via the command line. 