package model;

import core.Constants;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;


public class Node {

    public Board board;
    public int sum = 0;
    public Node parent;
    public Cell currentCell;
    private Cell[][] cells;
    private int goalValue;
    public int level;       /** in IDS Specify the level of node **/
    private  Hashtable<String, Boolean> repeatedStates;
    public ArrayList<Node> children = new ArrayList<>();    /** in IDS for DFS we need children of nodes so in this array we store current node kids **/
    public double cost;                                    /** in A* specify the cost from start to n (g(n))**/
    public double  distance_to_goal;                        /** in A* specify the  distance from n to goal (h(n)) **/
    public Node(Cell currentCell, int currentValue, int goalValue, Board board, Node parent, Hashtable<String, Boolean> repeated) {
        this.currentCell = currentCell;
        this.sum = currentValue;
        this.board = board;
        this.cells = board.getCells();
        this.parent = parent;
        this.goalValue = goalValue;
        Hashtable<String, Boolean> hashtableTemp = new Hashtable<String, Boolean>(repeated);
        hashtableTemp.put(this.toString(), true);
        this.repeatedStates = hashtableTemp;
        setGoalValue();
    }


    public ArrayList<Node> successor() {            /** i add some commands becuase i need these commands in IDS and A* **/
        ArrayList<Node> result = new ArrayList<Node>();
        if (canMoveRight()) {
            Cell rightCell = this.cells[this.currentCell.i][this.currentCell.j + 1];
            if (isValidMove(rightCell)) {
                int calculatedValue = calculate(rightCell);
                Node rightNode = new Node(rightCell, calculatedValue, goalValue, board, this, repeatedStates);
                rightNode.cost = rightNode.parent.cost + pathCost(rightNode);    /** here **/
                calculate_distance(rightNode);          /** here **/
                result.add(rightNode);
                this.children.add(rightNode);           /** here **/
            }
        }
        if (canMoveLeft()) {
            Cell leftCell = this.cells[this.currentCell.i][this.currentCell.j - 1];
            if (isValidMove(leftCell)) {
                int calculatedValue = calculate(leftCell);
                Node leftNode = new Node(leftCell, calculatedValue, goalValue, board, this, repeatedStates);
                leftNode.cost = leftNode.parent.cost + pathCost(leftNode);
                calculate_distance(leftNode);
                result.add(leftNode);
                this.children.add(leftNode);
            }
        }
        if (canMoveDown()) {
            Cell downCell = this.cells[this.currentCell.i + 1][this.currentCell.j];
            if (isValidMove(downCell)) {
                int calculatedValue = calculate(downCell);
                Node downNode = new Node(downCell, calculatedValue, goalValue, board, this, repeatedStates);
                downNode.cost = downNode.parent.cost + pathCost(downNode);
                calculate_distance(downNode);
                result.add(downNode);
                this.children.add(downNode);
            }

        }
        if (canMoveUp()) {
            Cell upCell = this.cells[this.currentCell.i - 1][this.currentCell.j];
            if (isValidMove(upCell)) {
                int calculatedValue = calculate(upCell);
                Node upNode = new Node(upCell, calculatedValue, goalValue, board, this, repeatedStates);
                upNode.cost = upNode.parent.cost + pathCost(upNode);
                calculate_distance(upNode);
                result.add(upNode);
                this.children.add(upNode);
            }
        }
        return result;
    }
    public ArrayList<Node> inverse_successor( ) {           /** in BDS we need inverse successor for start searching from goal side  **/
        ArrayList<Node> result = new ArrayList<Node>();

        if (canMoveDown()) {
            Cell downCell = this.cells[this.currentCell.i + 1][this.currentCell.j];
            if (isValidMove(downCell)) {
                int calculatedValue = inverse_calculate(this.currentCell);

                Node downNode = new Node(downCell, calculatedValue, goalValue, board, this, repeatedStates);
                result.add(downNode);
            }

        }
        if (canMoveUp()) {
            Cell upCell = this.cells[this.currentCell.i - 1][this.currentCell.j];
            if (isValidMove(upCell)) {
                int calculatedValue = inverse_calculate(this.currentCell);

                Node upNode = new Node(upCell, calculatedValue, goalValue, board, this, repeatedStates);
                result.add(upNode);
            }
        }

        if (canMoveRight()) {
            Cell rightCell = this.cells[this.currentCell.i][this.currentCell.j + 1];

            if (isValidMove(rightCell)) {
                int calculatedValue = inverse_calculate(this.currentCell);

                Node rightNode = new Node(rightCell, calculatedValue, goalValue, board, this, repeatedStates);
                result.add(rightNode);

            }
        }
        if (canMoveLeft()) {
            Cell leftCell = this.cells[this.currentCell.i][this.currentCell.j - 1];
            if (isValidMove(leftCell)) {
                int calculatedValue = inverse_calculate(this.currentCell);

                Node leftNode = new Node(leftCell, calculatedValue, goalValue, board, this, repeatedStates);
                result.add(leftNode);

            }
        }


        return result;
    }

