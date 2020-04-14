package state;

import upson.grant.ClientRequest;
import java.io.BufferedWriter;

public class Authenticated implements State
{
    private ClientRequest request;
    private BufferedWriter writer;

    public Authenticated(ClientRequest request, BufferedWriter writer)
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
