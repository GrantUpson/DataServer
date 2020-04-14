package state;

import upson.grant.ClientRequest;
import java.io.BufferedWriter;
import java.io.IOException;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class Connected implements State
{
    private final ClientRequest connection;
    private final BufferedWriter writer;

    public Connected(ClientRequest connection, BufferedWriter writer)
    {
        this.connection = connection;
        this.writer = writer;
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
            switch(result[0])
            {
                case "register":
                    response = "";
                    connection.changeState(new Register(connection, writer));
                    break;
                case "login":
                    response = "";
                    connection.changeState(new Login(connection, writer));
                    break;
                case "exit":
                    response = "";
                    connection.closeApplication();
                    break;
                default:
                    response = "Invalid option, try again.";
            }
        }

        return response;
    }

    @Override
    public void sendMenu()
    {
        final String MENU = "Register an account: <Register> /Login to your account: <Login> /Exit application: <Exit>";

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
