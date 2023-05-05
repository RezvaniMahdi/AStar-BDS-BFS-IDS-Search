package AI;

import model.Node;                          /** BDS completed **/

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public class BDS {
    Node a;
    Node b;
    public void search(Node startNode , Node goalNode) {
        System.out.println("***********************************\n" +
                "*            BDS Search           *\n" +
                "***********************************\n"
        );                                                          /** we need two queue for start searching one of them
                                                                         for start search from start point and another for
                                                                           start searching from goal point **/
        Queue<Node> frontier_start = new LinkedList<Node>();
        Hashtable<String, Boolean> inFrontier_start = new Hashtable<>();
        Hashtable<String, Boolean> explored_start = new Hashtable<>();

        Queue<Node> frontier_goal = new LinkedList<Node>();
        Hashtable<String, Boolean> inFrontier_goal = new Hashtable<>();
        Hashtable<String, Boolean> explored_goal = new Hashtable<>();

        if (startNode.isGoal()) {
            System.out.println("score : " + startNode.sum);
            printResult(startNode, 0);
            return;
        }

        frontier_start.add(startNode);
        inFrontier_start.put(startNode.hash(), true);

        frontier_goal.add(goalNode);
        inFrontier_goal.put(goalNode.hash(), true);


        while (!frontier_start.isEmpty() && !frontier_goal.isEmpty()) {     /** this is the main loop for searching **/
            Node temp_s = frontier_start.poll();
            Node temp_g = frontier_goal.poll();

            inFrontier_start.remove(temp_s.hash());
            inFrontier_goal.remove(temp_g.hash());


            explored_start.put(temp_s.hash(), true);
            explored_goal.put(temp_g.hash(), true);


            ArrayList<Node> children_s = temp_s.successor();
            ArrayList<Node> children_g = temp_g.inverse_successor();

            for (Node child :children_s){

                if (!(inFrontier_start.containsKey(child.hash())) && !(explored_start.containsKey(child.hash()))) {
                    frontier_start.add(child);
                    inFrontier_start.put(child.hash(), true);
                }
            }

            if (check_FriegeLists(frontier_start,frontier_goal)){       /** here we check that two fringe are see each other or no **/

                System.out.println("points from start side :" + b.sum);
                System.out.println("points from goal side  :" + a.sum);
                printResult(a,0);
                printResult(b,0);
                break;
            }
            for (Node child : children_g){
                if (!(inFrontier_goal.containsKey(child.hash())) && !(explored_goal.containsKey(child.hash()))) {
                    frontier_goal.add(child);
                    inFrontier_goal.put(child.hash(), true);
                }
            }

            if (check_FriegeLists(frontier_start,frontier_goal)){           /** here we checked again becuase in BDS after open each node we check**/

                System.out.println("points start :" + b.sum);
                System.out.println("points goal  :" + a.sum);
                printResult(a,0);
                printResult(b,0);
                break;
            }

        }

    }

    public boolean check_FriegeLists(Queue<Node>  friege_start, Queue<Node>  friege_goal){      /** function for checking fring lists **/

        boolean result = false;
        Queue<Node> frontier_start = new LinkedList<Node>();
        Queue<Node> frontier_goal = new LinkedList<Node>();
        int size_start = friege_start.size();
        int size_goal = friege_goal.size();
        for (int i = 0; i < size_goal;i++){

            Node temp = friege_goal.element();

            for (int j = 0; j < size_start;j++){
                Node temp2 = friege_start.element();

                if (temp.currentCell.getI() == temp2.currentCell.getI() && temp.currentCell.getJ() == temp2.currentCell.getJ()){

                    if (temp2.sum >= temp.sum){
                        result = true;
                        a = temp;
                        b = temp2;
                    }
                }
                frontier_start.add(temp2);
                friege_start.remove();
            }
            frontier_goal.add(temp);
            friege_goal.remove();
            for (int k = 0; k < size_start;k++){
                friege_start.add(frontier_start.poll());
            }
        }

        for (int l = 0; l < size_goal; l++){
            friege_goal.add(frontier_goal.poll());
        }

        return result;
    }


    public void printResult(Node node, int depthCounter) {
        if (node.parent == null) {
            System.out.println("problem solved at a depth of  : " + depthCounter);
            return;
        }

        System.out.println(node.toString());
        node.drawState();
        printResult(node.parent, depthCounter + 1);
    }

}
