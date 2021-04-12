import java.io.*;
import java.net.*;
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
                        System.out.println("RECEIVED FROM SERVER: " + fromServer);
                        String response = processServerResponse(fromServer);
                        if(!response.equals("")) {
                                outToServer.writeBytes(response+"\n");
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
        else if(sresponse.equals("Lmao")) {
                String response = "";
                try{
                        response = inFromUser.readLine();
                }
                catch(IOException e) {
                        System.out.println("Invalid Response.");
                }
                toReturn = response;
        }
        return toReturn;
}
}
