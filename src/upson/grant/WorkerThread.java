package upson.grant;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
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
    private int id;
    private final int capacity;
    private final CopyOnWriteArrayList<Integer> workerIDs;

    public WorkerThread(int capacity, QueryHandler handler, Socket connection, PriorityBlockingQueue<Message> requests, ConcurrentHashMap<String, Query> results, CopyOnWriteArrayList<Integer> workerIDs)
    {
        this.handler = handler;
        this.connection = connection;
        this.requests = requests;
        this.results = results;
        this.capacity = capacity;
        this.workerIDs = workerIDs;
    }

    @Override
    public void run()
    {
        try
        {
            ObjectOutputStream messageSender = new ObjectOutputStream(connection.getOutputStream());
            ObjectInputStream messageReceiver = new ObjectInputStream(connection.getInputStream());

            Message idMessage = (Message)messageReceiver.readObject();
            this.id = Integer.parseInt(idMessage.getResult());
            workerIDs.add(id);


            messageSender.writeObject(new Capacity(capacity, id));
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
                    requests.add(new Heartbeat(1, id));
                    beginningTime = System.currentTimeMillis();
                }

                Message newMessage = retrieveMessage();

                if(newMessage != null)
                {
                    if(newMessage instanceof Query)
                    {
                        ((Query) newMessage).updateStatus();
                        newMessage.setResult(String.valueOf(0));
                        results.put(Integer.toString(((Query) newMessage).getID()), (Query)newMessage);
                    }

                    messageSender.writeObject(newMessage);
                    messageSender.flush();

                    Message response = (Message)messageReceiver.readObject();

                    if(response instanceof Heartbeat) { System.out.println("Heartbeat Response: " + response.getResult()); }

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

                        aggregateResults((Query)response);
                        System.out.println("Query of ID: " + ((Query) response).getID() + " computed successfully. Time taken: " + ((Query) response).getTimeTakenToCompute() + " milliseconds");
                    }
                }
            }
        }
        catch(IOException | ClassNotFoundException exception)
        {
            System.out.println("Connection has been closed.. from worker of ID: " + id);

            for(int i = 0; i < workerIDs.size(); i++)
            {
                if(workerIDs.get(i) == id)
                {
                    workerIDs.remove(i);
                }
            }
        }
    }

    public synchronized Message retrieveMessage()
    {
        Message message = null;

            if(!requests.isEmpty())
            {
                try
                {
                    message = requests.peek();

                    if(message != null)
                    {
                        message = requests.take();
                    }
                }
                catch(InterruptedException interruptedException)
                {
                    System.out.println("Error: " + interruptedException.getMessage());
                }
            }

        return message;
    }

    public void aggregateResults(Query query)
    {
        Query temp = results.get(Integer.toString(query.getID()));

        switch(query.getType())
        {
            case MESSAGE:
            case MOST_FREQUENT_CHARACTER:
                results.put(Integer.toString(query.getID()), query);
                break;
            case FROM_AIRLINE:
            case CONTAINS_WORD:
                int result = Integer.parseInt(temp.getResult());
                result += Integer.parseInt(query.getResult());
                query.setResult(Integer.toString(result));
                results.put(Integer.toString(query.getID()), query);
                break;
        }

        temp.updateStatus();
    }

    public void createNewWorkerInstance()
    {
        System.out.println("Creating a new worker instance of capacity: " + capacity);
    }
}
