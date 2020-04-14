package upson.grant;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class DataServer
{
    private final ConcurrentHashMap<String, Query> results;
    private final PriorityBlockingQueue<Query> requests;

    private final int port;

    public DataServer(int port)
    {
        results = new ConcurrentHashMap<>(1000);
        requests = new PriorityBlockingQueue<>(300);
        this.port = port;
    }

    public void launch()
    {
        //new Thread(new TweetHandler("localhost", 6666)).start();

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
        if(args.length != 0)
        {
            System.out.println("Error: Invalid parameters. Usage: ");
        }
        else
        {
            new DataServer().launch();
        }
    }
}
