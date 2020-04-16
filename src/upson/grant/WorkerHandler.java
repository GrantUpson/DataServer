package upson.grant;

import java.io.BufferedWriter;
import java.io.IOException;
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

    public WorkerHandler(int port, LinkedBlockingQueue<Tweet> tweetQueue)
    {
        this.port = port;
        this.tweetQueue = tweetQueue;
    }

    @Override
    public void run()
    {
        try(ServerSocket listeningConnection = new ServerSocket(port))
        {
            Socket connection = listeningConnection.accept();
            ObjectOutputStream objectSender = new ObjectOutputStream(connection.getOutputStream());

            //Temp one connection only hack
            while(true)
            {
                if(!tweetQueue.isEmpty())
                {
                    try
                    {
                        objectSender.writeObject(tweetQueue.take());
                    }
                    catch(InterruptedException iException)
                    {
                        System.out.println("Error: " + iException.getMessage());
                    }
                }
            }
        }
        catch(IOException ioException)
        {
            System.out.println("Error: " + ioException.getMessage());
        }
    }
}
