/* Riley Monwai 
   CST-105 MIlestone Project
   This project is the main method for the UNO game.
   It deals cards to players, and runs through a loop, 
   playing cards until a player has no cards left */

import java.util.*;

public class Driver {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        String playAgain = "Y";
        do { //Play game while user wants to play again
            System.out.println("-----------------------------------------------------------------------");
            System.out.println("Welcome to UNO!!");
            Deck fullDeck = new Deck();
            Player p1 = new Player("Rob"); 
            Player p2 = new Player("Ben");
            Player p3 = new Player("Mark"); 
            Player p4 = new Player("Tom");
            ArrayList<Player> players = new ArrayList<>();
            players.add(p1); players.add(p2);
            players.add(p3); players.add(p4);

            //Sets player amount and players' names
            System.out.println("Press \"1\" to play with 4 preset players or... ");
            System.out.print("Enter how many you would like to play with!(2,3, or 4):  ");
            int numPlayers = input.nextInt();
            if (numPlayers == 3) {
                players.remove(p4); 
                for (int i = 0; i < players.size(); i++) {
                    System.out.print("Please enter name for Player " + (i+1) + ": ");
                    players.get(i).setName(input.next());
                }
            } else if (numPlayers == 2) {
                players.remove(p4); 
                players.remove(p3);
                for (int i = 0; i < players.size(); i++) {
                    System.out.print("Please enter name for Player " + (i+1) + ": ");
                    players.get(i).setName(input.next());
                }
            } else if (numPlayers == 4) {
                for (int i = 0; i < players.size(); i++) {
                    System.out.print("Please enter name for Player " + (i+1) + ": ");
                    players.get(i).setName(input.next());
                }
            }

            Boolean ai = true; //Game plays itself
            // Under Development
            // System.out.print("Do you want to play UNO manually? (Y for yes, N for no): ");
            // if (input.next().equalsIgnoreCase("Y")) {
            //     ai = false;
            // }

            // Boolean chooseWildCards = true; //Choosing wild card color manually 
            // System.out.print("Do you want to choose wild card color? (Y for yes, N for no): ");
            // if (input.next().equalsIgnoreCase("N")) {
            //     chooseWildCards = false;
            // }

            fullDeck.shuffle();
            System.out.println("Shuffling deck...");

            dealToPlayers(players, fullDeck); //Deals cards to currentPlayers

            for (Player currentPlayer : players) { //Print currentPlayer's hands
                System.out.println(currentPlayer.displayHand());            
            } 

            fullDeck.addFirstDiscard(); //Adds initial card to discard pile

            System.out.println("-----------------------------------------------------------------------");
            System.out.println(p1.getName() + " starts!");
            if (ai) { 
                playAgain = freePlay(players, fullDeck);
            }
        } while (playAgain.equalsIgnoreCase("Y"));
    }

    public static String freePlay(ArrayList<Player> players, Deck fullDeck) { //Game loop
        Scanner input = new Scanner(System.in);
        Boolean winner = false;
        while (!winner) {
            for (int i = 0; i < players.size(); i++) { 
                Player currentPlayer = players.get(i);                 
                Player nxtPlayer = getNextPl(currentPlayer, players); //Next player
                System.out.println("-----------------------------------------------------------------------");        
                System.out.println(currentPlayer.displayHand()); //Display player's current hand
                Card discard = currentPlayer.playCard(fullDeck.getTopCard());
                System.out.println("Top card is: " + fullDeck.getTopCard()); //Print top card of discard pile
                // Wild Cards
                int cardValue = discard.getValue();
                if (cardValue == 13 || cardValue == 14) {
                    Boolean wildPlus4 = wildCard(currentPlayer, nxtPlayer, discard, cardValue, fullDeck);
                    if (wildPlus4) {
                        i = players.indexOf(nxtPlayer); //skips ahead 1 i value
                        System.out.println(nxtPlayer.getName() + " must draw 4 and is skipped!");
                        nxtPlayer = getNextPl(nxtPlayer, players); 
                    }
                } 
                else if (discard.getColor().equals("N")) {//If currentPlayer can't play
                    drawCard(currentPlayer, nxtPlayer, fullDeck);
                } 
                else {
                    fullDeck.discardCard(discard); //Adds card to discard
                    System.out.println(currentPlayer.getName() + " played: " + discard + "... " + currentPlayer.getNumCards() + " cards left!");//Prints the card played
                    // if currentPlayer doesn't have cards, end the game
                    if (!currentPlayer.hasCards()) {
                        System.out.println(currentPlayer.getName() + " Wins!");
                        winner = true; //Change end condition
                        System.out.println("Would you like to play again? (Y for yes, N for no): ");
                        return input.next();
                    }
                    // Skip card
                    else if(cardValue == 10) {
                        System.out.println(currentPlayer.getName() + " skipped " + nxtPlayer.getName() + "!");
                        i = players.indexOf(nxtPlayer); //Skips ahead 1 i value
                        nxtPlayer = getNextPl(nxtPlayer, players); //New value for "nxtPlayer"
                    }
                    // Draw 2
                    else if(cardValue == 11) {                            
                        System.out.println(nxtPlayer.getName() + " must draw 2!");
                        for (int j = 0; j < 2; j++) { //Adds 2 cards to next player's hand
                            Card draw = fullDeck.deal();
                            nxtPlayer.addCard(draw);
                        }
                        i = players.indexOf(nxtPlayer); //skips ahead 1 i value
                        nxtPlayer = getNextPl(nxtPlayer, players); //New value for "nxtPlayer"
                    } 
                    // Reverse
                    else if(cardValue == 12) {
                        ArrayList<Player> temp = new ArrayList<>(); //Temp array so you can clear original to swap values
                        for (Player pl : players) {
                            temp.add(pl); //Adding players to temp 
                        }
                        players.clear();
                        players = reverse(temp); //Adding new reversed players back
                        i = players.indexOf(nxtPlayer)-1; //Skips ahead 1 i value
                        nxtPlayer = players.get(i+1);  //New value for "nxtPlayer"
                    } 
                    // Print uno if currentPlayer has 1 card remaining
                    if (currentPlayer.getNumCards() == 1) {
                        System.out.println("UNO!!! " + currentPlayer.getName() + " has 1 card remaining!");            
                    }
                    
                    // Restock Deck 
                    if (fullDeck.deckSize() <= 0) {
                        fullDeck.reset();
                    } 
                }
                System.out.println(nxtPlayer.getName() + "'s turn!");
            }
        }
        return "Y";
    }
    
    //Deal cards to all currentPlayers
    public static void dealToPlayers(ArrayList<Player> p, Deck deck) {
        System.out.println("-----------------------------------------------------------------------");
        for (int i = 0; i < 7; i++) {
            for (Player currentPlayer : p) {
                currentPlayer.addCard(deck.deal());
            }
        }
    }
    public static void drawCard(Player currentPlayer, Player nxtPlayer, Deck fullDeck) {
        Card draw = fullDeck.deal(); //Draw card 
        currentPlayer.addCard(draw);
        System.out.println(currentPlayer.getName() + " cannot play...");
        System.out.println("He draws " + draw);
    }
    //Returns the next currentPlayer in line
    public static Player getNextPl (Player currentPlayer, ArrayList<Player> p) {
        for (int i = 0; i < p.size(); i++) {
            if (p.get(i) == currentPlayer) {
                if (i == p.size()-1) {
                    return p.get(0);
                } else {   
                    return p.get(i+1);
                }
            }
        }
        return currentPlayer;
    }
    //Reverse Card
    public static ArrayList<Player> reverse(ArrayList<Player> p) {
        ArrayList<Player> reversePlayers = new ArrayList<>();
        for (int i = p.size(); i > 0; i--) {
            reversePlayers.add(p.get(i-1));
        }
        return reversePlayers;
    }
    //Wild Cards
    public static Boolean wildCard(Player currentPlayer, Player nxtPlayer, Card discard, int cardValue, Deck fullDeck) {
        Scanner input = new Scanner(System.in);
        System.out.println(currentPlayer.getName() + " has a wild card... "); 
        System.out.print("What color would you like it to be? ");
        String color = input.next();
        while (!color.equalsIgnoreCase("Blue") && !color.equalsIgnoreCase("Red") && !color.equalsIgnoreCase("Yellow") && !color.equalsIgnoreCase("Green")) {
            System.out.print("Invalid color, please enter a valid color: ");
            color = input.next();
            // wildCard(currentPlayer, nxtPlayer, discard, cardValue, fullDeck);
        } 
        discard.setColor(color); //Set new color value
        fullDeck.discardCard(discard);
        System.out.println(currentPlayer.getName() + " played " + discard.toString());        
            
        if (cardValue == 14) { //Wild draw 4
            for (int j = 0; j < 4; j++) { //Adds 4 cards to next player's hand
                Card draw = fullDeck.deal();
                nxtPlayer.addCard(draw);
            }
            return true;
        } 
        return false;
    }}