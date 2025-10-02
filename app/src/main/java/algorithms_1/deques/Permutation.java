package algorithms_1.deques; // Remove this line before submission

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: java Permutation k");
        }
        
        int k = Integer.parseInt(args[0]);
        
        // Read all strings from StdIn
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) queue.enqueue(StdIn.readString());
        
        // Dequeue k elements and print them
        for (int i = 0; i < k; i++) System.out.println(queue.dequeue());
    }
}
