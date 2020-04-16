package upson.grant;

import java.io.Serializable;
import java.sql.Timestamp;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class Tweet implements Serializable
{
    private final long UID;
    private final String sentiment;
    private final String airline;
    private final String message;
    private final Timestamp created;

    public Tweet(long UID, String sentiment, String airline, String message, Timestamp created)
    {
        this.UID = UID;
        this.sentiment = sentiment;
        this.airline = airline;
        this.message = message;
        this.created = created;
    }

    public long getUID() { return UID; }
    public String getSentiment() { return sentiment; }
    public String getAirline() { return airline; }
    public String getMessage() { return message; }
    public Timestamp getCreated() { return created; }
}
