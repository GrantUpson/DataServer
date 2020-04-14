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
    private ClientRequest request;
    private BufferedWriter writer;
    private final String Menu1 = "[username]: Enter your username. / [password]: Enter your password.";
    private final String LOGIN_SUCCESSFULL = "Login Successfull";
    private final String LOGIN_FAILED = "Login Failed";


    public Login(ClientRequest request, BufferedWriter writer)
    {
        this.request = request;
        this.writer = writer;
    }

    @Override
    public void changeState(State newState)
    {
        request.changeState(newState);
    }

    @Override
    public String parseCommand(String command)
    {
        String authenticationResult = "";
        String[] result = command.split(" ");
        Database database = new Database();

        int loginResult = database.databaseConnection(result[0], result[1]);

        if(loginResult == 1)
            {
                authenticationResult = LOGIN_SUCCESSFULL;
            }

        else
            {
                authenticationResult = LOGIN_FAILED;
            }
        return authenticationResult;
    }

    @Override
    public void sendMenu()
    {
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
