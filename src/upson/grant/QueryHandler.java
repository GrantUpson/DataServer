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
    private final int port;

    public QueryHandler(int port, PriorityBlockingQueue<Message> requests, ConcurrentHashMap<String, Query> results)
    {
        this.requests = requests;
        this.results = results;
        this.port = port;
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
                new Thread(new WorkerThread(connection, requests, results)).start();
            }
        }
        catch(IOException ioException)
        {
            System.out.println("Error: " + ioException.getMessage());
        }
    }
}
