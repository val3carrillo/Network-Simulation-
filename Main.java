import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
        LinkedEventList fel = new LinkedEventList();
        Map<Integer, SimpleHost> hosts = new HashMap<>(); // To keep track of all hosts

        try {
            readSimulationFile("simulation1.txt", fel, hosts);
        } catch (IOException e) {
            System.err.println("Error reading simulation file: " + e.getMessage());
            return;
        }

        // Run the simulation
        runSimulation(fel);
    }

    private static void readSimulationFile(String filename, FutureEventList fel, Map<Integer, SimpleHost> hosts) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;

        // First line: central host address
        int firstHostAddress = Integer.parseInt(br.readLine());
        SimpleHost firstHost = new SimpleHost();
        firstHost.setHostParameters(firstHostAddress, fel);
        hosts.put(firstHostAddress, firstHost);

        // Neighboring hosts and distances
        while (!(line = br.readLine()).equals("-1")) {
            String[] parts = line.split("\\s+");
            int neighborAddress = Integer.parseInt(parts[0]);
            int distance = Integer.parseInt(parts[1]);

            // Create and configure neighboring host if it doesn't already exist
            SimpleHost neighbor = hosts.computeIfAbsent(neighborAddress, addr -> new SimpleHost());
            neighbor.setHostParameters(neighborAddress, fel);

            firstHost.addNeighbor(neighbor, distance);
            neighbor.addNeighbor(firstHost, distance);
        }

        // Ping requests configurations
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("\\s+");
            int srcAddress = Integer.parseInt(parts[0]);
            int destAddress = Integer.parseInt(parts[1]);
            int interval = Integer.parseInt(parts[2]);
            int duration = Integer.parseInt(parts[3]);

            SimpleHost srcHost = hosts.get(srcAddress);
            if (srcHost != null) {
                srcHost.sendPings(destAddress, interval, duration);
            } else {
                System.err.println("Sender host with address " + srcAddress + " not found.");
            }
        }
        br.close();
    }

    private static void runSimulation(FutureEventList fel) {
        while (fel.size() > 0) {
            Event event = fel.removeFirst();
            if (event != null) {
                event.handle();
            }
        }
    }

}

















