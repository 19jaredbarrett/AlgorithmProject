public class GoldbergTarjan {
    public Graph g;
    public GoldbergTarjan() {
        // the max flow should be 23.

        int[][] graphTest
                = {{0, 16, 13, 0, 0, 0}, {0, 0, 10, 12, 0, 0},
                {0, 4, 0, 0, 14, 0}, {0, 0, 9, 0, 0, 20},
                {0, 0, 0, 7, 0, 4}, {0, 0, 0, 0, 0, 0}};
        g = new Graph(graphTest, 10);

    }

    public int goldbergTarjan(Graph g, int src, int dst) {
        g.graph[src][0] = g.numVert;

        // initialize our preflow.
        for (int i = 0; i < g.numVert; i++) {
            if (g.capGraph[src][i] != 0) {
                g.flowGraph[src][i] = g.capGraph[src][i];
                g.graph[i][0] = g.capGraph[src][i];
                g.addEdge(i, src);
                g.flowGraph[src][i] -= g.flowGraph[src][i];
            }
        }

        // this integer basically ensures
        int currVert = g.getVertex(src, dst);
        while(currVert != -1) {
            if(!push(currVert))
                relabel(currVert);
            currVert = g.getVertex(src, dst);
        }


        return g.graph[dst][g.EXCESS_FLOW];
    }

    // purpose of function: we push the current vertex to the
    private boolean push(int currVert) {
        for(int i = 0; i < g.numVert; i++) {

        }
        return true;
    }
    private void relabel(int currVert) {

    }


    private class Graph {
        public final int EXCESS_FLOW = 1;
        public int numVert;
        public int numEdges;
        // graph is the vertices: 0 is the h value and 1 is the excess flow.
        public int[][] graph;
        public int[][] capGraph;
        public int[][] flowGraph;

        public Graph(int[][] graph, int numEdges) {
            this.numVert = graph.length;
            this.numEdges = numEdges;
            graph = new int[numVert][2];
            capGraph = new int[numVert][numEdges];
            flowGraph = new int[numVert][numEdges];

            for (int i = 0; i < numVert; i++) {
                for (int j = 0; j < numVert; j++)
                    flowGraph[i][j] = graph[i][j];
            }
        }

        public void addEdge(int vertLoc, int destLoc) {
            flowGraph[vertLoc][destLoc] = graph[vertLoc][destLoc];
        }

        // this method basically gets the vertex that overflows
        public int getVertex(int vertLoc, int edgeLoc) {
            for (int i = 1; i < numVert - 1; i++) {
                if (graph[i][0] > 0) {
                    for (int j = 0; j < numVert; j++) {
                        if (capGraph[i][j] != 0) {
                            if (capGraph[i][j] != flowGraph[i][j]) {
                                return i;
                            }
                        }
                    }
                }
            }
            return -1;
        }
    }

}
