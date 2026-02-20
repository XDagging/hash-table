public class Node {


    public String value;
    public Node next;

    public Node(String val) {
        value = val;
    }

    public Node getNext() {
        return next;
    }

    public void  setNext(Node newNode) {
        next = newNode;
    }

}
