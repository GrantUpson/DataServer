package upson.grant;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

import org.jetbrains.annotations.NotNull;

public class Query implements Comparable
{
    public enum Type { MESSAGE, CONTAINS_WORD, FROM_AIRLINE, MOST_FREQUENT_CHARACTER; }

    public enum Status
    {
        SUBMITTED { public Status updateStatus() { return PROCESSING; }},
        PROCESSING { public Status updateStatus() { return COMPLETE; }},
        COMPLETE { public Status updateStatus() { return COMPLETE; }};

        public abstract Status updateStatus();
    }

    private final int id;
    private final int priority;
    private final Type type;
    private final String request;
    private Status currentStatus;
    private String result;

    public Query(int id, Type type, String request, int priority)
    {
        this.id = id;
        this.priority = priority;
        this.type = type;
        this.request = request;
        this.currentStatus = Status.SUBMITTED;
        this.result = null;
    }

    public int getID() { return id; }
    public int getPriority() { return priority; }
    public Type getType() { return type; }
    public String getRequest() { return request; }
    public String getStatus() { return currentStatus.name(); }
    public String getResult() { return result; }

    public void updateStatus() { currentStatus = currentStatus.updateStatus(); }
    public void setResult(String result) { this.result = result; }

    @Override
    public int compareTo(@NotNull Object o)
    {
        Query query = (Query)o;
        return Integer.compare(priority, query.getPriority());
    }
}
