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
public Game(){
        players = new player[8];
        numPlayers = 0;
        started = false;
        joinedSockets = new Socket[8];
}
public synchronized void addPlayer(player newPlayer, Socket playerSocket){
        if(started == false) {
                players[numPlayers] = newPlayer;
                joinedSockets[numPlayers] = playerSocket;
                numPlayers++;
                System.out.println("Player "+newPlayer.getName()+" has joined.\n");
                broadcast("Player "+newPlayer.getName()+"has joined.\n");
        }
        else {
                System.out.println("Game has already started.");
        }
}
public synchronized void startGame(){
        thisBoard = new board(players);
        started = true;
        System.out.println("The game has started.");
}
public static void broadcast(String toBroadcast){
        for(int i=0; i<numPlayers; i++) {
                try {
                        DataOutputStream os = new DataOutputStream(joinedSockets[i].getOutputStream());
                        os.writeBytes(toBroadcast+"\n");
                } catch (Exception e) {
                        System.out.println(e);
                }
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
        thisPlayer = new player(socket.getInetAddress().getHostAddress(),name);
        game.addPlayer(thisPlayer, socket);
        os.writeBytes("You have successfully joined the game.\n");
        boolean close = false;
        while(true) {
                String response = br.readLine();
                System.out.print(response);
                if(response.equals("quit")) {
                        game.broadcast("Player "+thisPlayer.getName()+"has quit.");
                        break;
                }
                else if(response.equals("start")) {
                        game.broadcast("Player "+thisPlayer.getName()+"has started the game.");
                        game.startGame();
                }
        }
        os.close();
        br.close();
        socket.close();
}
}
