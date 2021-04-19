import java.io.*;
import java.net.*;
import java.util.*;
class TCPClient {

public static void main(String argv[]) throws Exception
{
        Socket clientSocket = new Socket("172.16.66.250", 8080);


        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

        BufferedReader inFromServer =new BufferedReader(new InputStreamReader(
                                                                clientSocket.getInputStream()));

        while (true) {
                String fromServer = inFromServer.readLine();
                if(fromServer != null) {
                        System.out.println(fromServer);
                        String toReturn = processServerResponse(fromServer);
                        if(!toReturn.equals("")) {
                                outToServer.writeBytes(toReturn+"\n");
                        }
                }
        }


}
public static String processServerResponse(String sresponse) throws Exception {
        String toReturn= "";
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        if(sresponse.equals("What is your name?")) {
                String name = "";
                try{
                        name = inFromUser.readLine();
                }
                catch(IOException e) {
                        System.out.println("Invalid Response.");
                }
                toReturn = name;
        }
        return toReturn;
}
}
