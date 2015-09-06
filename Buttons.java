import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Created by Spencer on 9/5/2015.
 *
 * Solving CodeForces: http://codeforces.com/problemset/problem/520/B
 *
 * Three optimizations:
 * 1) if input exceeds goal just return the difference
 * 2) keep track of all visited nodes to prevent redundant computation
 * 3) if a multiply would produce a massive number, don't do it (test cases are guaranteed to be relatively small)
 *
 */
public class Buttons {

    private static class Node {
        int data;
        int depth;

        public Node(int data, int depth) {
            this.data = data;
            this.depth = depth;
        }
    }

    public static boolean debug = false;
    public static boolean optDec = true;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println(shortestPath(scan.nextInt(), scan.nextInt()));
    }

    // breadth-first search
    private static int shortestPath(int start, int goal) {

        // if the start > goal, straight computation
        if(optDec && start > goal)
            return start-goal;

        Queue<Node> q = new LinkedList<>();
        q.add(new Node(start, 0));
        int counter = 0;

        HashMap<Integer, Boolean> map = new HashMap<>();

        while(!q.isEmpty()) {
            Node currNode = q.remove();
            map.put(currNode.data, true);
            counter++;

            if(currNode.data == goal) {
                if(debug) System.out.println("counter: " + counter);
                return currNode.depth;
            }

            if(currNode.data >= 0 && !map.containsKey(currNode.data-1)) // positive, not already computed
                q.add(new Node(currNode.data-1, currNode.depth+1));
            if(currNode.data != 0 && currNode.data < 99999 && !map.containsKey(currNode.data*2)) // not zero, small, not already computed
                q.add(new Node(currNode.data*2, currNode.depth+1));
            if(debug) System.out.println(currNode.data);
        }
        return -1; // makes Java happy
    }
}