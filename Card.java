public class Card {

    private String color;
    // Skip = 10, Draw2 = 11, Reverse = 12, Wild = 13
    // Wild+4 = 14
    private int value;

    public Card(String c, int v) {
        color = c;
        value = v;
    }

    public void setColor(String cardColor) {
        color = cardColor;
    }

    public String getColor() {
        return color;
    }

    public void setValue(int num) {
        value = num;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        String first = "[" + color + ", ";
        if (value == 10) {
            return first + "SK]";
        } else if (value == 11) {
            return first + "DR2]";
        } else if (value == 12) {
            return first + "RV]";
        } else if (value == 13) {
            return first + "Wild]";
        } else if (value == 14) {
            return first + "Wild+4]";        
        } else { 
            return first + value + "]";
        }
    }

    public boolean isMatch(Card nextCard) {
        boolean returnValue = false;
        if (this.color.equals("x")) {
            returnValue = true;
        } 
        else if (this.color.equalsIgnoreCase(nextCard.color) || this.value == nextCard.value) {
            returnValue = true;
        }
        return returnValue;
    }
}
