package state;

/*
  @author Grant Upson : 385831
*/

import upson.grant.ClientRequest;

public class Register implements State
{
    private ClientRequest request;

    public Register(ClientRequest request)
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

        return "Register State";
    }
}
