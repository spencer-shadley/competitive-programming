import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Spencer on 9/13/2015.
 *
 * Solving CodeForces: http://codeforces.com/problemset/problem/20/C/
 *
 */
public class Dijkstra {

    public ArrayList<Node> nodeList;

    public static class Edge {
        public int cost;
        public Node n;

        public Edge(Node n, int cost) {
            this.n = n;
            this.cost = cost;
        }

    }

    public class Node {
        public boolean visited;
        public int cost;
        public ArrayList<Edge> edges;
        public Node previous;
        public int name;

        public Node(int name) {
            this.visited = false;
            this.cost = Integer.MAX_VALUE;
            this.name = name;
        }
    }


    public static void main(String[] args) {

        // construct graph
        Scanner scan = new Scanner(System.in);
        Dijkstra graph = new Dijkstra();

        int numVertices = scan.nextInt();
        int numEdges = scan.nextInt();
        scan.nextLine();

        for(int i = 0; i < numEdges; i++) {

            int num1 = scan.nextInt();
            int num2 = scan.nextInt();
            int cost = scan.nextInt();
            scan.nextLine();

            // add the nodes to the graph
            Node n1 = graph.addNode(num1);
            Node n2 = graph.addNode(num2);

            // undirected graph
            n1.edges.add(new Edge(n2, cost));
            n2.edges.add(new Edge(n1, cost));
        }

    }

    // add a new or existing node to the graph
    public Node addNode(int name, int edgeCost) {


        // prevent duplicates
        for(Node listNode : nodeList)
            if(listNode.name == name)
                return listNode;

        Node n = new Node(name);
        nodeList.add(n);
        return n;

    }

    public static Node getNode(int name, ArrayList<Node> nodeList) {
        for(Node n : nodeList)
            if(n.name == name)
                return n;
        return null;
    }

}
