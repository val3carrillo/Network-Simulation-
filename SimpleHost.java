/*child class of Host, handles sending ping requests, receiving ping responses, and handling ping requests
 which are received. The message type is determined by looking at the value of the message string,
 and the appropriate action is taken based on the message type.*/

import java.util.HashMap;
import java.util.Map;

public class SimpleHost extends Host{
    private int destAddr;
    private int interval;
    private int duration;
    private int currentPingIndex;
    private int[] pingTimes;
    private int rtt;

    public SimpleHost() {
        super();
        this.currentPingIndex = 0;
    }

    public void sendPings(int destAddr, int interval, int duration) {
        this.destAddr = destAddr;
        this.interval = interval;
        this.duration = duration;
        this.pingTimes = new int[duration / interval];
        for (int i = 0; i < pingTimes.length; i++) {
            pingTimes[i] = (i + 1) * interval;
            schedulePing(pingTimes[i]);
        }
        scheduleStop(duration);
    }

    private void schedulePing(int pingTime) {
        int eventId = newTimer(pingTime);
        Message pingRequest = new Message(Message.PING_REQUEST, getHostAddress(), this.destAddr);
        pingRequest.setNextHop(this, 1);
        pingRequest.setInsertionTime(pingTime); // Correctly set insertion time
        sendToNeighbor(pingRequest);
        System.out.println("[" + pingTime + "ts] Host " + getHostAddress() + ": Sent ping to host " + this.destAddr);
    }

    private void scheduleStop(int duration) {
        int eventId = newTimer(duration);
        System.out.println("[" + duration + "ts] Host " + getHostAddress() + ": Stopped sending pings");
    }

    @Override
    protected void receive(Message msg) {
        String messageType = msg.getMessage();

        if (messageType.equals(Message.PING_REQUEST)) {
            // If receiving a ping request, reply with a ping response
            int currentTime = getCurrentTime(); // Get current simulation time
            Message response = new Message(Message.PING_RESPONSE, getHostAddress(), msg.getSrcAddress());
            response.setNextHop(this, 1); // Set next hop
            response.setInsertionTime(currentTime); // Set insertion time for the response
            sendToNeighbor(response);
            System.out.println("[" + currentTime + "ts] Host " + getHostAddress() + ": Ping request from host "
                    + msg.getSrcAddress());
        } else if (messageType.equals(Message.PING_RESPONSE)) {
            // If receiving a ping response, compute the RTT (Round Trip Time)
            this.rtt = getCurrentTime() - msg.getArrivalTime();
            int currentTime = getCurrentTime(); // Get current simulation time
            System.out.println("[" + currentTime + "ts] Host " + getHostAddress() + ": Ping response from host "
                    + msg.getSrcAddress() + " (RTT = " + this.rtt + "ts)");
            // Stop sending ping requests if the RTT is greater than or equal to the interval
            if (this.rtt >= this.interval) {
                cancelTimer(0);
                System.out.println("[" + currentTime + "ts] Host " + getHostAddress() + ": Stopped sending pings");
            }
        }
    }

    @Override
    protected void timerExpired(int eventId) {
        if (eventId != 0) {
            int currentTime = getCurrentTime(); // Get current simulation time
            System.out.println("[" + currentTime + "ts] Host " + getHostAddress() + ": Sent ping to host " + this.destAddr);
        } else {
            int currentTime = getCurrentTime(); // Get current simulation time
            System.out.println("[" + currentTime + "ts] Host " + getHostAddress() + ": Stopped sending pings");
        }
    }

    @Override
    protected void timerCancelled(int eventId) {
        int currentTime = getCurrentTime(); // Get current simulation time
        System.out.println("[" + currentTime + "ts] Host " + getHostAddress() + ": Stopped sending pings");
    }
}



