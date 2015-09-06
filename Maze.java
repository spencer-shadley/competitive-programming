import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by Spencer on 9/5/2015.
 *
 * Solving CodeForces: http://codeforces.com/problemset/problem/377/A
 *
 */
public class Maze {

    public enum States { FREE, WALL, ADDEDWALL }

    private static class Node {
        public boolean visited;
        public char value;
        public int x;
        public int y;

        public Node(boolean visited, char value, int x, int y) {
            this.visited = visited;
            this.value = value;
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int rows = scan.nextInt();
        int cols = scan.nextInt();
        int ogBoxes = scan.nextInt();

        Node[][] ogMaze = createMaze(new Node[rows][cols], scan);
//        prettyMaze(addWalls(maze, boxes, 0, 0));

        for(int i = 0; i < ogMaze.length; i++) {
            for(int j = 0; j < ogMaze[0].length; j++) {
                System.out.println("i: " + i + "\tj: " + j);

                Node[][] maze = deepCopy(ogMaze);
                int boxes = ogBoxes;

                resetVisited(maze);
                LinkedList<Node> list = getPath(maze, i, j, new LinkedList<>());

                for (Node n : list) {
                    if (boxes != 0 && n.value == '.') {
                        n.value = 'X';
                        boxes--;
                    }
                }

                for(int k = 0; k < maze.length; k++) {
                    for (int l = 0; l < maze[0].length; l++) {
                        System.out.println("k: " + k + "\tl: " + l);

                        resetVisited(maze);
                        int c = isConnectedArea(maze, k, l)+1;
                        resetVisited(maze);
                        int g = getFreeSpaces(maze);

                        System.out.println("c: " + c + "\tg: " + g);
                        boolean connected = c == g;
                        prettyMaze(maze);

                        if (connected) {
                            prettyMaze(maze);
                            prettyList((list));
                            System.exit(0);
                        }
                    }
                }
            }
        }
        System.out.println("couldn't find anything");

//        System.out.println(isConnectedArea(ogMaze, 0, 0));

        /*int moves = isConnectedArea(maze, 0, 1);
        int spaces = getFreeSpaces(maze);
        System.out.println(moves);
        System.out.println(spaces);
        System.out.println(moves == spaces);*/
    }

    private static void resetVisited(Node[][] maze) {
        for(Node[] row : maze)
            for(Node n : row)
                n.visited = false;
    }

