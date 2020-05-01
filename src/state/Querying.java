package state;

import upson.grant.ClientRequest;
import upson.grant.Message;
import upson.grant.Query;
import upson.grant.QueryHandler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class Querying implements State
{
    private final PriorityBlockingQueue<Message> requests;
    private final ConcurrentHashMap<String, Query> results;
    private final ClientRequest connection;
    private final BufferedWriter writer;

    public Querying(ClientRequest connection, BufferedWriter writer, PriorityBlockingQueue<Message> requests, ConcurrentHashMap<String, Query> results)
    {
        this.connection = connection;
        this.writer = writer;
        this.requests = requests;
        this.results = results;
    }

    @Override
    public void changeState(State newState)
    {
        connection.changeState(newState);
    }

    @Override
    public String parseCommand(String command)
    {
        String[] result = command.split(" ");
        String response = "";

        if(result.length == 3)
        {
            int id = generateQueryID();
            response = "Your query has been submitted successfully. Query ID: " + id;

            Query query = null;

            switch(result[0])
            {
                case "1":
                    for(int i = 1; i <= QueryHandler.storageWorkerID; i++)
                    {
                        requests.add(query = new Query(id, i, Query.Type.MESSAGE, result[1], Integer.parseInt(result[2])));
                    }
                    results.put(Integer.toString(id), query);
                    break;
                case "2":
                    for(int i = 1; i <= QueryHandler.storageWorkerID; i++)
                    {
                        requests.add(query = new Query(id, i, Query.Type.CONTAINS_WORD, result[1], Integer.parseInt(result[2])));
                    }
                    results.put(Integer.toString(id), query);
                    break;
                case "3":
                    for(int i = 1; i <= QueryHandler.storageWorkerID; i++)
                    {
                        requests.add(query = new Query(id, i, Query.Type.FROM_AIRLINE, result[1], Integer.parseInt(result[2])));
                    }
                    results.put(Integer.toString(id), query);
                    break;
                case "4":
                    for(int i = 1; i <= QueryHandler.storageWorkerID; i++)
                    {
                        requests.add(query = new Query(id, i, Query.Type.MOST_FREQUENT_CHARACTER, result[1], Integer.parseInt(result[2])));
                    }
                    results.put(Integer.toString(id), query);
                    break;
                default:
                    response = "Invalid option, try again";
                    break;
            }
        }
        else if(result.length == 2)
        {
            if(result[0].equalsIgnoreCase("5"))
            {
                if(cancelQuery(Integer.parseInt(result[1])))
                {
                    response = "Query has been cancelled successfully";
                }
                else
                {
                    response = "Cannot cancel query, either it has been completed or the ID is wrong.";
                }
            }
            else
            {
                response = "Invalid option, try again";
            }
        }
        else if(result.length == 1)
        {
            if(result[0].equalsIgnoreCase("6"))
            {
                connection.changeState(new Authenticated(connection, writer, results));
            }
            else
            {
                response = "Invalid option, try again";
            }
        }
        else
        {
            response = "Invalid number of arguments, try again";
        }

        return response;
    }

    @Override
    public void sendMenu()
    {
        final String MENU = "1) Search for a tweet by ID: <1> <ID> <Priority>/2) Search for a tweet containing a word: <2> <Word> <Priority>/" +
                "3) Number of tweets from an airline: <3> <Airline> <Priority>/4) Most frequent character in a tweet: <4> <TweetID> <Priority>/" +
                "5) Cancel query: <5> <ID< /6) Return to Options: <6>";

        try
        {
            writer.write(MENU + "\r\n");
            writer.flush();
        }
        catch(IOException ioException)
        {
            System.out.println("Error: " + ioException.getMessage());
        }
    }

    public boolean cancelQuery(int ID)
    {
        boolean successful = false;

        for(Message message : requests)
        {
            if(message instanceof Query)
            {
                Query query = (Query)message;

                if(query.getID() == ID)
                {
                    requests.remove(message);
                    successful = true;
                }
            }
        }

        return successful;
    }

    public int generateQueryID()
    {
        final int ID_LENGTH = 8;

        String partialID = String.format("%040d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16));
        return Integer.parseInt(partialID.substring(0, ID_LENGTH));
    }
}