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
    private final QueryHandler handler;
    private final int id;

    public WorkerThread(int id, QueryHandler handler, Socket connection, PriorityBlockingQueue<Message> requests, ConcurrentHashMap<String, Query> results)
    {
        this.handler = handler;
        this.connection = connection;
        this.requests = requests;
        this.results = results;
        this.id = id;
    }

    @Override
    public void run()
    {
        try
        {
            ObjectOutputStream messageSender = new ObjectOutputStream(connection.getOutputStream());
            ObjectInputStream messageReceiver = new ObjectInputStream(connection.getInputStream());

            messageSender.writeObject(new Capacity(30, id));
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
                    requests.add(new Heartbeat(10, id));
                    beginningTime = System.currentTimeMillis();
                }

                if(!requests.isEmpty())
                {
                    Message newMessage = requests.take();
                    //TODO IF YOU PEEK YOU CAN GET A NULL POINTER EXCEPTION IF ANOTHER THREAD TRIES TO PEEK WHILE YOU SEND IT.
                    if(newMessage.getWorkerID() == id)
                    {
                        if(newMessage instanceof Query)
                        {
                            ((Query) newMessage).updateStatus();
                            results.put(Integer.toString(((Query) newMessage).getID()), (Query)newMessage);
                        }

                        messageSender.writeObject(newMessage);
                        messageSender.flush();

                        Message response = (Message)messageReceiver.readObject();

                        if(response instanceof Heartbeat) { System.out.println("Heartbeat: " + response.getResult()); }

                        if(response instanceof Tweet)
                        {
                            if(response.getResult().equalsIgnoreCase("Full"))
                            {
                                createNewWorkerInstance();
                            }
                        }

                        if(response instanceof Query)
                        {
                            ((Query) response).updateStatus();
                            System.out.println("Query of ID: " + ((Query) response).getID() + " computed successfully. Time taken: " + ((Query) response).getTimeTakenToCompute() + " milliseconds");
                            results.put(Integer.toString(((Query) response).getID()), (Query)response);
                        }
                    }
                }
            }
        }
        catch(IOException | ClassNotFoundException | InterruptedException exception)
        {
            System.out.println("Connection has been closed..");
        }
    }

    public void createNewWorkerInstance()
    {
        System.out.println("Creating a new worker instance..");
    }
}
