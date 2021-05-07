import java.util.ArrayList;

public class Johnson {
    private int numVert;
    public final int EDGES_SRC = 0;
    public final int EDGES_DST = 1;
    public final int EDGES_WEIGHT = 2;
    int[][] modGraph, originalGraph;
    ArrayList<Integer[]> edges;
    public Johnson(int[][] allPairsGraph) {
        originalGraph = allPairsGraph;
        modGraph = allPairsGraph;
        numVert = allPairsGraph.length;
        edges = new ArrayList<>();
        // add every edge to the edges list
        for(int x = 0; x < numVert; x++) {
            for (int y = 0; y < originalGraph[x].length; y++) {
                if(originalGraph[x][y] > 0)
                edges.add(new Integer[] {x,  y, originalGraph[x][y]});
            }
        }
        int[] hEdges = bellmanFordOnModifiedGraph();
        // reweight the edges of modifiedGraph
        for(int i = 0; i < numVert; i ++) {
            for (int j = 0; j < numVert; j++) {
                if (originalGraph[i][j] != 0) {
                    modGraph[i][j] = originalGraph[i][j] + hEdges[i] - hEdges[j];
                }
            }
        }
        for(int[] row : modGraph) {
            for(int num : row) {
                if(num == FloydWarshall.MAX_INT)
                    System.out.print("NF");
                else
                    System.out.print(num);
                System.out.print("\t");
            }
            System.out.println();
        }
        // run dijkstra on each vertex!
        for (int i = 0; i < numVert; i++)
            dijkstra(i) ;



        System.out.println("The distances between each vertex after Johnson's Algorithm");
        // print and get the shortest graph!
        for(int[] row : originalGraph) {
            for(int num : row) {
                if(num == FloydWarshall.MAX_INT)
                    System.out.print("NF");
                else
                    System.out.print(num);
                System.out.print("\t");
            }
            System.out.println();
        }
    }

    private int[] bellmanFordOnModifiedGraph() {
        // initialize array with MAX_INT as the distances
        // at distToVertices[numVert], this is the s we have
        int[] distToVertices = new int[numVert+1];
        // initialize all of distVertices to MAX_INT except the last value
        for(int i = 0; i < distToVertices.length-1; i++) {
            distToVertices[i] = FloydWarshall.MAX_INT;
        }
        // add every vertex to s
        for (int i = 0; i < numVert; i++) {
            edges.add(new Integer[] {numVert, i, 0});
        }
        /*
            Run bellman ford
         */
        for (int i = 0; i < numVert; i++) {
            for (int j = 0; j < edges.size(); j++) {
                Integer[] edge = edges.get(j);
                if ((distToVertices[edge[EDGES_SRC]] != FloydWarshall.MAX_INT) && (distToVertices[edge[EDGES_SRC]] + edge[EDGES_WEIGHT] < distToVertices[edge[EDGES_DST]])) {
                    distToVertices[edge[EDGES_DST]] = distToVertices[edge[EDGES_SRC]] + edge[EDGES_WEIGHT];
                }
            }
        }
        // copy this graph to the modified graph
        return distToVertices;
    }

    private int dijkstra(int src) {
        int[] distList = new int[numVert];
        boolean[] isVisitedSet = new boolean[numVert];

        for(int i = 0; i < numVert; i ++ ) {
            distList[i] = FloydWarshall.MAX_INT;
            isVisitedSet[i] = false;
        }
        // initialize src vert to be 0
        distList[src] = 0;
        for (int i = 0; i < numVert; i++) {
            int currVert = findMinDist(distList, isVisitedSet);
            isVisitedSet[currVert] = true;
            for (int vertex = 0; vertex < numVert; vertex++) {
                if(     (!isVisitedSet[vertex]) &&
                        (distList[vertex] > (distList[vertex] + modGraph[currVert][vertex])) &&
                        (originalGraph[currVert][vertex] != 0)) {
                    distList[vertex] = distList[currVert] + modGraph[currVert][vertex];
                }
            }
            // we finished finding the distant list for this vertex, set it in the original graph
            originalGraph[currVert] = distList;
        }

        return -1;
    }
    private int findMinDist (int[] distList, boolean[] isVisitedSet) {
        int min = FloydWarshall.MAX_INT;
        int minVert = 0;
        for (int i =0; i < distList.length; i ++) {
            if((min > distList[i]) && (!isVisitedSet[i])) {
                min = distList[i];
                minVert = i;
            }
        }


        return minVert;
    }
}
