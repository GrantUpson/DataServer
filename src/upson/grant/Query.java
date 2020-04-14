package upson.grant;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class Query
{
    public enum Type { MESSAGE, CONTAINS_WORD, FROM_AIRLINE, MOST_FREQUENT_CHARACTER; }

    private enum Status
    {
        SUBMITTED { public Status updateStatus() { return PROCESSING; }},
        PROCESSING { public Status updateStatus() { return COMPLETE; }},
        COMPLETE { public Status updateStatus() { return COMPLETE; }};

        public abstract Status updateStatus();
    }

    private final int id;
    private final Type type;
    private final String request;
    private Status currentStatus;
    private String result;

    public Query(int id, Type type, String request)
    {
        this.id = id;
        this.type = type;
        this.request = request;
        this.currentStatus = Status.SUBMITTED;
        this.result = null;
    }

    public int getID() { return id; }
    public Type getType() { return type; }
    public String getRequest() { return request; }
    public String getStatus() { return currentStatus.name(); }
    public String getResult() { return result; }

    public void updateStatus() { currentStatus = currentStatus.updateStatus(); }
    public void setResult(String result) { this.result = result; }
}
