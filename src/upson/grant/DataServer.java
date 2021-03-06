package upson.grant;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.sql.Timestamp;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class DataServer
{
    private final ConcurrentHashMap<String, Query> results;
    private final PriorityBlockingQueue<Message> requests;
    private final int capacity;

    private final int port;

    public DataServer(int port, int capacity)
    {
        results = new ConcurrentHashMap<>(2000);
        requests = new PriorityBlockingQueue<>(500);
        this.port = port;
        this.capacity = capacity;
    }

    public void launch()
    {
        System.out.println("Starting Data Server, worker capacity set to: " + capacity);

        new Thread(new TweetHandler("localhost", 9999, requests)).start();
        new Thread(new QueryHandler(7777, capacity, requests, results)).start();

        try(ServerSocket listeningConnection = new ServerSocket(port))
        {
            while(true)
            {
                Socket clientConnection = listeningConnection.accept();
                System.out.println("Client connected from " + clientConnection.getInetAddress() + " on port " + clientConnection.getLocalPort() +
                        " at " + new Timestamp(System.currentTimeMillis()));
                new Thread(new ClientRequest(clientConnection, requests, results)).start();
            }
        }
        catch(IOException ioException)
        {
            System.out.println("Error: " + ioException.getMessage());
        }
    }

    public static void main(String[] args)
    {
        if(args.length != 2)
        {
            System.out.println("Error: Invalid number of parameters");
        }
        else
        {
            new DataServer(Integer.parseInt(args[0]), Integer.parseInt(args[1])).launch();
        }
    }
}
