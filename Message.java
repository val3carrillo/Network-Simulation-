/* event class called Message that extends Event
* implement first the abstract methods from Event and later the additional methods from instructions
* also make sure it is compatible with the methods in Host!! */

public class Message extends Event{
    public static final String PING_REQUEST = "PING_REQUEST";
    public static final String PING_RESPONSE = "PING_RESPONSE";

    private String messageType;
    private int srcAddress;
    private int destAddress;
    private Host nextHop;
    private int distance = 0;

    public Message(String messageType, int srcAddress, int destAddress) {
        super();
        this.messageType = messageType;
        this.srcAddress = srcAddress;
        this.destAddress = destAddress;
    }


    @Override
    public void setInsertionTime(int currentTime) {
        this.insertionTime = currentTime;
        // Set the arrival time based on the current time and distance
        this.arrivalTime = currentTime + this.distance;
    }

    @Override
    public void cancel() {
        // Messages do not have cancellation behavior
    }

    @Override
    public void handle() {
        // Handle message at the destination host
        if (nextHop != null) {
            nextHop.receive(this);
        } else {
            // Log an error if the next hop is not set
            System.out.println("Error: No next hop set for the message.");
        }
    }

    public String getMessage() {
        return messageType;
    }

    public int getSrcAddress() {
        return srcAddress;
    }

    public int getDestAddress() {
        return destAddress;
    }

    public void setNextHop(Host nextHop, int distance) {
        this.nextHop = nextHop;
        this.distance = distance;
    }
}





