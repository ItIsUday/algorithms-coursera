package algorithms1.deques; // Remove this line before submission

import java.util.Iterator;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.size = 0;
        this.items = (Item[]) new Object[2];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return this.size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return this.size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException("Enter an entry");
        if (this.size == this.items.length) this.resize(2 * this.size);
        this.items[this.size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new java.util.NoSuchElementException("Queue is empty");
        int random = StdRandom.uniformInt(this.size);
        this.swap(this.size - 1, random);
        Item item = this.items[--this.size];
        this.items[this.size] = null;
        if (this.size > 0 && this.size == this.items.length / 4) this.resize(this.items.length / 2);

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new java.util.NoSuchElementException("Queue is empty");
        int random = StdRandom.uniformInt(this.size);

        return this.items[random];
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < this.size; i++) copy[i] = this.items[i];
        this.items = copy;
    }

    private void swap(int i, int j) {
        Item temp = this.items[i];
        this.items[i] = this.items[j];
        this.items[j] = temp;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private int current;
        private final int[] randomIndices;

        public ListIterator() {
            this.current = 0;
            this.randomIndices = new int[size];
            for (int i = 0; i < size; i++) this.randomIndices[i] = i;
            StdRandom.shuffle(this.randomIndices);
        }

        public boolean hasNext() {
            return this.current < size;
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException("No more items to return");
            return items[this.randomIndices[this.current++]];
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException("Remove operation is not supported");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        System.out.println("=== RandomizedQueue Unit Testing ===");
        
        // Test constructor
        System.out.println("\n1. Testing constructor:");
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        System.out.println("Created empty RandomizedQueue");
        
        // Test isEmpty() on empty queue
        System.out.println("\n2. Testing isEmpty() on empty queue:");
        System.out.println("isEmpty(): " + queue.isEmpty()); // Should be true
        
        // Test size() on empty queue
        System.out.println("\n3. Testing size() on empty queue:");
        System.out.println("size(): " + queue.size()); // Should be 0
        
        // Test enqueue()
        System.out.println("\n4. Testing enqueue():");
        queue.enqueue("First");
        System.out.println("Enqueued 'First', size: " + queue.size());
        queue.enqueue("Second");
        System.out.println("Enqueued 'Second', size: " + queue.size());
        queue.enqueue("Third");
        System.out.println("Enqueued 'Third', size: " + queue.size());
        queue.enqueue("Fourth");
        System.out.println("Enqueued 'Fourth', size: " + queue.size());
        
        // Test isEmpty() on non-empty queue
        System.out.println("\n5. Testing isEmpty() on non-empty queue:");
        System.out.println("isEmpty(): " + queue.isEmpty()); // Should be false
        
        // Test size() on non-empty queue
        System.out.println("\n6. Testing size() on non-empty queue:");
        System.out.println("size(): " + queue.size()); // Should be 4
        
        // Test sample()
        System.out.println("\n7. Testing sample() (should not remove items):");
        System.out.println("Sample: " + queue.sample());
        System.out.println("Size after sample: " + queue.size()); // Should still be 4
        System.out.println("Sample: " + queue.sample());
        System.out.println("Size after sample: " + queue.size()); // Should still be 4
        
        // Test iterator
        System.out.println("\n8. Testing iterator:");
        System.out.print("Items via iterator: ");
        for (String item : queue) {
            System.out.print(item + " ");
        }
        System.out.println();
        System.out.println("Size after iteration: " + queue.size()); // Should still be 4
        
        // Test iterator independence
        System.out.println("\n9. Testing iterator independence:");
        Iterator<String> iter1 = queue.iterator();
        Iterator<String> iter2 = queue.iterator();
        System.out.println("Iterator 1 first item: " + iter1.next());
        System.out.println("Iterator 2 first item: " + iter2.next());
        System.out.println("(Should be in different random orders)");
        
        // Test dequeue()
        System.out.println("\n10. Testing dequeue():");
        String dequeued1 = queue.dequeue();
        System.out.println("Dequeued: " + dequeued1 + ", size: " + queue.size());
        String dequeued2 = queue.dequeue();
        System.out.println("Dequeued: " + dequeued2 + ", size: " + queue.size());
        
        // Test resizing (add more items to trigger resize)
        System.out.println("\n11. Testing array resizing:");
        for (int i = 0; i < 10; i++) {
            queue.enqueue("Item" + i);
        }
        System.out.println("Added 10 more items, size: " + queue.size());
        
        // Dequeue all but one to trigger shrinking
        System.out.println("\n12. Testing array shrinking:");
        while (queue.size() > 1) {
            queue.dequeue();
        }
        System.out.println("Dequeued until size = 1, final size: " + queue.size());
        
        // Test exception cases
        System.out.println("\n13. Testing exception cases:");
        
        // Test enqueue(null)
        try {
            queue.enqueue(null);
            System.out.println("ERROR: Should have thrown IllegalArgumentException for null enqueue");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Correctly threw IllegalArgumentException for null enqueue");
        }
        
        // Clear the queue and test empty queue exceptions
        queue.dequeue(); // Remove the last item
        System.out.println("Queue is now empty, size: " + queue.size());
        
        // Test dequeue() on empty queue
        try {
            queue.dequeue();
            System.out.println("ERROR: Should have thrown NoSuchElementException for empty dequeue");
        } catch (java.util.NoSuchElementException e) {
            System.out.println("✓ Correctly threw NoSuchElementException for empty dequeue");
        }
        
        // Test sample() on empty queue
        try {
            queue.sample();
            System.out.println("ERROR: Should have thrown NoSuchElementException for empty sample");
        } catch (java.util.NoSuchElementException e) {
            System.out.println("✓ Correctly threw NoSuchElementException for empty sample");
        }
        
        // Test iterator exceptions
        System.out.println("\n14. Testing iterator exceptions:");
        queue.enqueue("Test");
        Iterator<String> iter = queue.iterator();
        iter.next(); // Consume the only item
        
        try {
            iter.next();
            System.out.println("ERROR: Should have thrown NoSuchElementException for iterator.next()");
        } catch (java.util.NoSuchElementException e) {
            System.out.println("✓ Correctly threw NoSuchElementException for iterator.next()");
        }
        
        try {
            iter.remove();
            System.out.println("ERROR: Should have thrown UnsupportedOperationException for iterator.remove()");
        } catch (java.lang.UnsupportedOperationException e) {
            System.out.println("✓ Correctly threw UnsupportedOperationException for iterator.remove()");
        }
        
        System.out.println("\n=== All tests completed ===");
    }

}