    private int inverse_calculate(Cell cell) {          /** for BDS (search from goal) **/

        return switch (cell.getOperationType()) {
            case MINUS -> sum + cell.getValue();
            case ADD -> sum - cell.getValue();
        //    case POW -> (int) Math.pow(sum, cell.getValue());
            case MULT -> sum / cell.getValue();
            default -> sum;
        };

    }
    private boolean canEnterGoal(Cell downCell) {
        if (downCell != Cell.getGoal()) return true;
        else {
            return sum >= goalValue;
        }
    }


    private int calculate(Cell cell) {

        return switch (cell.getOperationType()) {
            case MINUS -> sum - cell.getValue();
            case ADD -> sum + cell.getValue();
        //    case POW -> (int) Math.pow(sum, cell.getValue());
            case MULT -> sum * cell.getValue();
            default -> sum;
        };

    }
    private void setGoalValue() {
        if (currentCell.getOperationType() == OPERATION_TYPE.DECREASE_GOAL)
            goalValue -= currentCell.getValue();
        if (currentCell.getOperationType() == OPERATION_TYPE.INCREASE_GOAL)
            goalValue += currentCell.getValue();
    }
    private boolean isWall(Cell cell) {
        return cell.getOperationType() == OPERATION_TYPE.WALL;
    }

    private boolean canMoveRight() {
        return this.currentCell.j < this.board.getRow() - 1;
    }

    private boolean canMoveLeft() {
        return this.currentCell.j > 0;
    }

    private boolean canMoveUp() {
        return this.currentCell.i > 0;
    }

    private boolean canMoveDown() {
        return this.currentCell.i < this.board.getCol() - 1;
    }

    private Boolean isValidMove(Cell destCell) {
        return destCell != Cell.getStart() && canEnterGoal(destCell) && !isWall(destCell) && !repeatedStates.containsKey(destCell.toString());
    }

    public boolean isGoal() {
        if (currentCell.getOperationType() == OPERATION_TYPE.GOAL) {
            return sum >= goalValue;
        }
        return false;
    }

    public int pathCost(Node node) {
        return switch (node.currentCell.getOperationType()) {
            case MINUS,INCREASE_GOAL  -> 1;
            case ADD, DECREASE_GOAL -> 2;
            case MULT -> 5;
            case POW -> 11;
            default -> 0;
        };

    }

    public double heuristic() {
        // TODO: 2/16/2022 implement heuristic function
        /** f(n) = g(n) + h(n) **/
        return this.cost + this.distance_to_goal;
    }
    public void  calculate_distance(Node node){  /** claculate distance of each node to goal -h(n)- **/

        node.distance_to_goal = Math.sqrt(Math.pow(node.currentCell.getI()-Cell.getGoal().getI(),2) + Math.pow(node.currentCell.getJ()-Cell.getGoal().getJ(),2));
    }

     public String hash() {
        StringBuilder hash = new StringBuilder();
        hash.append("i:")
                .append(currentCell.i)
                .append("j:")
                .append(currentCell.j)
                .append("sum:")
                .append(sum)
                .append("op:")
                .append(currentCell.op)
                .append("val:")
                .append(currentCell.getValue());

        return hash.toString();
    }

    public void drawState() {

        for (int i = 0; i < board.getRow(); i++) {
            for (int j = 0; j < board.getCol(); j++) {
                if (cells[i][j].getOperationType() == OPERATION_TYPE.GOAL) {
                    System.out.print(Constants.ANSI_BRIGHT_GREEN +
                            OPERATION_TYPE.getOperationTag(cells[i][j].getOperationType())
                            + goalValue + spaceRequired(cells[i][j])
                    );
                    continue;
                }
                if (currentCell.j == j && currentCell.i == i) {
                    System.out.print(Constants.ANSI_BRIGHT_GREEN + Constants.PLAYER + sum + spaceRequired(cells[i][j]));
                } else {
                    System.out.print(Constants.ANSI_BRIGHT_GREEN +
                            OPERATION_TYPE.getOperationTag(cells[i][j].getOperationType())
                            + cells[i][j].getValue() + spaceRequired(cells[i][j])
                    );
                }
            }
            System.out.println();

        }
        System.out.println("-----------------------------------------");

    }

    public ArrayList<Node> getChildren() {
        return children;
    }



    private String spaceRequired(Cell cell) {
        int length = String.valueOf(cell.getValue()).length();
        String result = " ".repeat(5 - length);
        if (cell.op.equals("+") || cell.op.equals("-") || cell.op.equals("*") || cell.op.equals("^")) {
            result += " ";
        }
        return result;
    }

    @Override
    public String toString() {
        return "(" + this.currentCell.i + "," + this.currentCell.j + ")";
    }
}
