import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class AStarAlgorithm {
    public int numRows;
    public int numCols;
    // cost of moving diagonally in the grid.
    public static final int DIAGONAL_COST = 15;
    public static final int ADJACENT_COST = 10;
    private static final int BLOCKED_CELL_X = 0;
    private static final int BLOCKED_CELL_Y = 1;
    // horizontal cost for moving left or right
    private Cell[][] grid;
    // this priority queue is for all open cells that
    public Queue<Cell> openCells;
    // closed cells is all of the cells that are not available
    public boolean[][] closedCells;
    // this represents the start and end locations for the algorithm
    private int startX, startY, endX, endY;

    /*
        The implementation of this algorithm is used for pathfinding
        in video games
        inputBlockedCells example: at row 0: BLOCKED_CELL_X = 5, BLOCKED_CELL_Y = 4. This location will be blocked from traversing to
     */
    public AStarAlgorithm () {
        int[][] pathBlocks = new int[][] {
                {0, 4},
                {2,2},
                {3, 1},
                {3,3},
                {2, 1},
                {2, 3},
                {5, 6},
                {3, 5},
                {6, 7},
                {5, 2},
                {7, 2},
                {7, 4}
        };
        initAStarAlgorithm(8, 8, 0, 0, 7, 7, pathBlocks);
        displayGrid();

        runAlgorithm();
        System.out.println("Algorithm Running...");
        displayScores();
        displayPath();
    }
    public void initAStarAlgorithm (int rows, int cols, int startX, int startY, int endX, int endY, int[][] inputBlockedCells) {
        numRows = rows;
        numCols = cols;
        grid = new  Cell[rows][cols];
        closedCells = new boolean[rows][cols];
        openCells = new PriorityQueue<Cell>(new CellComparator());

        initStartCell(startX, startY);
        initEndCell(endX, endY);
        // initialize the cells and heuristic
        for (int x = 0; x < numRows; x++) {
            for(int y = 0; y < numCols; y++) {
                grid[x][y] = new Cell(x, y);
                // this heurisitc is taking the horizontal distance and the vertical y distance
                grid[x][y].heuristic = Math.abs(x - endX) + Math.abs(y - endY);
                grid[x][y].isSolutionPath = false;
            }
        }
        // initialize starting vertex cost to be 0
        grid[startX][startY].cost = 0;
        // initialize blocked cells, cells we cannot move to
        for(int i = 0; i < inputBlockedCells.length; i++)
            addBlockedCell(inputBlockedCells[i][BLOCKED_CELL_X], inputBlockedCells[i][BLOCKED_CELL_Y]);
    }

    private void initStartCell(int x, int y) {
        this.startX = x;
        this.endX = x;
    }
    private void initEndCell(int x, int y) {
        this.endX = x;
        this.endY = y;
    }
    /*
        This method makes the cell with the location x and y equal to null
        meaning we are unable to reach this cell (it is blocked)
     */
    private void addBlockedCell(int x, int y) {
        grid[x][y] = null;
    }
    /*
        Update cell cost only if it is not null and it is not a closed cell
     */
    private void updateCost(Cell currCell, Cell dst, int cost) {
        if (dst == null || closedCells[dst.x][dst.y])
            return;

        int dstFinalCost = dst.heuristic + cost;
        // only if the cell is not open and the cinal cost is less than the destination final cost
        if(!openCells.contains(dst) || dstFinalCost < dst.cost) {
            dst.cost = dstFinalCost;
            dst.cellParent = currCell;
            // add the destination to open cells, since it is currently not open
            if(!openCells.contains(dst))
                openCells.add(dst);
        }

    }
    private void runAlgorithm() {
        openCells.add(grid[startX][startY]);
        Cell currCell;
        while(true) {
            currCell = openCells.poll();
            if (currCell == null)
                break;
            closedCells[currCell.x][currCell.y] = true;

            if(currCell.equals(grid[endX][endY]))
                return;
            Cell dst;
            // try moving up in the grid
            // also try moving diagonally left or right while moving up
            if (currCell.x - 1 >= 0) {
                // first we update if we are able to move horizontally faster
                dst  = grid[currCell.x-1][currCell.y];
                updateCost(currCell, dst, currCell.cost + ADJACENT_COST);
                // then, we try moving diagonally to the left
                if (currCell.y - 1 >= 0) {
                    dst = grid[currCell.x - 1][currCell.y - 1];
                    updateCost(currCell, dst, currCell.cost + DIAGONAL_COST);
                }
                // try moving diagonally to the right
                if (currCell.y + 1 < grid[0].length) {
                    dst = grid[currCell.x - 1][currCell.y + 1];
                    updateCost(currCell, dst, currCell.cost + DIAGONAL_COST);
                }
            }
            // see if moving left is faster
            if (currCell.y - 1 >= 0 ) {
                dst = grid[currCell.x][currCell.y - 1];
                updateCost(currCell, dst, currCell.cost + ADJACENT_COST);
            }
            // see if moving right is faster
            if (currCell.y + 1 < grid[0].length) {
                dst = grid[currCell.x][currCell.y + 1];
                updateCost(currCell, dst, currCell.cost + ADJACENT_COST);
            }
            // handle moving downward in the grid
            if(currCell.x + 1 < grid.length) {
                dst = grid[currCell.x + 1][currCell.y];
                updateCost(currCell, dst, currCell.cost + ADJACENT_COST);
                // handle diagonal down and to the left
                if (currCell.y - 1 >= 0) {
                    dst = grid  [currCell.x + 1][currCell.y - 1];
                    updateCost(currCell, dst, currCell.cost + DIAGONAL_COST);
                }
                if (currCell.y + 1 < grid[0].length) {
                    dst = grid  [currCell.x + 1][currCell.y +1];
                    updateCost(currCell, dst, currCell.cost + DIAGONAL_COST);
                }
            } // end handle downward

        }
    }
    private void displayGrid() {
        System.out.print("Cost to adjacent cell: 10, Cost to diagonal: 15;\t");
        System.out.println("What grid looks like: ");
        for(int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (i == startX && j == startY)
                    System.out.print("SRC  ");
                else if (i == endX && j == endY)
                    System.out.print("DST  ");
                else if (grid[i][j] != null)
                    System.out.printf("%-4d ", 0);
                else
                    System.out.print("BLC  ");
            }
            System.out.println();
        }

    }
    private void displayScores () {
        System.out.println("Cost to get to each cell: ");
        for(int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != null)
                    System.out.printf("%-4d ", grid[i][j].cost);
                else
                    System.out.print("BLC  ");
            }
            System.out.println();
        }
        System.out.println();

    }
    private void displayPath() {
        if(closedCells[endX][endY]) {
            System.out.println("Optimized Path: ");
            Cell currCell = grid[endX][endY];
            System.out.print(" x: "+ currCell.x + " y: "+currCell.y);
            grid[currCell.x][currCell.y].isSolutionPath = true;
            while (currCell.cellParent != null) {
                System.out.print(" <---" + " x: " + currCell.x + " y: "+currCell.y);
                grid[currCell.cellParent.x][currCell.cellParent.y].isSolutionPath = true;
                currCell = currCell.cellParent;
            }
            System.out.println(" <---" + " x: " + startX + " y: "+startY);
            System.out.println("Path displayed on grid: ");
            for(int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (i == startX && j == startY)
                        System.out.print("SRC  ");
                    else if (i == endX && j == endY)
                        System.out.print("DST  ");
                    else if (grid[i][j] != null)
                        System.out.printf("%-4s ", (grid[i][j].isSolutionPath ? "X" : "0"));
                    else
                        System.out.print("BLC  ");
                }
                System.out.println();
            }
            System.out.println();
        } else {
            System.out.println("path not found");
        }
    }

    private class CellComparator implements Comparator<Cell> {
        /**
         * This method is used in the priority queue to ensure that we are checking cells right
         * @param o1 first cell we are comparing
         * @param o2 second cell we are comparing
         * @return -1, it is less, 1 it is greater, 0 they are equal
         */
        @Override
        public int compare(Cell o1, Cell o2) {
            if (o1.cost < o2.cost)
                return -1;
            else if (o1.cost > o2.cost)
                return 1;
            else return 0;
        }
    }
    private class Cell {
        // represents the x and y coordinates
        public int x, y;
        public Cell cellParent;
        public int heuristic;
        public int cost;
        // whether this cell is part of solution
        public boolean isSolutionPath;
        public Cell (int x, int y) {
            this.x = x;
            this.y = y;
        }

    }
}
