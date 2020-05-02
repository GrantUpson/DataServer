package upson.grant;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.PriorityBlockingQueue;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class QueryHandler implements Runnable
{
    private final PriorityBlockingQueue<Message> requests;
    private final ConcurrentHashMap<String, Query> results;
    public static CopyOnWriteArrayList<Integer> workerIDs = new CopyOnWriteArrayList<>();
    private final int port;
    private final int capacity;

    public QueryHandler(int port, int capacity, PriorityBlockingQueue<Message> requests, ConcurrentHashMap<String, Query> results)
    {
        this.requests = requests;
        this.results = results;
        this.port = port;
        this.capacity = capacity;
    }

    @Override
    public void run()
    {
        try(ServerSocket listeningConnection = new ServerSocket(port))
        {
            while(true)
            {
                Socket connection = listeningConnection.accept();
                System.out.println("Worker connection from: " + connection.getInetAddress() + " accepted.");

                new Thread(new WorkerThread(capacity,this, connection, requests, results, workerIDs)).start();
            }
        }
        catch(IOException ioException)
        {
            System.out.println("Error: " + ioException.getMessage());
        }
    }
}
