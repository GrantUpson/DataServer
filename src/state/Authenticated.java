package state;

import com.mysql.cj.xdevapi.Client;
import upson.grant.ClientRequest;

public class Authenticated implements State
{
    private ClientRequest request;

    public Authenticated(ClientRequest request)
    {
        this.request = request;
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
}
