import java.util.*;

public class Player {
    
    private String playerName;
    private ArrayList<Card> myCards = new ArrayList<>();
    Deck deck = new Deck();

    public Player(String n) {
        playerName = n;
    }

    public void setName(String name) {
        playerName = name;
    }

    public String getName() {
        return playerName;
    }
    
    public void addCard(Card c) {
        myCards.add(c);
    }

    public Card playCard(Card c) {
        Card f = new Card("N",0);
        Card wildCard = new Card("N",0);
        boolean wild = false;
        for (Card card : myCards) {
            if (c.isMatch(card)) {
                myCards.remove(card);
                return card; 
            } else if (card.getColor().equals("x")) { //If there is wild card(I do this so that it evaluates all 
                                                      //normal cards first, then checks for wilds)
                wild = true; //To return wild card
                wildCard = card;
            }
        }
        if (wild) {
            myCards.remove(wildCard);
            return wildCard;
        }
        return f;//No match    
    }

    public void drawCard(Card c) {
        myCards.add(c);
    }

    public String displayHand() {
        return playerName + "'s Hand: " + myCards;
    }

    public boolean hasCards() {
        return !myCards.isEmpty();
    }

    public int getNumCards() {
        return myCards.size();
    }
}
