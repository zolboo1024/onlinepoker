import java.io.*;
import java.net.*;
import java.util.*;

public final class board {

//Keep track of all the flipped cards on the table.
private static card[] flipped;
private static int num;
//keep track of individual players.
private static player[] players;
private static int moneyOnTheTable;
public board(player[] thisPlayers){
        flipped = new card[5];
        num = 0;
        players = thisPlayers;
}
public static int getMoneyOnTheTable(){
        return moneyOnTheTable;
}
public static void setMoneyOnTheTable(int update){
        moneyOnTheTable= update;
}
public static card[] getFlipped(){
        System.out.println("Flipped called");
        System.out.println(flipped[0].print());
        return flipped;
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
public static String printBoard(){
        String toReturn = "On the board: \n";
        for(int i=0; i<num; i++) {
                toReturn += flipped[i].print()+"\n";
        }
        return toReturn;
}
}
