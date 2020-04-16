package upson.grant;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.sql.Timestamp;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class DataServer
{
    private final ConcurrentHashMap<String, Query> results;
    private final PriorityBlockingQueue<Query> requests;
    private final LinkedBlockingQueue<Tweet> tweetQueue;

    private final int port;

    public DataServer(int port)
    {
        results = new ConcurrentHashMap<>(1000);
        requests = new PriorityBlockingQueue<>(300);
        tweetQueue = new LinkedBlockingQueue<>(300);
        this.port = port;
    }

    public void launch()
    {
        new Thread(new WorkerHandler(7777, tweetQueue)).start();
        new Thread(new QueryHandler(8888, requests, results)).start();
        new Thread(new TweetHandler("localhost", 9999, tweetQueue)).start();

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
        if(args.length != 1)
        {
            System.out.println("Error: Invalid number of parameters");
        }
        else
        {
            new DataServer(Integer.parseInt(args[0])).launch();
        }
    }
}
