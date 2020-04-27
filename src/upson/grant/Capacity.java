package upson.grant;

import java.io.Serializable;

public class Capacity extends Message implements Serializable
{
    private final int maximumCapacity;

    public Capacity(int capacity)
    {
        super(0);
        this.maximumCapacity = capacity;
    }

    public int getMaximumCapacity() { return maximumCapacity; }
}
