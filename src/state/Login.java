package state;

import upson.grant.ClientRequest;
import java.io.BufferedWriter;

/*
  @author Grant Upson : 385831
*/

public class Login implements State
{
    private ClientRequest request;
    private BufferedWriter writer;

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

        return "";
    }

    @Override
    public void sendMenu()
    {

    }
}
