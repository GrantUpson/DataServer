package state;

import upson.grant.ClientRequest;
import upson.grant.Query;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class Authenticated implements State
{
    private ConcurrentHashMap<String, Query> results;
    private final ClientRequest connection;
    private final BufferedWriter writer;

    public Authenticated(ClientRequest connection, BufferedWriter writer, ConcurrentHashMap<String, Query> results)
    {
        this.connection = connection;
        this.writer = writer;
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

        if(result.length == 1)
        {
            switch(result[0])
            {
                case "query":
                    connection.changeState(new Querying(connection, writer, connection.getRequestsPriorityQueue(), results));
                    break;
                case "logout":
                    connection.changeState(new Connected(connection, writer));
                    break;
                default:
                    response = "Invalid option, try again";
                    break;
            }
        }
        else if(result.length == 2)
        {
            if(result[0].equalsIgnoreCase("result"))
            {
                if(results.containsKey(result[1]))
                {
                    Query query = results.get(result[1]);

                    switch(query.getStatus())
                    {
                        case "SUBMITTED":
                            response = "The query has been submitted, but has not begun processing, try again later";
                            break;
                        case "PROCESSING":
                            response = "The query is currently processing, try again later";
                            break;
                        case "COMPLETE":
                            response = "The result of the query is: " + query.getResult();
                            break;
                    }
                }
                else
                {
                    response = "Incorrect query ID, try again";
                }
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
        final String MENU = "Perform a query: <Query> /Enquire about a query: <Result> <QueryID> /Logout: <Logout>";

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
