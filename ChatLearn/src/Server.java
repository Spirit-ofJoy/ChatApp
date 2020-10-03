
import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
//import java.util.Set;
//import java.util.HashSet;
import java.util.Vector;

/*class Chat implements Serializable {
    private String message;
    private String from;

    public Chat(String message, String from) {
        this.message = message;
        this.from = from;
    }

    @Override
    public String toString() {
        return String.format("%s: \n%s\n", from, message);
    }
}*/

public class Server {

    private static final int PORT = 5436;
    static Vector<HandleClient> clientRecord = new Vector<>();
    public static int i = 0;

    public static void main(String[] args) {
        ServerSocket serverSocket;
        Socket client;


        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("[SERVER] Started and waiting....");

            while (true) {
                client = serverSocket.accept();
                System.out.println("[SERVER]New Client connected.");

                HandleClient temp;
                Thread t = new Thread(temp = new HandleClient(client, "client#" + i));
                clientRecord.add(temp);
                t.start();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class HandleClient implements Runnable {
        private Socket clientSocket;
        private String name;
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;

        public HandleClient(Socket socket, String s) {
            this.clientSocket = socket;
            this.name=s;

            try {
                objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            Chat messageReceived = null;
            while (true) {
                try {
                    messageReceived = (Chat) objectInputStream.readObject();
                    System.out.println(messageReceived);

                    for(HandleClient temp : clientRecord){
                        temp.objectOutputStream.writeObject(messageReceived);
                        temp.objectOutputStream.flush();
                    }

                } catch (EOFException e) {
                    System.out.println("User logged of.");
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    break;
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}