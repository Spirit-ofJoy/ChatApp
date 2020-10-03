
import java.io.*;
import java.net.Socket;

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

public class Client {

    private static final int Server_PORT = 5436;
    private static boolean terminate = false;


    public static void main(String[] args){
        try {
            Socket mySocket = new Socket("127.0.0.1", Server_PORT);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(mySocket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(mySocket.getInputStream());
            BufferedReader keyboardInput = new BufferedReader(new InputStreamReader(System.in));


            System.out.print("Name:");
            String clientName = keyboardInput.readLine();

            
            Thread t = new Thread(new ReceiveMessage(mySocket, objectInputStream));
            t.start();

            while (true) {
                sendChat(clientName, objectOutputStream);
                if(terminate){ break; }

            }

            mySocket.close();

        } catch (IOException e){
            e.printStackTrace();
        } //catch (ClassNotFoundException e){
          //  e.printStackTrace();
       // }
    }

    public static void sendChat(String name, ObjectOutputStream out) throws IOException {
        BufferedReader keyboardInput = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("> ");
        String msg = keyboardInput.readLine();

        if(msg.equals("EXIT")){ terminate=true; }

        Chat clientMsg = new Chat(msg, name);
        out.writeObject(clientMsg);
        out.flush();
    }

    public static class ReceiveMessage extends Thread{
        private Socket server;
        private ObjectInputStream inputStream;

        ReceiveMessage(Socket socket, ObjectInputStream Stream){
            this.server = socket;
            this.inputStream = Stream;
        }
        public void run(){
            try {
                System.out.println(inputStream.readObject());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

}