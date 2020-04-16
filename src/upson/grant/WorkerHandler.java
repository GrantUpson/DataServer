package upson.grant;

import java.util.concurrent.LinkedBlockingQueue;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class WorkerHandler implements Runnable
{
    private final LinkedBlockingQueue<Tweet> tweetQueue;
    private final String hostname;
    private final int port;

    public WorkerHandler(String hostname, int port, LinkedBlockingQueue<Tweet> tweetQueue)
    {
        this.hostname = hostname;
        this.port = port;
        this.tweetQueue = tweetQueue;
    }

    @Override
    public void run()
    {

    }
}
