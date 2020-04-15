package state;

import upson.grant.ClientRequest;
import upson.grant.Database;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.UUID;

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
        this.accountDatabase = new Database("USER", "PASS");
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
            if(!command.equalsIgnoreCase("return"))
            {
                if(accountDatabase.usernameExists(command))
                {
                    response = "That username is already in use, try another";
                }
                else
                {
                    String password = generatePasscode();

                    if(accountDatabase.register(command, password))
                    {
                        response = "Registration successful, your password is: " + password;
                        changeState(new Connected(connection, writer));
                    }
                    else
                    {
                        response = "Registration unsuccessful, try again";
                    }
                }
            }
            else
            {
                response = "";
                connection.changeState(new Connected(connection, writer));
            }
        }

        return response;
    }

    @Override
    public void sendMenu()
    {
        final String MENU = "Enter a username to register with: <Username> or <Return> to return";

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

    public String generatePasscode()
    {
        final int PASSCODE_LENGTH = 8;

        UUID potentialCharacters = UUID.randomUUID();
        return potentialCharacters.toString().replace("-", "").substring(0, PASSCODE_LENGTH);
    }
}
