package upson.grant;

import java.io.Serializable;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class Message implements Serializable
{
    private final int priority;
    private final int workerID;
    private String result;

    public Message(int priority, int workerID)
    {
        this.priority = priority;
        this.workerID = workerID;
    }

    public int getPriority() { return priority; }
    public String getResult() { return result; }
    public int getWorkerID() { return workerID; }

    public void setResult(String result) { this.result = result; }
}
