package upson.grant;

import java.io.Serializable;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class IDConfirmation extends Message implements Serializable
{
    public IDConfirmation(int priority, int workerID, String id)
    {
        super(priority, workerID);
        setResult(id);
    }
}
