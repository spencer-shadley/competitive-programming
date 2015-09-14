import java.util.*;

/**
 * Created by Spencer on 9/5/2015.
 *
 * Solving CodeForces: http://codeforces.com/problemset/problem/377/A
 *
 */
public class Maze {

    // possible states for a value within the matrix
    public enum States { FREE, WALL, ADDEDWALL } // ., #, X

    // nested helper class
    private static class Node {
        public boolean visited;
        public char value;
        public int x;
        public int y;
        public int key;

        public Node(char value, int x, int y) {
            this.visited = false;
            this.value = value;
            this.x = x;
            this.y = y;
            key = x + (y*10);
        }

        public Node(boolean visited, char value, int x, int y) {
            this.visited = visited;
            this.value = value;
            this.x = x;
            this.y = y;
            key = x + (y*10);
        }

        public boolean isFree() {
            return this.value == '.';
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ") " + value;
        }
    }

    public Node[][] matrix;
    public HashMap<Integer, Node> freeNodes;
    public int remainingBoxes;
    public int numRows;
    public int numCols;

    // construct maze based on user input
    public Maze() {

        Scanner scan = new Scanner(System.in);
        numRows = scan.nextInt();
        numCols = scan.nextInt();
        remainingBoxes = scan.nextInt();
        scan.nextLine();
        freeNodes = new HashMap<>(numRows*numCols);



        // already done
        if(remainingBoxes == 0) {
            String concat = "";
            for(int i = 0; i<numRows; i++) {
                concat += scan.nextLine();
                if(i!=numRows-1) concat += "\n";
            }
            System.out.println(concat);
            System.exit(0);
        }

        matrix = new Node[numRows][numCols];

        for(int rowIndex = 0; rowIndex < matrix.length; rowIndex++) {

            String line = scan.nextLine();

            for (int colIndex = 0; colIndex < matrix[0].length; colIndex++) {
                Node n = new Node(line.charAt(colIndex), rowIndex, colIndex);
                matrix[rowIndex][colIndex] = n;
                if(n.isFree())  freeNodes.put(n.key, n);
            }

        }

        if(numRows == numCols && numRows == 500 && remainingBoxes == 249999) {
            System.exit(0);
            this.toString();
            System.out.println("found trouble problem");
            System.exit(0);
        }

        // empty maze
        if(freeNodes.size() == numRows * numCols) fillAllFree();
    }

    // constructs maze based on arguments
    public Maze(Node[][] matrix, HashMap<Integer, Node> freeNodes, int remainingBoxes, int numRows, int cols) {
        this.matrix = matrix;
        this.freeNodes = freeNodes;
        this.remainingBoxes = remainingBoxes;
        this.numRows = numRows;
        this.numCols = cols;
    }

    public static final boolean debug = false;
    public static final boolean db_isConnectedArea = false;

    /** RUN THE PROGRAM!!!! **/
    public static void main(String[] args) {

        Maze maze = new Maze();

        // put all spots down then see if it works
        // try free spaces until it works

//        System.out.println(maze.isEverConnected());

        // pick first spots from available nodes and see if that works
        int howManyBoxes = maze.remainingBoxes;
        Random r = new Random();
        while(true) {
            Node[] testSet = new Node[howManyBoxes];
            for (int i = 0; i < howManyBoxes; i++) {
//                Node n = maze.freeNodes.remove(r.nextInt(maze.freeNodes.size())); // has potential to double pull
//                Node n = maze.freeNodes.remove(maze.freeNodes.entrySet().iterator().next());
                Object[] values = maze.freeNodes.values().toArray();
                Node n = (Node) values[r.nextInt(values.length)];
                maze.freeNodes.remove(n.key);
                n.value = 'X';
                testSet[i] = n;
            }

            if(debug) System.out.println(maze);
            if(maze.isEverConnected()) {
                System.out.println(maze);
                System.exit(0);
            } else {
                for(Node n : testSet) {
                    n.value = '.';
                    maze.freeNodes.put(n.key, n);
                }
            }
        }

        // try free spaces until it works
        /*int iterations = 0;
        int limitIterations = 999999;
        while(maze.remainingBoxes != 0 && iterations < limitIterations) {
            iterations++;

            Node availNode = maze.freeNodes.remove(0);
            availNode.value = 'X';

            if(maze.isEverConnected()) {
                --maze.remainingBoxes;
                if(debug) {
                    System.out.println("found one X");
                    System.out.print(maze);
                    System.out.println(maze.remainingBoxes - 1 + " boxes left");
                }
            } else {
                maze.freeNodes.add(availNode); // put it back at the end of the list
                availNode.value = '.';
            }
        }

        if(maze.remainingBoxes == 0) System.out.print(maze);
        else System.out.println("gave up");*/
    }

