import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;



public class Main {
    public static void main(String[] args) {
        String fileName = "simulation1.txt";

        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            int firstHostAddress = Integer.parseInt(br.readLine());
            FutureEventList fel = new LinkedEventList();
            Host firstHost = new SimpleHost(firstHostAddress, 0, 0);
            firstHost.setHostParameters(firstHostAddress, fel);

            System.out.println("First Host address: " + firstHostAddress);

            String line;
            while((line = br.readLine()) != null && !line.equals("-1")){
                String[] parts = line.split(" ");
                int neighborAddress = Integer.parseInt(parts[0]);
                int distance = Integer.parseInt(parts[1]);

                System.out.println("Adding neighbor: " + neighborAddress + " with distance " + distance);
                firstHost.addNeighbor(new SimpleHost(neighborAddress, 0,0), distance);
            }

            while((line = br.readLine()) != null ){
                String[] parts = line.split(" ");
                int sourceAddr = Integer.parseInt(parts[0]);
                int destAddr = Integer.parseInt(parts[1]);
                int interval = Integer.parseInt(parts[2]);
                int duration = Integer.parseInt(parts[3]);

                System.out.println("Source Address: " + sourceAddr);
                System.out.println("Destination Address: " + destAddr);
                System.out.println("Interval " + interval);
                System.out.println("Duration " + duration);

                if(sourceAddr == firstHostAddress){
                    ((SimpleHost) firstHost).sendPings(destAddr, interval, duration);
                }
            }

            Event event;
        while((event = fel.removeFirst()) != null){
            event.handle();
        }

        } catch(IOException e){
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}