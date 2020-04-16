package upson.grant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class TweetHandler implements Runnable
{
    private final LinkedBlockingQueue<Tweet> tweetQueue;
    private final String hostname;
    private final int port;

    public TweetHandler(String hostname, int port, LinkedBlockingQueue<Tweet> tweetQueue)
    {
        this.hostname = hostname;
        this.port = port;
        this.tweetQueue = tweetQueue;
    }

    @Override
    public void run()
    {
        try(Socket connection = new Socket(hostname, port); BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())))
        {
            String tweetData;

            while((tweetData = reader.readLine()) != null)
            {
                String[] tweet = tweetData.split("\t");

                try
                {
                    Timestamp created;
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy hh:mm");
                    Date parsedDate = formatter.parse(tweet[12]);
                    created = new java.sql.Timestamp(parsedDate.getTime());

                    Tweet newTweet = new Tweet(Long.parseLong(tweet[0]), tweet[1], tweet[5], tweet[10], created);
                    tweetQueue.put(newTweet);
                }
                catch(ParseException | InterruptedException exception)
                {
                    System.out.println("Error: " + exception);
                }
            }
        }
        catch(IOException ioException)
        {
            System.out.println("Error: " + ioException.getMessage());
        }
    }
}
