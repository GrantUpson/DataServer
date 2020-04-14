package state;

/*
  @author Grant Upson : 385831
*/

import upson.grant.ClientRequest;

import java.io.BufferedWriter;

public class Register implements State
{
    private ClientRequest request;
    private BufferedWriter writer;

    public Register(ClientRequest request, BufferedWriter writer)
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

        return "Register State";
    }

    @Override
    public void sendMenu()
    {

    }
}