    private static Node[][] addWalls(Node[][] maze, int numWallsLeft, int currRow, int currCol) {

        Node currNode = maze[currRow][currCol];
        boolean pushed = false;

        if(numWallsLeft == 0) {
            System.out.println("all done");
            System.exit(0);
            return maze;
        }

        // above
        if(currRow != maze.length-1 && !maze[currRow + 1][currCol].visited && maze[currRow+1][currCol].value=='.') {

            pushed = true;
            Node[][] mazeCpy = deepCopy(maze);
            mazeCpy[currRow+1][currCol].value = 'X';
            if(isConnectedArea(mazeCpy, currRow+1, currCol) == getFreeSpaces(mazeCpy)) {
                maze[currRow+1][currCol].value = 'X';
//                prettyMaze(maze);
                addWalls(maze, numWallsLeft-1, currRow+1, currCol);
            } else {
                addWalls(maze, numWallsLeft, currRow+1, currCol);
            }

        }

        // below
        if(currRow != 0 && !maze[currRow - 1][currCol].visited && maze[currRow-1][currCol].value=='.') {

            pushed = true;
            Node[][] mazeCpy = deepCopy(maze);
            mazeCpy[currRow-1][currCol].value = 'X';
            if(isConnectedArea(mazeCpy, currRow-1, currCol) == getFreeSpaces(mazeCpy)) {
                maze[currRow-1][currCol].value = 'X';
//                prettyMaze(maze);
                addWalls(maze, numWallsLeft-1, currRow-1, currCol);
            } else {
                addWalls(maze, numWallsLeft, currRow-1, currCol);
            }

        }

        // left
        if(currCol != 0 && !maze[currRow][currCol-1].visited && maze[currRow][currCol-1].value=='.') {

            pushed = true;
            Node[][] mazeCpy = deepCopy(maze);
            mazeCpy[currRow][currCol-1].value = 'X';
            if(isConnectedArea(mazeCpy, currRow, currCol-1) == getFreeSpaces(mazeCpy)) {
                maze[currRow][currCol-1].value = 'X';
//                prettyMaze(maze);
                addWalls(maze, numWallsLeft-1, currRow, currCol-1);
            } else {
                addWalls(maze, numWallsLeft, currRow, currCol-1);
            }

        }

        // right
        if(currCol != maze[0].length-1 && !maze[currRow][currCol+1].visited && maze[currRow][currCol+1].value=='.') {

            pushed = true;
            Node[][] mazeCpy = deepCopy(maze);
            mazeCpy[currRow][currCol+1].value = 'X';
            if(isConnectedArea(mazeCpy, currRow, currCol+1) == getFreeSpaces(mazeCpy)) {
                maze[currRow][currCol+1].value = 'X';
//                prettyMaze(maze);
                addWalls(maze, numWallsLeft - 1, currRow, currCol+1);
            } else {
                addWalls(maze, numWallsLeft, currRow, currCol+1);
            }

        }

        // finished visiting all neighbors
        if(!pushed) {
            currNode.visited = true;
        }
        return maze;
    }

    private static Node[][] deepCopy(Node[][] values) {

        final Node[][] result = new Node[values.length][values[0].length];
        for (int i = 0; i < values.length; i++) {
            for(int j = 0; j < values[0].length; j++) {
                Node ogNode = values[i][j];
                result[i][j] = new Node(ogNode.visited, ogNode.value, ogNode.x, ogNode.y);
            }
        }
        return result;
    }

    private static void prettyList(LinkedList<Node> list) {
        for(Node n : list) {
            System.out.println(n.value + " (" + n.x + ", " + n.y + ") ");
        }
        System.out.println();
    }

    // if you can reach all empty squares
    private static LinkedList<Node> getPath(Node[][] maze, int currRow, int currCol, LinkedList<Node> currPath) {

        Node save = maze[currRow][currCol];
        maze[currRow][currCol] = new Node(false, 'O', currRow, currCol);
//        prettyMaze(maze);
        maze[currRow][currCol] = save;

        Node currNode = maze[currRow][currCol];
        currNode.visited = true;

        LinkedList<Node> currList = new LinkedList<>();
        currList.add(currNode);
//        prettyList(combine(currList, currPath));

        LinkedList<Node> above = new LinkedList<>();
        LinkedList<Node> below = new LinkedList<>();
        LinkedList<Node> left = new LinkedList<>();
        LinkedList<Node> right = new LinkedList<>();

        currPath.add(currNode);
//        prettyList(currPath);

        // above
        if (currRow != maze.length - 1 && !maze[currRow + 1][currCol].visited && maze[currRow + 1][currCol].value == '.') {
            above = currPath;
            getPath(maze, currRow + 1, currCol, currPath);
        }

        // below
        if (currRow != 0 && !maze[currRow - 1][currCol].visited && maze[currRow - 1][currCol].value == '.') {
            below = currPath;
            getPath(maze, currRow - 1, currCol, currPath);
        }

        // left
        if (currCol != 0 && !maze[currRow][currCol - 1].visited && maze[currRow][currCol - 1].value == '.') {
            left = currPath;
            getPath(maze, currRow, currCol - 1, currPath);
        }

        // right
        if(currCol != maze[0].length-1 && !maze[currRow][currCol+1].visited && maze[currRow][currCol+1].value=='.') {
            right = currPath;
            getPath(maze, currRow, currCol + 1, currPath);
        }

        if(above.size() >= below.size() && above.size() >= left.size() && above.size() >= right.size()) return above;
        if(below.size() >= above.size() && below.size() >= left.size() && below.size() >= right.size()) return below;
        if(left.size() >= below.size() && left.size() >= above.size() && left.size() >= right.size()) return left;
        if(right.size() >= below.size() && right.size() >= left.size() && right.size() >= above.size()) return right;

        return currPath; // makes Java happy
    }

