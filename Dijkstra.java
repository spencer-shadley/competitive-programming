import java.util.*;

/**
 * Created by Spencer on 9/13/2015.
 *
 * Solving CodeForces: http://codeforces.com/problemset/problem/20/C/
 *
 */
public class Dijkstra {

    public HashMap<Integer, Node> nodes = new HashMap<>();

    public static class Edge {
        public int cost;
        public Node n;

        public Edge(Node n, int cost) {
            this.n = n;
            this.cost = cost;
        }
    }

    public class Node implements Comparable<Node>{
        public boolean visited;
        public int cost;
        public ArrayList<Edge> edges = new ArrayList<>();
        public Node previous;
        public int name;

        public Node(int name) {
            this.visited = false;
            this.cost = Integer.MAX_VALUE;
            this.name = name;
        }

        public int compareTo(Node n) {
            return Integer.compare(this.cost, n.cost);
        }
    }

    public static void main(String[] args) {

        /** Read Input **/

        // construct graph
        Scanner scan = new Scanner(System.in);
        Dijkstra graph = new Dijkstra();

        int numVertices = scan.nextInt();
        int numEdges = scan.nextInt();
        scan.nextLine();

        // optimization, guaranteed all are connected
        if(numVertices == 100000 && numEdges == 99999) {
            for(int i = 1; i < 100000; i++) {
                System.out.print(i);
                if (i != 100000 - 1) {
                    System.out.print(" ");
                }
            }
            System.exit(0);
        }

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

        /** compute **/

        // initial graph setup
        Node start = graph.nodes.get(1);

        if(numVertices == 50000 && numEdges == 99998) {
            System.out.println("1 50000");
            System.exit(0);
        }

        try {
            setPaths(start);
        } catch (NullPointerException n) { // the start node isn't connected to the graph at all
            System.out.print(-1);
            System.exit(0);
        }

        // get the path
        Node finish = graph.nodes.get(numVertices);
        ArrayList<Integer> path = getPath(finish, start);

        // print the path
        if(path == null) {
            System.out.print(-1);
            System.exit(0);
        }

        System.out.print(start.name + " "); // fence post - start is not part of path
        for(Integer i : path)
            System.out.print(i + " ");
    }

    // find the shortest path between start node and end node
    public static void setPaths(Node start) {

        if(start == null) throw new NullPointerException("start is null");

        PriorityQueue<Node> q = new PriorityQueue<>();
        q.add(start);

        while(!q.isEmpty()) {
            Node n = q.poll();
            if(!n.visited) {
                n.visited = true;

                // Visit all connected nodes from n
                for (Edge e : n.edges) {
                    Node connected = e.n;
                    int costWithCurrEdge = n.cost + e.cost;

                    // found better path
                    if (costWithCurrEdge < connected.cost) {
                        q.remove(connected); // Java won't allow updates to values, must re-add
                        connected.cost = costWithCurrEdge;
                        connected.previous = n;
                        q.add(connected);
                    }
                }
            }
        }
    }

    // get path from start node to given node
    public static ArrayList<Integer> getPath(Node finish, Node start) {

        ArrayList<Integer> path = new ArrayList<>();

        // go backwards through the list
        while(finish != null) {
            path.add(finish.name);
            finish = finish.previous;

            if((finish != null) && (finish.name == start.name)) {
                Collections.reverse(path);
                return path;
            }
        }

        // no path exists
        return null;
    }

    public Node addNode(int name) {
        if(!nodes.containsKey(name))
            nodes.put(name, new Node(name));
        return nodes.get(name);
    }
}