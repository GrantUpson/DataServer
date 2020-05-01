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
import java.util.concurrent.PriorityBlockingQueue;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class TweetHandler implements Runnable
{
    private final PriorityBlockingQueue<Message> requests;
    private final String hostname;
    private final int port;

    public TweetHandler(String hostname, int port, PriorityBlockingQueue<Message> requests)
    {
        this.hostname = hostname;
        this.port = port;
        this.requests = requests;
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
                    Timestamp dateCreated;
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy hh:mm");
                    Date parsedDate = formatter.parse(tweet[12]);
                    dateCreated = new java.sql.Timestamp(parsedDate.getTime());

                    Tweet newTweet = new Tweet(Long.parseLong(tweet[0]), tweet[1], tweet[5], tweet[10], dateCreated, 5, QueryHandler.storageWorkerID);
                    requests.add(newTweet);
                }
                catch(ParseException exception)
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
