package state;

import com.mysql.cj.log.Log;
import upson.grant.ClientRequest;

public class Connected implements State
{
    //private final String MENU = "[Register]: Register an account. /[Login]: Log in to your account.";
    private ClientRequest request;

    public Connected(ClientRequest request)
    {
        this.request = request;
        changeState(new Register(request));
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
}
