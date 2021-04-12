import java.io.*;
import java.net.*;
import java.util.*;

public final class board {

//Keep track of all the flipped cards on the table.
private static card[] flipped;
private static int num;
//keep track of individual players.
private static player[] players;

public board(player[] thisPlayers){
        flipped = new card[5];
        num = 0;
        players = thisPlayers;
}

public static void flipACard(){
        if(num==5) {
                System.out.println("5 cards have already been flipped.");
        }
        else {
                card thisCard = new card();
                flipped[num] = thisCard;
                num++;
        }
}
public static void printBoard(){
        System.out.println("On the board:");
        for(int i=0; i<num; i++) {
                flipped[i].print();
        }
        System.out.println();
}
}
