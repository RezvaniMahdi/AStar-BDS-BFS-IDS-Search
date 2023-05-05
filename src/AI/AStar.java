package AI;
import model.Node;

import java.util.*;

public class AStar {

    public static double min = Integer.MAX_VALUE;;
    public void search(Node start) {
        System.out.println("\n***********************************\n" +
                "*            A* Search           *\n" +
                "***********************************\n"
        );
        Comparator<Node> compare = new AStarPairComparator();
        PriorityQueue<Node> frontier = new PriorityQueue<Node>(compare);
        PriorityQueue<Node> explored = new PriorityQueue<Node>(compare);
        frontier.add(start);
        start.cost = 0;
        start.calculate_distance(start);
        while (!frontier.isEmpty()) {
            Node temp = frontier.poll();
        //    System.out.println("current parent :" + temp.parent + "\nncurrent node :" + temp+"\ncurrent cost :"+temp.cost+"\ncurrent distace : "+temp.distance_to_goal);
            explored.add(temp);
            if (temp.isGoal()) {
                if (temp.cost < min) {
                    min = temp.cost;
                    printResult(temp,0);
                    System.out.println("total cost :" +temp.cost);
                }
            }
            ArrayList<Node> children = temp.successor();
            for (Node child : children) {
                if (!(frontier.contains(child)) && !(explored.contains(child))) {
                    frontier.add(child);
                }
            }

        }
   //     int fsize = frontier.size();
    //    int esize = explored.size();
    //    for (int k = 0; k < esize;k++){
      //      System.out.println("explored_parent : " + explored.element().parent+"\ncost : "+explored.element().cost+ "\ndistance : " +explored.element().distance_to_goal +"\n exlpored : " + explored.poll() );
      //  }
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
    class AStarPairComparator implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            if (o1.heuristic() > o2.heuristic())return 1;
            if (o1.heuristic() < o2.heuristic())return -1;
            return 0;
        }
    }