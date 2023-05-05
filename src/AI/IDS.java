package AI;

import model.Node;                                          /** IDS completed **/

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public class IDS {
    Node a;
    public void search(Node startNode){
        System.out.println("\n***********************************\n" +
                "*            IDS Search           *\n" +
                "***********************************\n"
        );
        Queue<Node> frontier = new LinkedList<Node>();
        Hashtable<String, Boolean> inFrontier = new Hashtable<>();
        Hashtable<String, Boolean> explored = new Hashtable<>();

        if (startNode.isGoal()) {
            System.out.println("score : " + startNode.sum);
            printResult(startNode, 0);
            return;
        }
        startNode.level = 0;
        frontier.add(startNode);
        inFrontier.put(startNode.hash(), true);
        int count = 0;
        while (!frontier.isEmpty()) {                       /** make tree with certain deph with BF **/

            Node current_node = frontier.poll();
            inFrontier.remove(current_node.hash());
            explored.put(current_node.hash(), true);
            ArrayList<Node> children = current_node.successor();
            for (Node child : children) {
                if (!(inFrontier.containsKey(child.hash())) && !(explored.containsKey(child.hash()))) {
                    child.level = current_node.level+1;
                    frontier.add(child);
                    inFrontier.put(child.hash(), true);
                }
            }
            if (!frontier.isEmpty()){

                if (current_node.level != frontier.element().level){                /** check a level of tree is complete or ni **/

                    if (_DFS(startNode, current_node.level+1)){            /** search in tree for goal with DFS **/
                        printResult(a, 0);
                        System.out.println("total points : " +a.sum);
                        break;
                    }
                }
            }
            count++;


        }
    }

    public boolean _DFS(Node root, int deph_limit){         /** DFS limited deph **/
        ArrayList<Node> children = root.getChildren();
        if (root.isGoal()){
            a = root;
            return true;
        }
        if (deph_limit == 0){
            return false;
        }
        for (Node child : children){

            if (_DFS(child, deph_limit - 1)){
                return true;
            }
        }
    return false;
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
