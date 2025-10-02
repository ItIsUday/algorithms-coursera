package algorithms_1.deques; // Remove this line before submission

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    
    private class Node {
        Item value;
        Node prev;
        Node next;
        
        Node(Item value, Node prev, Node next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }
    
    private Node head;
    private Node tail;
    private int size;

    // construct an empty deque
    public Deque() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return this.size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return this.size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("Argument cannot be null");
        Node node = new Node(item, null, this.head);
        if (this.head != null) {
            this.head.prev = node;
        }
        this.head = node;
        if (this.tail == null) {
            this.tail = node;
        }
        this.size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("Argument cannot be null");
        Node node = new Node(item, this.tail, null);
        if (this.tail != null) {
            this.tail.next = node;
        }
        this.tail = node;
        if (this.head == null) {
            this.head = node;
        }
        this.size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        Item value = this.head.value;
        this.head = this.head.next;
        this.size--;
        if (isEmpty()) {
            this.tail = null;
        }
        else {
            this.head.prev = null;
        }
        return value;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        Item value = this.tail.value;
        this.tail = this.tail.prev;
        this.size--;
        if (isEmpty()) {
            this.head = null;
        }
        else {
            this.tail.next = null;
        }
        return value;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }
    
    private class DequeIterator implements Iterator<Item> {
        private Node current = head;
        
        public boolean hasNext() {
            return current != null;
        }
        
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = current.value;
            current = current.next;
            return item;
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        System.out.println("=== Testing Deque Implementation ===");
        
        // Test 1: Constructor - create empty deque
        System.out.println("\n1. Testing constructor:");
        Deque<String> deque = new Deque<String>();
        System.out.println("Created empty deque");
        System.out.println("isEmpty(): " + deque.isEmpty()); // Should be true
        System.out.println("size(): " + deque.size()); // Should be 0
        
        // Test 2: addFirst() and basic operations
        System.out.println("\n2. Testing addFirst():");
        deque.addFirst("first");
        System.out.println("Added 'first' to front");
        System.out.println("isEmpty(): " + deque.isEmpty()); // Should be false
        System.out.println("size(): " + deque.size()); // Should be 1
        
        deque.addFirst("second");
        System.out.println("Added 'second' to front");
        System.out.println("size(): " + deque.size()); // Should be 2
        
        // Test 3: addLast()
        System.out.println("\n3. Testing addLast():");
        deque.addLast("third");
        System.out.println("Added 'third' to back");
        System.out.println("size(): " + deque.size()); // Should be 3
        
        deque.addLast("fourth");
        System.out.println("Added 'fourth' to back");
        System.out.println("size(): " + deque.size()); // Should be 4
        
        // Test 4: Iterator - should print: second, first, third, fourth
        System.out.println("\n4. Testing iterator (front to back):");
        System.out.print("Items: ");
        for (String item : deque) {
            System.out.print(item + " ");
        }
        System.out.println();
        
        // Test 5: removeFirst()
        System.out.println("\n5. Testing removeFirst():");
        String removed = deque.removeFirst();
        System.out.println("Removed from front: " + removed); // Should be "second"
        System.out.println("size(): " + deque.size()); // Should be 3
        
        removed = deque.removeFirst();
        System.out.println("Removed from front: " + removed); // Should be "first"
        System.out.println("size(): " + deque.size()); // Should be 2
        
        // Test 6: removeLast()
        System.out.println("\n6. Testing removeLast():");
        removed = deque.removeLast();
        System.out.println("Removed from back: " + removed); // Should be "fourth"
        System.out.println("size(): " + deque.size()); // Should be 1
        
        removed = deque.removeLast();
        System.out.println("Removed from back: " + removed); // Should be "third"
        System.out.println("size(): " + deque.size()); // Should be 0
        System.out.println("isEmpty(): " + deque.isEmpty()); // Should be true
        
        // Test 7: Edge case - operations on empty deque
        System.out.println("\n7. Testing edge cases:");
        
        // Test adding to empty deque
        System.out.println("Adding to empty deque:");
        deque.addFirst("alone1");
        System.out.println("Added 'alone1' to front of empty deque");
        System.out.println("size(): " + deque.size()); // Should be 1
        String aloneItem = deque.removeFirst();
        System.out.println("Removed: " + aloneItem); // Should be "alone1"
        System.out.println("isEmpty(): " + deque.isEmpty()); // Should be true
        
        deque.addLast("alone2");
        System.out.println("Added 'alone2' to back of empty deque");
        System.out.println("size(): " + deque.size()); // Should be 1
        aloneItem = deque.removeLast();
        System.out.println("Removed: " + aloneItem); // Should be "alone2"
        System.out.println("isEmpty(): " + deque.isEmpty()); // Should be true
        
        // Test 8: Exception handling
        System.out.println("\n8. Testing exception handling:");
        
        // Test null argument exception
        try {
            deque.addFirst(null);
            System.out.println("ERROR: Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ addFirst(null) correctly threw IllegalArgumentException");
        }
        
        try {
            deque.addLast(null);
            System.out.println("ERROR: Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ addLast(null) correctly threw IllegalArgumentException");
        }
        
        // Test operations on empty deque
        try {
            deque.removeFirst();
            System.out.println("ERROR: Should have thrown NoSuchElementException");
        } catch (NoSuchElementException e) {
            System.out.println("✓ removeFirst() on empty deque correctly threw NoSuchElementException");
        }
        
        try {
            deque.removeLast();
            System.out.println("ERROR: Should have thrown NoSuchElementException");
        } catch (NoSuchElementException e) {
            System.out.println("✓ removeLast() on empty deque correctly threw NoSuchElementException");
        }
        
        // Test 9: Iterator on empty deque
        System.out.println("\n9. Testing iterator on empty deque:");
        System.out.print("Items in empty deque: ");
        for (String item : deque) {
            System.out.print(item + " ");
        }
        System.out.println("(should be empty)");
        
        // Test 10: Iterator next() on empty iterator
        System.out.println("\n10. Testing iterator exceptions:");
        Iterator<String> iter = deque.iterator();
        try {
            iter.next();
            System.out.println("ERROR: Should have thrown NoSuchElementException");
        } catch (NoSuchElementException e) {
            System.out.println("✓ Iterator.next() on empty deque correctly threw NoSuchElementException");
        }
        
        try {
            iter.remove();
            System.out.println("ERROR: Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            System.out.println("✓ Iterator.remove() correctly threw UnsupportedOperationException");
        }
        
        // Test 11: Comprehensive test with mixed operations
        System.out.println("\n11. Comprehensive mixed operations test:");
        Deque<Integer> intDeque = new Deque<Integer>();
        
        // Mix of addFirst and addLast
        intDeque.addFirst(1);
        intDeque.addLast(2);
        intDeque.addFirst(0);
        intDeque.addLast(3);
        intDeque.addFirst(-1);
        
        System.out.print("After mixed adds, deque contains: ");
        for (Integer num : intDeque) {
            System.out.print(num + " ");
        }
        System.out.println(); // Should be: -1 0 1 2 3
        System.out.println("Final size: " + intDeque.size()); // Should be 5
        
        // Mix of removeFirst and removeLast
        System.out.println("removeFirst(): " + intDeque.removeFirst()); // Should be -1
        System.out.println("removeLast(): " + intDeque.removeLast()); // Should be 3
        System.out.println("removeFirst(): " + intDeque.removeFirst()); // Should be 0
        
        System.out.print("After mixed removes, deque contains: ");
        for (Integer num : intDeque) {
            System.out.print(num + " ");
        }
        System.out.println(); // Should be: 1 2
        System.out.println("Final size: " + intDeque.size()); // Should be 2
        
        System.out.println("\n=== All tests completed ===");
    }
}
