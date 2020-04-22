package upson.grant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class WorkerHandler implements Runnable
{
    private final LinkedBlockingQueue<Tweet> tweetQueue;
    private final int port;
    private boolean connected;

    public WorkerHandler(int port, LinkedBlockingQueue<Tweet> tweetQueue)
    {
        this.port = port;
        this.tweetQueue = tweetQueue;
        this.connected = false;
    }

    @Override
    public void run()
    {
        try(ServerSocket listeningConnection = new ServerSocket(port))
        {
            while(true)
            {
                Socket connection = listeningConnection.accept();
                System.out.println("Worker instance connected from: " + connection.getInetAddress());
                ObjectOutputStream objectSender = new ObjectOutputStream(connection.getOutputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                connected = true;

                while (connected)
                {
                    if (!tweetQueue.isEmpty())
                    {
                        try
                        {
                            objectSender.writeObject(tweetQueue.take());
                            objectSender.flush();
                            objectSender.reset();

                            String response = reader.readLine();
                            if(response.equalsIgnoreCase("false"))
                            {
                                connected = false;
                                createNewWorkerInstance();
                            }
                        }
                        catch (InterruptedException iException)
                        {
                            System.out.println("Error: " + iException.getMessage());
                        }
                    }
                }
            }
        }
        catch(IOException ioException)
        {
            System.out.println("Error: " + ioException.getMessage());
        }
    }

    public void createNewWorkerInstance()
    {
        System.out.println("Creating a new worker instance..");
    }
}
