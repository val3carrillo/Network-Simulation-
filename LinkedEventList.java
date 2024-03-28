/* Implements FutureEventList */
public class LinkedEventList implements FutureEventList {
    private Node head;
    private int size;
    private int simulationTime;
    public LinkedEventList(){
        this.head = null;
        this.size = 0;
        this.simulationTime = 0;
    }

//////////////////////////// Node class for LinkedEventList ///////////////////////////////////////
    private class Node{
        private Event event;
        private Node next;
        public Node(Event event){
            this.event = null;
            this.next = null;
        }

    }
///////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Event removeFirst() {
        if(head == null){
            return null;
        }
        Node removedNode = head;
        head = head.next;
        size--;
        if(removedNode.event != null){
            this.simulationTime = removedNode.event.getArrivalTime();
            return removedNode.event;
        } else{
            return null;
        }
    }


    @Override
    public boolean remove(Event e) {
        Node current = head;
        Node prev = null;

        while(current != null && current.event != e){
            prev = current;
            current = current.next;
        }
        if(current == null){
            return false; // Event not found
        }

        if(prev == null){
            head = current.next; // Remove first node
        } else{
            prev.next = current.next;  // Remove node in the middle or end
        }
        size--;

        if(current.event != null){
            this.simulationTime = current.event.getInsertionTime();
        }
        return true;
    }

    @Override
    public void insert(Event e) {
        Node newNode = new Node(e);
        newNode.next = head;
        head = newNode;
        size++;

        this.simulationTime = Math.max(this.simulationTime, e.getArrivalTime());

    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int capacity() {
        return size;
    }

    @Override
    public int getSimulationTime() {
        return this.simulationTime;
    }
}