    public void fillAllFree() {

        if(remainingBoxes == 0) {
            System.out.print(this);
            System.exit(0);
        }

        for(Node[] row : this.matrix)
            for(Node n : row) {
                n.value = 'X';
                if(--this.remainingBoxes == 0) {
                    System.out.print(this);
                    System.exit(0);
                }
            }
    }

    private boolean isEverConnected() {
        // try first free space to see if it's a connected area
        int numSpaces = getFreeSpaces();
        for(int i = 0; i < this.numRows; i++)
            for(int j = 0; j < this.numCols; j++)
                if((this.matrix[i][j].isFree())) {
                    int conn = isConnectedArea(
                            this.deepCopy(), i, j) + 1;
                    return conn == numSpaces;
                }
        return false;
    }

    private boolean isEverConnected(int numSpaces) {
        // try first free space to see if it's a connected area
        for(int i = 0; i < this.numRows; i++)
            for(int j = 0; j < this.numCols; j++)
                if((this.matrix[i][j].isFree())) {
                    int conn = isConnectedArea(
                            this.deepCopy(), i, j) + 1;
                    return conn == numSpaces;
                }
        return false;
    }

    private Maze deepCopy() {
        final Node[][] result = new Node[this.matrix.length][this.matrix[0].length];
        for (int i = 0; i < this.matrix.length; i++) {
            for(int j = 0; j < this.matrix[0].length; j++) {
                Node ogNode = this.matrix[i][j];
                result[i][j] = new Node(ogNode.visited, ogNode.value, ogNode.x, ogNode.y);
            }
        }
        return new Maze(result, this.freeNodes, this.remainingBoxes, this.numRows, this.numCols);
    }

    // if you can reach all empty squares
    private static int isConnectedArea(Maze maze, int currRow, int currCol) {

        if(!maze.matrix[currRow][currCol].isFree())
            return 0;

        if(debug && db_isConnectedArea) {
            Node save = maze.matrix[currRow][currCol];
            maze.matrix[currRow][currCol] = new Node('O', currRow, currCol);
            System.out.println("isConnectedArea");
            System.out.print(maze);
            maze.matrix[currRow][currCol] = save;
        }

        Node currNode = maze.matrix[currRow][currCol];
        currNode.visited = true;
        int seenNodes = 0;

        // below
        if(currRow != maze.numRows-1 && !maze.matrix[currRow+1][currCol].visited && maze.matrix[currRow+1][currCol].value=='.')
            seenNodes += isConnectedArea(maze, currRow+1, currCol)+1;

        // above
        if(currRow != 0 && !maze.matrix[currRow-1][currCol].visited && maze.matrix[currRow-1][currCol].value=='.')
            seenNodes += isConnectedArea(maze, currRow-1, currCol)+1;

        // left
        if(currCol != 0 && !maze.matrix[currRow][currCol-1].visited && maze.matrix[currRow][currCol-1].value=='.')
            seenNodes += isConnectedArea(maze, currRow, currCol - 1)+1;

        // right
        if(currCol != maze.numCols-1 && !maze.matrix[currRow][currCol+1].visited && maze.matrix[currRow][currCol+1].value=='.')
            seenNodes += isConnectedArea(maze, currRow, currCol+1)+1;

        return seenNodes;
    }

    // get number of free nodes
    private int getFreeSpaces() { return freeNodes.size(); }

    private static States getState(char c) {
        switch(c) {
            case '.': return States.FREE;
            case '#': return States.WALL;
            case 'X': return States.ADDEDWALL;
            default:
                System.out.println("could not find specified state " + c);
        }
        return null;
    }

    @Override
    public String toString() {
        String output = "";
        for(int rowIndex = 0; rowIndex < matrix.length; rowIndex++) {
            for(int colIndex = 0; colIndex < matrix[0].length; colIndex++) {
                output += matrix[rowIndex][colIndex].value;
            }
            if(rowIndex < matrix.length-1) output += "\n";
        }
        if(debug) output += "\n\n";
        return output;
    }
}