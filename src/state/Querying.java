package state;

import upson.grant.ClientRequest;
import upson.grant.Query;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class Querying implements State
{
    private final PriorityBlockingQueue<Query> requests;
    private final ConcurrentHashMap<String, Query> results;
    private final ClientRequest connection;
    private final BufferedWriter writer;

    public Querying(ClientRequest connection, BufferedWriter writer, PriorityBlockingQueue<Query> requests, ConcurrentHashMap<String, Query> results)
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

        if(result.length == 2)
        {
            switch(result[0])
            {
                case "1":
                    response = "By ID!";
                    break;
                case "2":
                    response = "Contains word!";
                    break;
                case "3":
                    response = "Number of tweets from airline!";
                    break;
                case "4":
                    response = "Most frequent character in tweet!";
                    break;
                case "5":
                    if(result[1].equalsIgnoreCase("return"))
                    {
                        connection.changeState(new Authenticated(connection, writer, results));
                    }
                    else
                    {
                        response = "Invalid option, try again";
                    }
                    break;
                default:
                    response = "Invalid option, try again";
                    break;
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
        final String MENU = "1) Search for a tweet by ID: <1> <ID> /2) Search for a tweet containing a word: <2> <Word> /" +
                "3) Number of tweets from an airline: <3> <Airline> /4) Most frequent character in a tweet: <4> <TweetID> /" +
                "5) Return to Options: <5> <Return>";

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
}