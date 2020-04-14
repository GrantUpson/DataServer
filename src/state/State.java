package state;

/*
  @author Grant Upson : 385831
*/

public interface State
{
    void changeState(State newState);
    String parseCommand(String command);
    void sendMenu();
}
