package upson.grant;

import java.io.Serializable;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class Capacity extends Message implements Serializable
{
    private final int maximumCapacity;

    public Capacity(int capacity, int workerID)
    {
        super(0, workerID);
        this.maximumCapacity = capacity;
    }

    public int getMaximumCapacity() { return maximumCapacity; }
}
