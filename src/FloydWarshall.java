public class FloydWarshall {

    public static final int MAX_INT = 100000000;

    public static void main(String[] args) {
        int[][] allPairsGraph = {
                { 0, 3, 6, MAX_INT, MAX_INT, MAX_INT, MAX_INT},
                { 3, 0, 2, 1, MAX_INT, MAX_INT, MAX_INT},
                { 6,         2, 0, 1, 4, 2, MAX_INT},
                { MAX_INT,1, 1, 0, 2, MAX_INT,4},
                { MAX_INT, MAX_INT,4, 2, 0, 2, 1},
                { MAX_INT, MAX_INT,2, MAX_INT,2, 0, 1},
                { MAX_INT, MAX_INT, MAX_INT,4, 1, 1, 0}
                };
        /* How the graph should look at the end:
            0 3 4 5 6 7 7
            3 0 2 1 3 4 4
            4 2 0 1 3 2 3
            5 1 1 0 2 3 3
            6 3 3 2 0 2 1
            7 4 2 3 2 0 1
            7 4 3 3 1 1 0

         */
        System.out.println("------------Floyd-Warshall Algorithm----------------");
        System.out.println("Distances between vertices before");
        for(int[] row : allPairsGraph) {
            for(int num : row) {
                if (num == MAX_INT)
                    System.out.print("NF");
                else
                    System.out.print(num);
                System.out.print("\t");
            }
            System.out.println();
        }
        System.out.println("The distances between each vertex after FloydWarshall");
        // print and get the shortest graph!
        for(int[] row : floydWarshall(allPairsGraph)) {
            for(int num : row) {
                if(num == MAX_INT)
                    System.out.print("NF");
                else
                    System.out.print(num);
                System.out.print("\t");
            }
            System.out.println();
        }
        System.out.println("------------Johnson Algorithm----------------");
        Johnson john = new Johnson(allPairsGraph);
        System.out.println("------------A Star Algorithm----------------");
        AStarAlgorithm star = new AStarAlgorithm();
        System.out.println("Graph used for network algorithms: " + "https://www.hackerearth.com/practice/algorithms/graphs/maximum-flow/tutorial/");
        System.out.println("Expected maximum flow for this graph: 15");
        // graph used for network graphs: https://www.hackerearth.com/practice/algorithms/graphs/maximum-flow/tutorial/
        System.out.println("------------Ford-Fulkerson Algorithm----------------");
        FordFulkerson ford = new FordFulkerson();
        System.out.println("------------Goldberg-Tarjan Algorithm----------------");
        GoldbergTarjan gold = new GoldbergTarjan();

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
