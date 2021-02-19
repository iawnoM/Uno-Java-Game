import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    
    private ArrayList<Card> theDeck;
    private ArrayList<Card> discard;


    public Deck() {
        theDeck = new ArrayList<Card>();
        discard = new ArrayList<Card>();
        // Add wild cards
        for (int i = 0; i < 5; i++) {
            theDeck.add(new Card("x",14));
            theDeck.add(new Card("x",13));
        }
        String [] colors = {"Blue", "Green", "Red", "Yellow"};
        // Add 0 value cards to theDeck
        for (String a : colors) {
            Card newCard = new Card(a, 0);
            theDeck.add(newCard);
        }
        // Add rest of the cards to theDeck
        for (int i = 0; i < 2; i++) {
            for (String c : colors) {
                for (int j = 1; j <= 12; j++) {
                    Card newCard = new Card(c, j);
                    theDeck.add(newCard);
                }
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(theDeck);
    }
    
    public String toString() {
        return "theDeck: " + theDeck;
    }

    public Card deal() {
        return theDeck.remove(theDeck.size()-1);
    }

    public void addFirstDiscard() {
        discard.add(theDeck.remove(theDeck.size()-1));
        int i = discard.get(0).getValue();
        if (i == 10 || i == 11 || i == 12 || i == 13 || i == 14) { //If first card is a fun card
            Card c = discard.remove(0); //Remove card from discard pile
            theDeck.add(0, c); //Add card back to the bottom of the deck
            addFirstDiscard(); //Run method until card is a number card
        }
    }

    public int deckSize() { 
        return theDeck.size();
    }

    // Discard Pile Methods

    public void discardCard (Card c){
        discard.add(c);
    }

    public void reset(){ // restocks deck
        Card temp = discard.remove(discard.size()-1);
        Collections.shuffle(discard);
        for (Card card : discard) {
            theDeck.add(card);
        }
        discardCard(temp);
    }

    public Card getTopCard() { //Returns top card on discard pile
        return discard.get(discard.size()-1);
    }

    public String displayPile() {
        return "Discard Pile: " + discard;
    }
}
