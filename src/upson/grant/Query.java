package upson.grant;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

import java.io.Serializable;

public class Query extends Message implements Serializable, Comparable
{
    public enum Type { MESSAGE, CONTAINS_WORD, FROM_AIRLINE, MOST_FREQUENT_CHARACTER; }

    private enum Status
    {
        SUBMITTED { public Status updateStatus() { return PROCESSING; }},
        PROCESSING { public Status updateStatus() { return COMPLETE; }},
        COMPLETE { public Status updateStatus() { return COMPLETE; }};

        public abstract Status updateStatus();
    }

    private final Type type;
    private final String request;
    private Status currentStatus;
    private double bill;
    private long timeTakenToCompute;
    private final int id;

    public Query(int id, int workerID, Type type, String request, int priority)
    {
        super(priority, workerID);

        this.id = id;
        this.type = type;
        this.request = request;
        this.currentStatus = Status.SUBMITTED;
        this.bill = 0.00;
        this.timeTakenToCompute = 0;
    }

    public int getID() { return id; }
    public int getPriority() { return super.getPriority(); }
    public Type getType() { return type; }
    public String getRequest() { return request; }
    public String getStatus() { return currentStatus.name(); }
    public String getResult() { return super.getResult(); }
    public double getBill() { return bill; }
    public long getTimeTakenToCompute() { return timeTakenToCompute; }

    public void updateStatus() { currentStatus = currentStatus.updateStatus(); }
    public void setResult(String result) { super.setResult(result); }
    public void setBill(double amount) { bill = amount; }
    public void setTimeTakenToCompute(long time) { timeTakenToCompute = time; }

    @Override
    public int compareTo(Object o)
    {
        Message message = (Message)o;
        return Integer.compare(super.getPriority(), message.getPriority());
    }
}
