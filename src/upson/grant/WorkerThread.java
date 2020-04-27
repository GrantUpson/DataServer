package upson.grant;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class WorkerThread implements Runnable
{
    private final Socket connection;
    private final PriorityBlockingQueue<Message> requests;
    private final ConcurrentHashMap<String, Query> results;

    public WorkerThread(Socket connection, PriorityBlockingQueue<Message> requests, ConcurrentHashMap<String, Query> results)
    {
        this.connection = connection;
        this.requests = requests;
        this.results = results;
    }

    @Override
    public void run()
    {
        try
        {
            ObjectOutputStream messageSender = new ObjectOutputStream(connection.getOutputStream());
            ObjectInputStream messageReceiver = new ObjectInputStream(connection.getInputStream());

            messageSender.writeObject(new Capacity(200));
            messageSender.flush();

            long elapsedTime;
            long currentTime;
            long beginningTime = System.currentTimeMillis();

            while(true)
            {
                currentTime = System.currentTimeMillis();
                elapsedTime = currentTime - beginningTime;

                if(elapsedTime > 10000)
                {
                    requests.add(new Heartbeat(10));
                    beginningTime = System.currentTimeMillis();
                }

                if(!requests.isEmpty())
                {
                    Message newMessage = requests.poll();

                    if(newMessage instanceof Query)
                    {
                        ((Query) newMessage).updateStatus();
                    }

                    messageSender.writeObject(newMessage);
                    messageSender.flush();

                    Message response = (Message)messageReceiver.readObject();

                    if(response instanceof Heartbeat) { System.out.println("Heartbeat: " + response.getResult()); }
                    if(response instanceof Tweet) { System.out.println("Tweet: " + response.getResult()); }
                    if(response instanceof Query)
                    {
                        ((Query) response).updateStatus();
                        results.put(Integer.toString(((Query) response).getID()), (Query)response);
                    }
                }
            }
        }
        catch(IOException | ClassNotFoundException exception)
        {
            System.out.println("Connection has been closed..");
        }
    }

    public void createNewWorkerInstance()
    {
        System.out.println("Creating a new worker instance..");
    }
}
