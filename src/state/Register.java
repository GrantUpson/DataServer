package state;

import upson.grant.ClientRequest;
import upson.grant.Database;
import java.io.BufferedWriter;
import java.io.IOException;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class Register implements State
{
    private final ClientRequest connection;
    private final BufferedWriter writer;
    private final Database accountDatabase;

    public Register(ClientRequest connection, BufferedWriter writer)
    {
        this.connection = connection;
        this.writer = writer;
        this.accountDatabase = new Database();
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
        String response;

        if(result.length != 1)
        {
            response = "Invalid number of arguments, try again";
        }
        else
        {
            response = "Username would go here";
            changeState(new Authenticated(connection, writer, connection.getResultsHashmap()));
        }

        return response;
    }

    @Override
    public void sendMenu()
    {
        final String MENU = "Enter a username to register with: ";

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
