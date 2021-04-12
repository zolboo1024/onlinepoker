import java.util.Random;
import java.io.*;
public final class card {

private int suite;
private int card;

public card(){
        Random rand = new Random();
        int cardUpperBound = 13;
        card = rand.nextInt(cardUpperBound);
        int suiteUpperBound = 4;
        suite = rand.nextInt(suiteUpperBound);
}

public void print(){
        String toPrint = "";
        //Convert the card to a string.
        if(card==1 || card == 2 || card == 3 ||card == 4 ||card == 5 ||card == 6 ||card == 7 ||card == 8 ||card == 9) {
                toPrint = (card+1) + " of ";
        }
        else if(card == 0) {
                toPrint = "Ace of ";
        }
        else if(card == 10) {
                toPrint = "Jack of ";
        }
        else if(card == 11) {
                toPrint = "Queen of ";
        }
        else if(card == 12) {
                toPrint = "King of ";
        }

        //Convert the suite to a string.
        if(suite==0) {
                toPrint = toPrint + "hearts";
        }
        else if(suite==1) {
                toPrint = toPrint + "spades";
        }
        else if(suite==2) {
                toPrint = toPrint + "clubs";
        }
        else if(suite==3) {
                toPrint = toPrint + "diamonds";
        }
        System.out.println(toPrint);
}
}
