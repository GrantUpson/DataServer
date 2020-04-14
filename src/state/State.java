package state;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public interface State
{
    void changeState(State newState);
    String parseCommand(String command);
    void sendMenu();
}
