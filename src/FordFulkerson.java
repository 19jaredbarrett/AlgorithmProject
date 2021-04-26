public class FordFulkerson {

    public int fordFulkerson(int[][] graph, int s, int dst) {
        // initialize flow graph
        int numVertices = graph.length;

        int[][] flowGraph = new int[numVertices][numVertices];
        // these variables are used to keep track of the vertex/edges
        int u, v;
        // initialize our flow graph with all of the vertices
        for(u = 0; u < numVertices; u++)
            for (v = 0; v < numVertices; v++)
                flowGraph[u][v] = graph[u][v];


        return -1;
    }
}
