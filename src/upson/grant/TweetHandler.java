package upson.grant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;

public class TweetHandler implements Runnable
{
    private String hostname;
    private int port;

    public TweetHandler(String hostname, int port)
    {
        this.hostname = hostname;
        this.port = port;
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

                System.out.println("ID: " + tweet[0] + " Sentiment: " + tweet[1] + " Airline: " + tweet[5] + " Message: " + tweet[10] +
                        " Date Created: " + tweet[12]);
            }
        }
        catch(IOException ioException)
        {
            System.out.println("Error: " + ioException.getMessage());
        }
    }
}
