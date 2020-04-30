package upson.grant;

import java.io.Serializable;
import java.sql.Timestamp;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class Tweet extends Message implements Serializable, Comparable
{
    private final long UID;
    private final String sentiment;
    private final String airline;
    private final String message;
    private final Timestamp dateCreated;

    public Tweet(long UID, String sentiment, String airline, String message, Timestamp created, int priority, int workerID)
    {
        super(priority, workerID);

        this.UID = UID;
        this.sentiment = sentiment;
        this.airline = airline;
        this.message = message;
        this.dateCreated = created;
    }

    public long getUID() { return UID; }
    public String getSentiment() { return sentiment; }
    public String getAirline() { return airline; }
    public String getMessage() { return message; }
    public Timestamp getDateCreated() { return dateCreated; }

    @Override
    public int compareTo(Object o)
    {
        Message message = (Message)o;
        return Integer.compare(super.getPriority(), message.getPriority());
    }
}