    public static LinkedList<Node> combine(LinkedList<Node> list1, LinkedList<Node> list2) {

        for(Node element: list2)
            list1.add(element);

        return list1;
    }

    // if you can reach all empty squares
    private static int isConnectedArea(Node[][] maze, int currRow, int currCol) {

        if(maze[currRow][currCol].value!='.')
            return 0;

       /* Node save = maze[currRow][currCol];
        maze[currRow][currCol] = new Node(false, 'O', currRow, currCol);
        System.out.println("isconnectedarea");
        prettyMaze(maze);
        maze[currRow][currCol] = save;*/

        Node currNode = maze[currRow][currCol];
        currNode.visited = true;
        int seenNodes = 0;

        // above
        if(currRow != maze.length-1 && !maze[currRow+1][currCol].visited && maze[currRow+1][currCol].value=='.')
            seenNodes += isConnectedArea(maze, currRow+1, currCol)+1;

        // below
        if(currRow != 0 && !maze[currRow-1][currCol].visited && maze[currRow-1][currCol].value=='.')
            seenNodes += isConnectedArea(maze, currRow-1, currCol)+1;

        // left
        if(currCol != 0 && !maze[currRow][currCol-1].visited && maze[currRow][currCol-1].value=='.')
            seenNodes += isConnectedArea(maze, currRow, currCol - 1)+1;

        // right
        if(currCol != maze[0].length-1 && !maze[currRow][currCol+1].visited && maze[currRow][currCol+1].value=='.')
            seenNodes += isConnectedArea(maze, currRow, currCol+1)+1;

        return seenNodes;
    }

    private static int getFreeSpaces(Node[][] maze) {
        int freeSpaces = 0;
        for(Node[] row : maze)
            for(Node n : row)
                if(getState(n.value) == States.FREE)
                    freeSpaces++;
        return freeSpaces;
    }

    // if there is a wall or boundary on all sides
    private static boolean isSurrounded(Node[][] maze, int rowIndex, int colIndex) {
        return  (rowIndex==0                || isWall(maze, rowIndex-1, colIndex)) &&  // up
                (colIndex==maze[0].length-1 || isWall(maze, rowIndex, colIndex+1)) &&  // right
                (rowIndex==maze.length-1    || isWall(maze, rowIndex+1, colIndex)) &&  // down
                (colIndex==0                || isWall(maze, rowIndex, colIndex-1));      // left
    }

    private static boolean isWall(Node[][] maze, int row, int col) {
        return getState(maze[row][col].value) == States.WALL || getState(maze[row][col].value) == States.ADDEDWALL;
    }

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

    private static Node[][] createMaze(Node[][] maze, Scanner scan) {
        scan.nextLine();
        for(int rowIndex = 0; rowIndex < maze.length; rowIndex++) {
            String line = scan.nextLine();
            for (int colIndex = 0; colIndex < maze[0].length; colIndex++) {
                maze[rowIndex][colIndex] = new Node(false, line.charAt(colIndex), rowIndex, colIndex);
            }
        }
        return maze;
    }

    private static void prettyMaze(Node[][] maze) {
        for(int rowIndex = 0; rowIndex < maze.length; rowIndex++) {
            for(int colIndex = 0; colIndex < maze[0].length; colIndex++) {
                System.out.print(maze[rowIndex][colIndex].value);
            }
            if(rowIndex < maze.length-1) System.out.println();
        }
        System.out.println();
        System.out.println();
    }
}