package state;

import upson.grant.ClientRequest;
import java.io.BufferedWriter;
import java.io.IOException;

public class Connected implements State
{
    private final String MENU = "[Register]: Register an account. /[Login]: Log in to your account.";
    private ClientRequest request;
    private BufferedWriter writer;

    public Connected(ClientRequest request, BufferedWriter writer)
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
        return null;
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
