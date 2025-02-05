class StepsToComfort {
    static class Node {
        String data;
        Node next;

        public Node(String data) {
            this.data = data;
            this.next = null;
        }
    }

    static class LinkedList {
        Node head;

        public void insert(String data) {
            Node newNode = new Node(data);
            if (head == null) {
                head = newNode;
                return;
            }
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }

        public boolean search(String key) {
            Node temp = head;
            while (temp != null) {
                if (temp.data.equals(key)) {
                    return true;
                }
                temp = temp.next;
            }
            return false;
        }

        public void display() {
            Node temp = head;
            while (temp != null) {
                System.out.print(temp.data + " -> ");
                temp = temp.next;
            }
            System.out.println("None");
        }
    }

    public static void main(String[] args) {
        LinkedList list = new LinkedList();
        list.insert("Foot Measurement 1");
        list.insert("Foot Measurement 2");
        list.insert("Foot Measurement 3");
        list.insert("Custom Shoe Order");

        list.display();

        String searchValue = "Foot Measurement 2";
        System.out.println("Searching for " + searchValue + ": " + list.search(searchValue));
        
        searchValue = "Non-existent Measurement";
        System.out.println("Searching for " + searchValue + ": " + list.search(searchValue));
    }
}
