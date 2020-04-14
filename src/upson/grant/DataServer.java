package upson.grant;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class DataServer
{
    private ConcurrentHashMap<Integer, Query> results;
    private PriorityBlockingQueue<Query> requests;

    public DataServer()
    {
        results = new ConcurrentHashMap<>(1000);
        requests = new PriorityBlockingQueue<>(300);
    }

    public void launch()
    {
        //new Thread(new TweetHandler("localhost", 6666)).start();
        Query query = new Query(1, Query.Type.MESSAGE, "HIi");
        query.updateStatus();
        System.out.println("Query of type: " + query.getType().toString() + " | Status: " + query.getStatus());

    }

    public static void main(String[] args)
    {
        if(args.length != 0)
        {
            System.out.println("Error: Invalid parameters. Usage: ");
        }
        else
        {
            new DataServer().launch();
        }
    }
}
