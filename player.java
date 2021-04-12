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
public void printHand(){
        System.out.println("Player "+name+" has:");
        for(int i=0; i<num; i++) {
                hand[i].print();
        }
        System.out.println();
}
public String getName(){
        return name;
}
}
