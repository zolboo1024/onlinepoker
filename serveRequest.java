import java.io.*;
import java.net.*;
import java.util.*;
public class serveRequest implements Runnable {
Socket socket;
// Constructor
public serveRequest(Socket socket) throws Exception {
        this.socket = socket;
}
// Implement the run() method of the Runnable interface.
public void run() {
        try {
                processRequest();
        }
        catch (Exception e) {
                System.out.println(e);
        }
}
private void processRequest() throws Exception {
// Get a reference to the socket's input and output streams.
        InputStream is = socket.getInputStream();
        DataOutputStream os = new
                              DataOutputStream(socket.getOutputStream());
// Set up input streams
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while (true) {
// Get the incoming message from the client (read from socket)
                String msg = br.readLine();
//Print message received from client
                System.out.println("Received from client: ");
                System.out.println(msg);
//convert message to upper case
                String outputMsg = msg.toUpperCase();
//Send modified msg back to client (write to socket)
                os.writeBytes(outputMsg);
                os.writeBytes("\r\n");
                System.out.println("Sent to client: ");
        }
}
}
