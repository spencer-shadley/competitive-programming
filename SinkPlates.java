import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Spencer on 9/13/2015.
 *
 * Solving DFS Problem
 * http://codeforces.com/problemset/problem/60/B
 *
 */
public class SinkPlates {

    public static final boolean debug = false;

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        int numLayers = scan.nextInt(); // k
        int numRows = scan.nextInt();   // n
        int numCols = scan.nextInt();   // m
        scan.nextLine();
        scan.nextLine();

        char[][][] plates = new char[numLayers][numRows][numCols];
        boolean[][][] visited = new boolean[numLayers][numRows][numCols];
//        Arrays.fill(visited, false);
        Arrays.deepToString(visited);

        for(int layer = 0; layer < numLayers; layer++) {
            for(int row = 0; row < numRows; row++)
                plates[layer][row] = scan.nextLine().toCharArray();
            scan.nextLine();
        }

        int fX = scan.nextInt()-1;
        int fY = scan.nextInt()-1;

        if(debug) {
            System.out.println("input");
            System.out.println(numLayers + "\t" + numRows + "\t" + numCols);
            System.out.println();
            System.out.println("plate sizes");
            System.out.println(plates.length + "\t" + plates[0].length + "\t" + plates[0][0].length);
            System.out.println();
            System.out.println("other shit");
        }

        int traversed = traverse(plates, visited, 0, fX, fY);
        System.out.println(traversed);
    }

    public static int traverse(char[][][] plates, boolean[][][] visited, int layer, int row, int col) {

        // six directions (up, down, left, right, deepDown, deepUp)
        int[] dx  =    {-1, 1,  0, 0,  0, 0};
        int[] dy  =    { 0, 0, -1, 1,  0, 0};
        int[] dz  =    { 0, 0,  0, 0, -1, 1};

        // out of bounds
        if(layer < 0 || row < 0 || col < 0 || layer >= plates.length || row >= plates[0].length || col >= plates[0][0].length)
            return 0;

        if(debug) {
            System.out.println(layer + "\t" + row + "\t" + col);
            char save = plates[layer][row][col];
            plates[layer][row][col] = 'X';
            System.out.println();
            printPlates(plates);
            plates[layer][row][col] = save;
        }

        // blocked
        if(plates[layer][row][col] == '#')
            return 0;

        // visited
        if(visited[layer][row][col])
            return 0;

        visited[layer][row][col] = true;

        // recurse
        int total = 1;
        for(int i = 0; i < 6; i++) {
            total += traverse(plates, visited, layer + dz[i], row + dx[i], col + dy[i]);
        }
        return total;
    }

    public static void printPlates(char[][][] plates) {
        System.out.println();
        System.out.println("--------------");
        for(int i = 0; i<plates.length; i++) {
            for(int j = 0; j<plates[0].length; j++) {
                for(int k = 0; k<plates[0][0].length; k++)
                    System.out.print(plates[i][j][k]);
                System.out.println();
            }
            System.out.println();
        }
    }

}
