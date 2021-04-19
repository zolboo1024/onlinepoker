import java.io.*;
import java.net.*;
import java.util.*;

public class player {

//Keep track of the player.
private card[] hand;
private int num;
private String id;
private int pool;
private String name;
public player(String thisId, String thisName){
        hand = new card[2];
        num = 0;
        id = thisId;
        pool = 800;
        name = thisName;
}

public void addACard(){
        if(num==2) {
                System.out.println("Player "+name+" already has 2 cards.");
        }
        else {
                card thisCard = new card();
                hand[num] = thisCard;
                num++;
        }
}
public String printHand(){
        String toReturn = "Player "+name+" has: ";
        toReturn += hand[0].print()+" and "+ hand[1].print();
        return toReturn;
}
public void assignHand(int index, card thisCard){
        hand[index] = thisCard;
        num++;
}
public String getName(){
        return name;
}
public int getPool(){
        return pool;
}
public void updatePool(int change){
        pool = pool + change;
}
}
