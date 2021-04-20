import java.io.*;
import java.net.*;
import java.util.*;
public final class Poker {
public static int numPlayers;
public static Game game;
public static void main(String argv[]) throws Exception
{
        game = new Game();
        // Set the port number.
        int port = 8080;
        // Establish the listen socket.
        ServerSocket socket = new ServerSocket(port);
        // Process incoming requests in an infinite loop.
        while (true) {
                // Listen for a TCP connection request.
                Socket connection = socket.accept();
                // // Construct an object to process the incoming request
                // serveRequest request = new serveRequest(connection);
                // // Create a new thread to process the request.
                // Thread thread = new Thread(request);
                // // Start the thread.
                // thread.start();
                // Construct an object to process the HTTP request message.
                Player request = new Player(connection);
                // Create a new thread to process the request.
                Thread thread = new Thread(request);
                // Start the thread.
                thread.start();
        }
}
}

final class Game {
public static boolean started;
public static board thisBoard;
public static player[] players;
public static int numPlayers;
public static Socket[] joinedSockets;
public static int roundNum;
public static int numFolded;
public static int winner;
public static int roundWinner;
public Game(){
        players = new player[2];
        numPlayers = 0;
        started = false;
        numFolded = 0;
        joinedSockets = new Socket[2];
        winner = -1;
        roundWinner = -1;
}
public synchronized void addPlayer(player newPlayer, Socket playerSocket){
        if(started == false) {
                players[numPlayers] = newPlayer;
                joinedSockets[numPlayers] = playerSocket;
                numPlayers++;
                System.out.println("Player "+newPlayer.getName()+" has joined.\n");
                broadcast("Player "+newPlayer.getName()+" has joined.\n");
                if(numPlayers>1) {
                        broadcast("2 players have joined. Game is starting\n");
                        startGame();
                }
                else if(numPlayers == 1) {
                        broadcast("Waiting for the second player.\n");
                }
        }
        else {
                System.out.println("Game has already started."+Player.CRLF);
        }
}
public void startGame(){
        thisBoard = new board(players);
        int roundSum = 0;
        started = true;
        broadcast("The game has started."+Player.CRLF);
        for(int i=0; i<numPlayers; i++) {
                for(int j=0; j<2; j++) {
                        card newCard = new card();
                        players[i].assignHand(j,newCard);
                }
                unicast(i, players[i].printHand());
        }
        roundSum = betRound(roundSum);
        if(roundWinner != -1) {
                finishRound(roundWinner);
        }
        broadcast("Flipping the first common card.\n");
        thisBoard.flipACard();
        broadcast("On the board, we have: "+thisBoard.print()+"\n");
        broadcast("Flipping the second common card.\n");
        thisBoard.flipACard();
        broadcast("On the board, we have: "+thisBoard.print()+"\n");
        roundSum = betRound(roundSum);
        if(roundWinner != -1) {
                finishRound(roundWinner);
        }
        broadcast("Flipping the third common card.\n");
        thisBoard.flipACard();
        broadcast("On the board, we have: "+thisBoard.print()+"\n");
        roundSum = betRound(roundSum);
        if(roundWinner != -1) {
                finishRound(roundWinner);
        }
        broadcast("Flipping the fourth common card.\n");
        thisBoard.flipACard();
        broadcast("On the board, we have: "+thisBoard.print()+"\n");
        roundSum = betRound(roundSum);
        if(roundWinner != -1) {
                finishRound(roundWinner);
        }
        broadcast("Flipping the fifth common card.\n");
        thisBoard.flipACard();
        broadcast("On the board, we have: "+thisBoard.print()+"\n");
        roundSum = betRound(roundSum);
        if(roundWinner != -1) {
                finishRound(roundWinner);
        }
        //determine the winner through evaluation
        for(int i=0; i<numPlayers; i++) {
                players[i].combineHand(thisBoard.getFlipped);
                evaluate(i);
        }
}
public void evaluate(int thisPlayer){

        //get combinedHand
        card[] thisHand = players[thisPlayer].getCombinedHand();

        //Royal flush
        if(evaluateIfRoyalFlush(thisHand)) {
                players[thisPlayer].setPoints(1);
                return;
        }
        //Straight flush
        else if(evaluateIfStraightFlush(thisHand)) {
                players[thisPlayer].setPoints(2);
                return;
        }
        //4 of a kind
        else if(evaluateIfFourOfaKind(thisHand)) {
                players[thisPlayer].setPoints(3);
                return;
        }
        //full house
        else if(evaluateIfFullHouse(thisHand)) {
                players[thisPlayer].setPoints(4);
                return;
        }
        //flush
        else if(evaluateIfFlush(thisHand)) {
                players[thisPlayer].setPoints(6);
                return;
        }
        //straight
        else if(evaluateIfStraight(thisHand)) {
                players[thisPlayer].setPoints(7);
                return;
        }
        //straight
        else if(evaluateIfTriple(thisHand)) {
                players[thisPlayer].setPoints(7);
                return;
        }
}
public bool evaluateIfRoyalFlush(card[] hand){
        if(hand.evaluateContainsCard(0, hand) && hand.evaluateContainsCard(12, hand)
           && hand.evaluateContainsCard(11, hand) && hand.evaluateContainsCard(10, hand) && hand.evaluateContainsCard(9, hand)) {
                return true;
        }
        else{
                return 0;
        }
}
public bool evaluateIfStraightFlush(card[] hand){
        if(evaluateIfFlush(hand) && evaluateIfStraight(hand)) {
                return true;
        }
        else {
                return false;
        }
}
public bool evaluateIfFlush(card[] hand){
        for(int i=0; i<3; i++) {
                int thisSuite = hand[i].getSuite();
                int numSameSuites = 1;
                for(int j=i+1; j<7; j++) {
                        if(hand[j].getSuite() == thisSuite) {
                                numSameSuite++;
                        }
                }
                if(numSameSuites>=5) {
                        return true;
                }
        }
        return false;
}
public bool evaluateIfFourOfaKind(card[] hand){
        for(int i=0; i<7; i++) {
                int thisCard = hand[i].getCard();
                if(evaluateContainsCombo(0, thisCard, hand) && evaluateContainsCombo(1, thisCard, hand)
                   && evaluateContainsCombo(2, thisCard, hand) && evaluateContainsCombo(3, thisCard, hand)) {
                        return true;
                }
        }
        return false;
}
public bool evaluateIfFullHouse(card[] hand){
        int triple = false;
        int doublE = false;
        for(int i=0; i<7; i++) {
                int thisCard = hand[i].getCard();
                int sameNum = 0;
                for(int j=0; j<4; j++) {
                        if(evaluateContainsCombo(j, thisCard, hand)) {
                                sameNum++;
                        }
                }
                if(sameNum==3) {
                        triple = true;
                }
                else if(sameNum==2) {
                        doublE = true;
                }
        }
        if(tripe&&doublE) {
                return true;
        }
        return false;
}
public bool evaluateIfTriple(card[] hand){
        for(int i=0; i<7; i++) {
                int thisCard = hand[i].getCard();
                int sameNum = 0;
                for(int j=0; j<4; j++) {
                        if(evaluateContainsCombo(j, thisCard, hand)) {
                                sameNum++;
                        }
                }
                if(sameNum==3) {
                        return true;
                }
        }
        return false;
}
public bool evaluateIfTwoPairs(card[] hand){
        int count = 0;
        for(int i=0; i<7; i++) {
                int thisCard = hand[i].getCard();
                int sameNum = 0;
                for(int j=0; j<4; j++) {
                        if(evaluateContainsCombo(j, thisCard, hand)) {
                                sameNum++;
                        }
                }
                if(sameNum==2) {
                        count++;
                }
        }
        if(count==2) {
                return true;
        }
        return false;
}
public bool evaluateIfPair(card[] hand){
        for(int i=0; i<7; i++) {
                int thisCard = hand[i].getCard();
                int sameNum = 0;
                for(int j=0; j<4; j++) {
                        if(evaluateContainsCombo(j, thisCard, hand)) {
                                sameNum++;
                        }
                }
                if(sameNum==2) {
                        return true;
                }
        }
        return false;
}
public bool evaluateIfStraight(card[] hand){
        for(int i=0; i<7; i++) {
                int thisCard = hand[i].getCard();
                if(evaluateContainsCard((thisCard+1)%12, hand) && evaluateContainsCard((thisCard+2)%12, hand)
                   &&evaluateContainsCard((thisCard+3)%12, hand) &&evaluateContainsCard((thisCard+4)%12, hand)) {
                        return true;
                }
        }
        return false;
}
public bool evaluateContainsCombo(int suite, int card, card[] hand){
        if(evaluateContainsSuite(suite, card) && evaluateContainsCard(card, hand)) {
                return true;
        }
        else{
                return false;
        }
}
public bool evaluateContainsCard(int card, card[] hand){
        bool found = false;
        for(card each: hand) {
                if(each.getCard()==card) {
                        found = true;
                }
        }
        return found;
}
public bool evaluateContainsSuite(int suite, card[] hand){
        bool found = false;
        for(card each: hand) {
                if(each.getCard()==card) {
                        found = true;
                }
        }
        return found;
}
public void finishRound(int thisRoundWinner){
        broadcast("Player "+players[thisRoundWinner]+"has won this round.");
        players[roundWinner].updatePool(roundSum);

}
public int betRound(int roundSum){
        int currentBet = 0;
        int currentPool = roundSum; //make a copy
        for(int i=0; i<numPlayers; i++) {
                if(numFolded+1==numPlayers && !players[i].checkFolded) {
                        broadcast("Player "+players[i].getName()+" has won.\n");
                        roundWinner = i;
                        return currentPool;
                }
                if(players[i].checkFolded() || players[i].getPool()<=0) {
                        continue;
                }
                broadcast("It is "+players[i]+"\'s turn.\n");
                if(currentBet == 0) {
                        unicast(i, "Bet, check or fold?\n");
                        unicast(i, "You have "+players[i].getPool()+" credits.\n");
                }
                else {
                        unicast(i, "Bet, call or fold? The current bet is "+currentBet+"\n");
                        unicast(i, "You have "+players[i].getPool()+" credits.\n");
                }
                // Get a reference to the socket's input and output streams.
                InputStream is = joinedSockets[i].getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String action = br.readLine();
                if(action.contains("Bet")) {
                        int thisBet = Integer.parseInt(action.split(" ")[1]);
                        if(thisBet<=currentBet) {
                                unicast(i, "Bet must be greater than "+currentBet+".\n");
                                i--;
                                continue;
                        }
                        else if(thisBet>=players[i].getPool()) {
                                unicast(i, "You don't have enough money. You have "+players[i].getPool()+".\n");
                                i--;
                                continue;
                        }
                        else {
                                currentBet = thisBet;
                                currentPool += thisBet;
                                players[i].updatePool(-thisBet);
                                broadcast("Player "+players[i].getName()+" has bet "+thisBet+" credits.\n");
                                broadcast("There is "+currentPool+" on the table.\n");
                        }
                }
                else if(action.contains("Check")) {
                        if(currentBet!=0) {
                                unicast(i, "You can't check. And the current bet is "+currentBet+".\n");
                                i--;
                                continue;
                        }
                        else {
                                broadcast("Player "+players[i].getName()+" has checked.\n");
                                broadcast("There is "+currentPool+" on the table.\n");
                        }
                }
                else if(action.contains("Call")) {
                        if(currentBet>=players[i].getPool()) {
                                broadcast("Player "+players[i].getName()+" has went all in.\n");
                                currentPool += players[i].getPool();
                                players[i].updatePool(-players[i].getPool());
                                continue;
                        }
                        else {
                                broadcast("Player "+players[i].getName()+" has called. And the current bet is "+currentBet+".\n");
                                currentPool += currentBet;
                                players[i].updatePool(-currentBet);
                                continue;
                        }
                }
                else if(action.contains("Fold")) {
                        broadcast("Player "+players[i].getName()+" has folded.\n");
                        players[i].fold();
                }
        }
        return currentPool;
}
public static void unicast(int playerId, String toUnicast){
        try {
                DataOutputStream os = new DataOutputStream(joinedSockets[playerId].getOutputStream());
                os.writeBytes(toUnicast+"\n");
        } catch (Exception e) {
                System.out.println(e);
        }
}
public static void broadcast(String toBroadcast){
        for(int i=0; i<numPlayers; i++) {
                unicast(i, toBroadcast);
        }
}
}


final class Player implements Runnable {
final static String CRLF = "\r\n";
Socket socket;
// Constructor
public Player(Socket socket) throws Exception {
        this.socket = socket;
}
// Implement the run() method of the Runnable interface.
public void run(){
        try {
                listenToPlayer();
        } catch (Exception e) {
                System.out.println(e);
        }
}
private void listenToPlayer() throws Exception {
        Game game = Poker.game;
        player thisPlayer;
        // Get a reference to the socket's input and output streams.
        InputStream is = socket.getInputStream();
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        // Set up input stream filters.
        //FilterInputStream input_filter = BufferInputStream(is);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        os.writeBytes("What is your name?\n");
        String name = br.readLine();
        System.out.println("Name: "+name);
        thisPlayer = new player(socket.getInetAddress().getHostAddress(),name);
        os.writeBytes("You have successfully joined the game.\n");
        game.addPlayer(thisPlayer, socket);
        boolean close = false;
        while(true) {
                String response = br.readLine();
                System.out.print(response);
                if(response.equals("quit")) {
                        game.broadcast("Player "+thisPlayer.getName()+"has quit."+CRLF);
                        break;
                }
        }
        os.close();
        br.close();
        socket.close();
}
}
