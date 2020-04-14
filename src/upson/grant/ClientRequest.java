package upson.grant;

import state.*;
import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class ClientRequest implements Runnable
{
    private final Socket clientConnection;

    private final ConcurrentHashMap<String, Query> results;
    private final PriorityBlockingQueue<Query> requests;
    private State currentState;
    private boolean running;

    public ClientRequest(Socket connection, PriorityBlockingQueue<Query> requests, ConcurrentHashMap<String, Query> results)
    {
        this.clientConnection = connection;
        this.requests = requests;
        this.results = results;
        this.running = true;
    }

    @Override
    public void run()
    {
        try(BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(clientConnection.getOutputStream()));
            BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientConnection.getInputStream())))
        {
            currentState = new Connected(this, clientWriter);
            String command;

            while(running)
            {
                currentState.sendMenu();
                command = clientReader.readLine().toLowerCase();
                String result = currentState.parseCommand(command);
                clientWriter.write(result + "\r\n");
                clientWriter.flush();
            }

            System.out.println("Client disconnected from " + clientConnection.getInetAddress() + " on port " + clientConnection.getLocalPort() +
                    " at " + new Timestamp(System.currentTimeMillis()));
        }
        catch(IOException | NullPointerException exception)
        {
            System.out.println("Error: " + exception.getMessage());
        }
    }

    public void changeState(State newState)
    {
        this.currentState = newState;
    }

    public ConcurrentHashMap<String, Query> getResultsHashmap()
    {
        return results;
    }

    public PriorityBlockingQueue<Query> getRequestsPriorityQueue()
    {
        return requests;
    }

    public void closeApplication()
    {
        running = false;
    }
}
