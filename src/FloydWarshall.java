public class FloydWarshall {

    public static final int MAX_INT = 100000000;

    public static void main(String[] args) {
        int graph[][] = {   {0,         5,      MAX_INT,    10},
                            {MAX_INT,   0,      3,          MAX_INT},
                            {MAX_INT,   MAX_INT,0,          1},
                            {MAX_INT, MAX_INT, MAX_INT, 0}
        };
        // print and get the shortest graph!
        for(int[] row : floydWarshall(graph)) {
            for(int num : row) {
                if(num == MAX_INT)
                    System.out.print("NF");
                else
                    System.out.print(num);
                System.out.print("\t");
            }
            System.out.println();
        }
        FordFulkerson ford = new FordFulkerson();
    }

    public static int[][] floydWarshall(int graph[][]) {
        int numVertices = graph.length;
        int[][] solution = new int[numVertices][numVertices];
        // initialize our solution matrix, for pairs of all vertices
        // if the graph has a null value, the number value is simply Math.Infinity()
        for(int i = 0; i < numVertices; i++)
            for(int j = 0; j < numVertices; j++)
                solution[i][j] = graph[i][j];

        // first, we must initialize the solution matrix
        for(int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if(solution[i][k] + solution[k][j] < solution[i][j])
                        solution[i][j] = solution[i][k] + solution[k][j];
                }
            }
        } // end for loops

        return solution;
    }
}
