public class GoldbergTarjan {
    public Graph g;
    public GoldbergTarjan() {
        // the max flow should be 23.
        // graph used: https://www.hackerearth.com/practice/algorithms/graphs/maximum-flow/tutorial/
        int[][] graphTest
                = {{0, 10, 0, 8, 0, 0}, {0, 0, 5, 2, 0, 0},
                {0, 0, 0, 0, 0, 7}, {0, 0, 0, 0, 10, 0},
                {0, 0, 8, 0, 0, 10}, {0, 0, 0, 0, 0, 0}};
        g = new Graph(graphTest);
        int maxFlow = goldbergTarjan(g, 0 , 5);
        System.out.println("Goldberg Tarjan flow: " + maxFlow);

    }

    public int goldbergTarjan(Graph g, int src, int dst) {

        g.graph[src][g.H_VALUE] = g.numVert;
        // initialize our preflow.
        for (int i = 1; i < g.numVert; i++) {
            if (g.capGraph[src][i] != 0) {
                g.flowGraph[src][i] = g.capGraph[src][i];
                g.graph[i][g.EXCESS_FLOW_VALUE] += g.flowGraph[src][i];
                g.addEdge(i, src, 0);
                g.flowGraph[i][src] = (-g.flowGraph[src][i]);
            }
        }
        // this integer basically ensures
        int currVert = g.getVertex();
        while(currVert != -1) {
            if(!push(currVert))
                relabel(currVert);
            currVert = g.getVertex();
        }
        return g.graph[dst][g.EXCESS_FLOW_VALUE];
    }

    // purpose of function: we push the current vertex to the
    private boolean push(int currVert) {
        for(int i = 0; i < g.numVert; i++) {
            // the capacity of this edge is greater than 0
            if(g.capGraph[currVert][i] != 0) {
                // exit the loop if the current vertex's edge is at capacity!
                if(g.flowGraph[currVert][i] == g.capGraph[currVert][i])
                    continue;
                // we push flow to i because it is
                if(g.graph[currVert][g.H_VALUE] > g.graph[i][g.H_VALUE]) {
                    int pushFlowVal = Math.min(g.capGraph[currVert][i] - g.flowGraph[currVert][i], g.graph[currVert][g.EXCESS_FLOW_VALUE]);
                    g.graph[currVert][g.EXCESS_FLOW_VALUE] -= pushFlowVal;
                    g.flowGraph[i][currVert] -= pushFlowVal;
                    g.graph[i][g.EXCESS_FLOW_VALUE] += pushFlowVal;
                    g.flowGraph[currVert][i] += pushFlowVal;
                    return true;
                }
            }
        }
        return false;
    }
    /*
        We relabel the flow for the current vertex
     */
    private void relabel(int currVert) {
        int minH_VALUE = FloydWarshall.MAX_INT;
        for (int i = 0; i < g.numVert; i++) {
                if (g.capGraph[currVert][i] != 0) {
                    if (g.capGraph[currVert][i] == g.flowGraph[currVert][i])
                        continue;
                    if (g.graph[i][g.H_VALUE] < minH_VALUE) {
                        minH_VALUE = g.graph[i][g.H_VALUE];
                        g.graph[currVert][g.H_VALUE] = minH_VALUE +1;
                    }
                }

        }

    }


    private class Graph {
        public final int H_VALUE = 0;
        public final int EXCESS_FLOW_VALUE = 1;
        public int numVert;
        // graph is the vertices: 0 is the h value and 1 is the excess flow.
        public int[][] graph;
        public int[][] capGraph;
        public int[][] flowGraph;

        public Graph(int[][] graphMap) {
            this.numVert = graphMap.length;
            graph = new int[numVert][2];
            capGraph = new int[numVert][numVert];
            flowGraph = new int[numVert][numVert];

            // initialize the vertices
            for (int i = 0; i < numVert; i++) {
                for (int j = 0; j < numVert; j++)
                    if(graphMap[i][j] != 0)
                        addEdge(i, j, graphMap[i][j]);
            }
        }

        public void addEdge(int vertLoc, int destLoc, int capacity) {
            capGraph[vertLoc][destLoc] = capacity;
        }

        // this method basically gets the vertex that overflows
        public int getVertex() {
            for (int i = 1; i < numVert - 1; i++) {
                // check if excess flow is greater than 0
                if (graph[i][g.EXCESS_FLOW_VALUE] > 0) {
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
