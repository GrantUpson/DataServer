package upson.grant;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class QueryHandler implements Runnable
{
    private final PriorityBlockingQueue<Query> requests;
    private final ConcurrentHashMap<String, Query> results;
    private final String hostname;
    private final int port;

    public QueryHandler(String hostname, int port, PriorityBlockingQueue<Query> requests, ConcurrentHashMap<String, Query> results)
    {
        this.requests = requests;
        this.results = results;
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void run()
    {

    }
}
