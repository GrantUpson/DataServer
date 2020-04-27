package upson.grant;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class Heartbeat extends Message implements Serializable, Comparable
{
    public Heartbeat(int priority)
    {
        super(priority);
    }

    @Override
    public int compareTo(Object o)
    {
        Message message = (Message)o;
        return Integer.compare(super.getPriority(), message.getPriority());
    }
}
