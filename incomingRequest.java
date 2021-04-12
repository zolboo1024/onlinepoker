import java.io.*;
import java.net.*;
import java.util.*;
public class incomingRequest {
public static void main(String argv[]) throws Exception {
// Get the port number from the command line.
        int port = (new Integer(argv[0])).intValue();
// Establish the listen socket.
        ServerSocket socket = new ServerSocket(port);
// Process incoming requests in an infinite loop.
        while (true) {
// Listen for a TCP connection request.
                Socket connection = socket.accept();
// Construct an object to process the incoming request
                serveRequest request = new serveRequest(connection);
// Create a new thread to process the request.
                Thread thread = new Thread(request);
// Start the thread.
                thread.start();
        }
}
}
