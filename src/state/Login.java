package state;

import com.mysql.cj.xdevapi.Client;
import upson.grant.ClientRequest;

/*
  @author Grant Upson : 385831
*/

public class Login implements State
{
    ClientRequest request;

    public Login(ClientRequest request)
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
