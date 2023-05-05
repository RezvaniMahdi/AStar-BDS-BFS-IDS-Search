package core;
import AI.AStar;
import AI.IDS;
import AI.BDS;
import AI.BFS;
;
import model.Board;
import model.Cell;
import model.Node;

import java.util.Hashtable;
import java.util.Scanner;

public class main {


    public static void main(String[] args) {
        System.out.println(" pls enter rows and columns of your board : \n");
        Scanner sc = new Scanner(System.in);
        String mn = sc.nextLine();
        int rows = Integer.parseInt(mn.split(" ")[0]);
        int columns = Integer.parseInt(mn.split(" ")[1]);
        String[][] board = new String[rows][columns];
        String[] lines = new String[rows];
        for (int i = 0; i < rows; i++) {
            lines[i] = sc.nextLine();
            String[] line = lines[i].split(" ");
            if (columns >= 0) System.arraycopy(line, 0, board[i], 0, columns);
        }
        Mapper mapper = new Mapper();
        Cell[][] cells = mapper.createCells(board, rows, columns);
        Board gameBoard = mapper.createBoard(cells, rows, columns);

        Hashtable<String, Boolean> initHash = new Hashtable<>();
        initHash.put(Cell.getStart().toString(), true);
        Hashtable<String, Boolean> initHash_IDS = new Hashtable<>();
        initHash_IDS.put(Cell.getStart().toString(), true);
        initHash_IDS.put(Cell.getGoal().toString(),true);
        Node start = new Node(Cell.getStart(), Cell.getStart().getValue(), Cell.getGoal().getValue(), gameBoard, null, initHash);
        Node goal = new Node(Cell.getGoal(),Cell.getGoal().getValue(), Cell.getStart().getValue(),gameBoard,null,initHash_IDS);

        BDS bds = new BDS();
        IDS ids = new IDS();
        AStar astar = new AStar();
        bds.search(start,goal);
        ids.search(start);
        astar.search(start);
    }
}
