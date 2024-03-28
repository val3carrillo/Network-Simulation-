/* event class called Message that extends Event
* implement first the abstract methods from Event and later the additional methods from instructions
* also make sure it is compatible with the methods in Host!! */

public class Message extends Event{
public static final String PING_RESPONSE = "ping response";
public static final String PING_REQUEST = "ping request";
private String stringMessage;
private int srcAddress;
private int destAddress;
private Host nextHop;
private int distance;

public Message(String stringMessage, int srcAddress, int destAddress){
    this.stringMessage = stringMessage;
    this.srcAddress = srcAddress;
    this.destAddress = destAddress;
    this.nextHop = null; // next hop initially set to null
    this.distance = 0;
}

   public String getMessage(){
    return stringMessage;
   }
   public int getSrcAddress(){
    return srcAddress;
   }

   public int getDestAddress(){
    return destAddress;
   }

   public void setNextHop(Host nextHop, int distance){
       this.nextHop = nextHop;

    // set insertion time based on distance (1 distance = 1 simualtion time)
       setInsertionTime(getInsertionTime() + distance);
   }

    @Override
    public void setInsertionTime(int currentTime) {
       this.insertionTime = currentTime;
    }

    @Override
    public void cancel() {
         // messages cant be cancelled so u can leave alone
    }

    @Override
    public void handle() {
       // check if the current time is accessible from the Host object
        if(nextHop != null && nextHop instanceof Host){
            int currentTime = ((Host) nextHop).getCurrentTime();

            if(stringMessage.equals("ping request")){
                // process ping request message
                // like send a  ping response back to source
                Message pingResponse = new Message("ping response", destAddress, srcAddress);
                pingResponse.setNextHop(this.nextHop, distance);
                nextHop.sendToNeighbor(pingResponse);
            } else if(stringMessage.equals("ping response")){
                // process ping response message
                // compute RTT
                int rtt = currentTime - insertionTime;
                System.out.println("RTT for message from Host " + srcAddress + " to Host " + destAddress + ": " + rtt);
            }
        } else {
            System.out.println("Error: could not access current simulation time");
        }

    }
}





