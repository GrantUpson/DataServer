package upson.grant;

import java.io.Serializable;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class Heartbeat extends Message implements Serializable, Comparable
{
    public Heartbeat(int priority, int id)
    {
        super(priority, id);
    }

    @Override
    public int compareTo(Object o)
    {
        Message message = (Message)o;
        return Integer.compare(super.getPriority(), message.getPriority());
    }
}
