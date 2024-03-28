/*child class of Host, handles sending ping requests, receiving ping responses, and handling ping requests
 which are received. The message type is determined by looking at the value of the message string,
 and the appropriate action is taken based on the message type.*/
public class SimpleHost extends Host{
    private int destAddr;
    private int interval;
    private int duration;

    public SimpleHost(int destAddr, int interval, int duration) {
        this.destAddr = destAddr;
        this.interval = interval;
        this.duration = duration;
    }

    @Override
    protected void receive(Message msg) {
        // determine message type and handle accordingly
        String messageType = msg.getMessage();

        switch (messageType){
            case "ping request":
                // send ping response
                sendPingResponse(msg.getSrcAddress());
                break;
            case "ping response":
                // compute RTT
                computeRTT(msg);
                break;
            default:
                throw new EventException("Unknown message type received " + messageType);
        }
    }

    @Override
    protected void timerExpired(int eventId) {
        // handle timer expiration
    }

    @Override
    protected void timerCancelled(int eventId) {
        // handle timer cancellation
    }

    public int calculateDistance(){
        return Math.abs(destAddr - getHostAddress()); // calculate absolute difference as distance
    }

    public void sendPings(int destAddr, int interval, int duration){
        this.destAddr = destAddr;
        this.interval = interval;
        this.duration = duration;

        int currentTime = getCurrentTime(); // get current simulation time
        int startTime = currentTime + interval; // calculate start time for sending pings
        int endTime = currentTime + duration;  // calcualte end time for sending pings
        while(startTime <= endTime){
            // send ping request to destAddr
            System.out.println("[" + startTime + "ts] Host " + getHostAddress() + ": Sent ping to Host " + destAddr +
                    " with distance " + calculateDistance());
            // simulate receiving ping request
            int delay = calculateDistance() + 1;
            int receiveTime = startTime + delay;
            if(receiveTime <= endTime){
                System.out.println("[" + receiveTime + "ts] Host " + destAddr + ": Ping request from Host " +
                        getHostAddress());
            }
            // simulate sending ping response and calculate RTT
            int responseTime = receiveTime + delay;
            if(responseTime <= endTime){
                System.out.println("[" + responseTime + "ts] Host " + getHostAddress() + ": Ping response from Host "
                + destAddr + " (RTT = " + (responseTime - startTime) + "ts)");
            }
            startTime += interval; // increment time by interval
        }
        System.out.println("[" + endTime + "ts] Host " + getHostAddress() + ": Stopped sending pings");
    }

    private void sendPingResponse(int srcAddr){
        Message pingResponse = new Message("ping response", getHostAddress(), srcAddr);
        sendToNeighbor(pingResponse);
    }

    private void computeRTT(Message msg){
        int rtt = getCurrentTime() - msg.getInsertionTime();
        System.out.println("RTT for message " + msg.getId() + ": " + rtt + " simulation time units");
    }
}

