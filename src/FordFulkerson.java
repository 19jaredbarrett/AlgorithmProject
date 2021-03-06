import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
// EDMONDS KARP IMPLEMENTATION!!!
public class FordFulkerson {
    // this will store the path of the augmenting path
    // determined by breadth first search
    private int[] augPath;
    private int numVert;
    int[][] flowGraph;
    public FordFulkerson () {
        // graph used: https://www.hackerearth.com/practice/algorithms/graphs/maximum-flow/tutorial/
        int[][] graphTest
                = {{0, 10, 0, 8, 0, 0}, {0, 0, 5, 2, 0, 0},
                {0, 0, 0, 0, 0, 7}, {0, 0, 0, 0, 10, 0},
                {0, 0, 8, 0, 0, 10}, {0, 0, 0, 0, 0, 0}};
        numVert = graphTest.length;
        augPath = new int[numVert];
        flowGraph = new int[numVert][numVert];
        int flow = fordFulkerson(graphTest, 0, 5) ;
        System.out.println("Ford-Fulkerson Flow Found: " + flow);

    }
    public int fordFulkerson(int[][] graph, int src, int dst) {


        // these variables are used to keep track of the vertex/edges
        int u, v;
        // initialize our flow graph with all of the vertices
        for(u = 0; u < numVert; u++) {
            for (v = 0; v < numVert; v++)
                flowGraph[u][v] = graph[u][v];
        }
        // stores the amount of flow we have
        int numFlow = 0;

        // while there is a path determined by breadth first search,
        //
        while(breadthFirstSearch(src, dst)) {
            // set it to a large number initially
            int currPathFlow = FloydWarshall.MAX_INT;

            // get the max flow from augpath
            for ( v = dst; v != src;  v = augPath[v]) {
                u = augPath[v];
                currPathFlow = Math.min(currPathFlow, flowGraph[u][v]);
            }
            // update capacity, meaning add or subtract from currflow
            for (v = dst; v != src; v = augPath[v]) {
                u = augPath[v];
                flowGraph[u][v] -= currPathFlow;
                flowGraph[v][u] += currPathFlow;
            }
            numFlow += currPathFlow;
        }
        return numFlow;
    }
    private boolean breadthFirstSearch(int src, int dst) {
        boolean[] visited = new boolean[numVert];
        // aug path stores the path
        Queue<Integer> queue = new LinkedList<Integer>();

        // add src to visited and queue it!
        visited[src] = true;
        queue.add(src);
        augPath[src] = -1;

        // this while loop finds the augmenting path to the destination
        while(queue.size() != 0) {
            int currVert = queue.poll();

            for (int v = 0; v < numVert; v++){
                // only if this vertex hasn't been visited yet and the residual graph for this is greater than 0 (not src)
                if(!visited[v] && flowGraph[currVert][v] > 0) {
                    // we found the destination, v is equal to the destination!
                    if (v == dst) {
                        // set augPath to currVert to describe the path
                        augPath[v] = currVert;
                        return true;
                    }
                    // add this vertex to our path!
                    augPath[v] = currVert;
                    // set visited to true
                    visited[v] = true;
                    queue.add(v);

                }
            }
        }
        return false;
    }
}
