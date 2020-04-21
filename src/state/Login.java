package state;

import upson.grant.ClientRequest;
import upson.grant.Database;
import java.io.BufferedWriter;
import java.io.IOException;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class Login implements State
{
    private final ClientRequest connection;
    private final BufferedWriter writer;
    private Database accountDatabase;

    public Login(ClientRequest connection, BufferedWriter writer)
    {
        this.connection = connection;
        this.writer = writer;
        accountDatabase = new Database("grant", "Killthemall2");
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

        if(result.length == 2)
        {
            if(accountDatabase.login(result[0], result[1]))
            {
                response = "Login successful";
                connection.changeState(new Authenticated(connection, writer, connection.getResultsHashmap()));
            }
            else
            {
                response = "Incorrect username or password, try again";
            }
        }
        else if(result.length == 1)
        {
            if(result[0].equalsIgnoreCase("return"))
            {
                response = "";
                connection.changeState(new Connected(connection, writer));
            }
            else
            {
                response = "Invalid argument, try again";
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
        final String MENU = "Enter your username and password: <Username> <Password> or <Return> to return";

        try
        {
            writer.write(MENU + "\r\n");
            writer.flush();
        }
        catch(IOException ioException)
        {
            System.out.println("Error: " + ioException.getLocalizedMessage());
        }
    }
}
