package upson.grant;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

import java.io.Serializable;

public class Message implements Serializable
{
    private final int priority;
    private String result;

    public Message(int priority)
    {
        this.priority = priority;
    }

    public int getPriority() { return priority; }
    public String getResult() { return result; }

    public void setResult(String result) { this.result = result; }
}
