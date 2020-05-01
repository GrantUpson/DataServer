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
    private final int capacity;

    public static int storageWorkerID;
    private boolean initialWorker;

    public QueryHandler(int port, int capacity, PriorityBlockingQueue<Message> requests, ConcurrentHashMap<String, Query> results)
    {
        this.requests = requests;
        this.results = results;
        this.port = port;
        this.initialWorker = true;
        storageWorkerID = 1;
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

                if(initialWorker) { initialWorker = false; }
                else { storageWorkerID++; }
                new Thread(new WorkerThread(storageWorkerID, capacity,this, connection, requests, results)).start();
            }
        }
        catch(IOException ioException)
        {
            System.out.println("Error: " + ioException.getMessage());
        }
    }

    public int getStorageWorkerID() { return storageWorkerID; }
}
