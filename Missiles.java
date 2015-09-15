import java.util.*;

/**
 * Created by Spencer on 9/15/2015.
 *
 * Solving CodeForces: http://codeforces.com/problemset/problem/144/D
 *
 */
public class Missiles {

    public HashMap<Integer, Node> graph = new HashMap<>();
    public ArrayList<Road> roads = new ArrayList<>();

    public static class Road {
        public int cost;
        public Node n;
        public boolean[] missiles;

        public Road(Node n, int cost) {
            this.n = n;
            this.cost = cost;
            this.missiles = new boolean[cost];
        }
}

    public class Node implements Comparable<Node>{
        public boolean visited;
        public int cost;
        public ArrayList<Road> roads = new ArrayList<>();
        public Node previous;
        public int name;
        public boolean isMissile;

        public Node(int name) {
            this.visited = false;
            this.cost = Integer.MAX_VALUE;
            this.name = name;
            this.isMissile = false;
        }

        public int compareTo(Node n) {
            return Integer.compare(this.cost, n.cost);
        }
    }

    public static void main(String[] args) {
        Missiles m = new Missiles();
        m.input();
    }

    public void input() {
        Scanner scan = new Scanner(System.in);
        int numCities = scan.nextInt();
        int numRoads = scan.nextInt();
        int capital = scan.nextInt();

        // create all nodes
        for(int i = 1; i <= numCities; i++)
            graph.put(i, new Node(i));

        for(int i = 0; i < numRoads; i++) {

            scan.nextLine();
            Node node1 = graph.get(scan.nextInt());
            Node node2 = graph.get(scan.nextInt());
            int cost = scan.nextInt();


            node1.roads.add(new Road(node2, cost));
            node2.roads.add(new Road(node1, cost));

        }
//        dijkstra(graph.get(capital));
//        missiles(scan.nextInt());
        getMissiles(graph.get(capital), scan.nextInt());
    }

    public void getMissiles(Node start, int distance) {

        int numMissiles = 0;
        start.cost = 0;

        Queue<Node> q = new PriorityQueue<>();
        q.add(start);

        while(!q.isEmpty()) {
            Node currNode = q.poll();

            if(currNode.visited) continue;

            currNode.visited = true;
            for(Road r : currNode.roads) {
                Node roadNode = r.n;
                q.add(roadNode);

                int nodeWithRoad = currNode.cost + r.cost;

                // found better path
                if (nodeWithRoad < roadNode.cost) {
                    roadNode.cost = nodeWithRoad;
                    roadNode.previous = currNode;
                }

                // is the node a missile?
                if(currNode.cost == distance && !currNode.isMissile) {
                    numMissiles++;
                    currNode.isMissile = true;
                }

                // is there a missile on the road?
                int remainingDistance = distance - currNode.cost;
                if( remainingDistance > 0 &&
                    remainingDistance < r.missiles.length &&
                    currNode.cost < distance &&
                    !r.missiles[remainingDistance] &&
                    nodeWithRoad > distance) {

                    for(Road road : roadNode.roads) {

                        // find path going the other way
                        if(road.n.equals(currNode)) {

                            // free spot for a missile
                            int flipped = Math.abs(road.missiles.length - distance - road.n.cost); // look at the other side of the road
                            if(flipped > 0 && flipped < road.missiles.length && !road.missiles[flipped])
                                numMissiles++;
                            r.missiles[remainingDistance] = true;
                        }
                    }
                }
            }
        }
        System.out.print(numMissiles);
        System.exit(0);
    }

    public void dijkstra(Node start) {
        if(start == null) throw new NullPointerException("start is null");

        PriorityQueue<Node> q = new PriorityQueue<>();
        q.add(start);

        while(!q.isEmpty()) {
            Node n = q.poll();
            if(!n.visited) {
                n.visited = true;

                // Visit all connected nodes from n
                for (Road e : n.roads) {
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

    public int missiles(int distance) {

        int missileSpots = 0;
        for(Node n : graph.values()) {
            int cost = n.cost;
            if(cost == distance && !n.isMissile) {
                n.isMissile = true;
                missileSpots++;
            }
            if(cost < distance) {
                int remaining = distance - cost;
                for (Road r : n.roads) {
                    if(r.cost > remaining && !r.missiles[remaining]) {
                        r.missiles[remaining] = true;
                        missileSpots++;
                    }
                }
            }
        }

        return 0;
    }

}
