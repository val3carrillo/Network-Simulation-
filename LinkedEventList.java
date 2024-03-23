/* Implements FutureEventList */
public class LinkedEventList implements FutureEventList{
    private Node head;
    private int size;
    private int simulationTime;

    public LinkedEventList() {
        this.head = null;
        this.size = 0;
        this.simulationTime = 0;
    }

    @Override
    public Event removeFirst() {
        if (head == null) {
            return null; // No event to remove
        }
        Event removedEvent = head.event;
        head = head.next; // Move head to next node
        if (head != null) {
            head.previous = null; // Previous head is gone, so new head's previous is null
        }
        size--;
        simulationTime = removedEvent.getArrivalTime(); // Update simulation time to removed event's arrival time
        return removedEvent;
    }

    @Override
    public boolean remove(Event e) {
        Node current = head;
        while (current != null) {
            if (current.event.getId() == e.getId()) { // Compare using event ID
                if (current.previous != null) {
                    current.previous.next = current.next;
                } else {
                    head = current.next; // Removing the head node
                }
                if (current.next != null) {
                    current.next.previous = current.previous;
                }
                size--;
                return true; // Event found and removed
            }
            current = current.next;
        }
        return false; // Event not found
    }

    @Override
    public void insert(Event e) {
        // assuming insertionTime and arrivalTime are already set correctly
        // when the event is scheduled or created.
       // if (e.getArrivalTime() >= 0) {
            Node newNode = new Node(e);
            if (head == null || e.getArrivalTime() < head.event.getArrivalTime()) {
                newNode.next = head;
                if (head != null) {
                    head.previous = newNode;
                }
                head = newNode;
            } else {
                Node current = head;
                while (current.next != null && current.next.event.getArrivalTime() <= e.getArrivalTime()) {
                    current = current.next;
                }
                newNode.next = current.next;
                if (current.next != null) {
                    current.next.previous = newNode;
                }
                current.next = newNode;
                newNode.previous = current;
           }
            size++;
      //  }

    }



    @Override
    public int size() {
        return size; // Current number of events in the list
    }

    @Override
    public int capacity() {
        return size; // Since it's a linked list, capacity dynamically matches the size
    }

    @Override
    public int getSimulationTime() {
        return simulationTime; // Current simulation time based on the last removed event
    }

    // Inner class for doubly linked list node
    private static class Node {
        Event event;
        Node previous;
        Node next;

        Node(Event event) {
            this.event = event;
            this.previous = null;
            this.next = null;
        }
    }
}



