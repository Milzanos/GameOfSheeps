package com.missingmarblesstudio.gameofsheeps;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Vonck-PC on 08/11/2016.
 */
public class AStar {
    private static ArrayList<Node> m_openNodes = new ArrayList<>();
    private static ArrayList<Node> m_closedNodes = new ArrayList<>();
    private static Node m_endNode;
    private static Node m_currentNode;

    private AStar() {

    }

    public static ArrayList<Point> CalculateRoute(int a_startX, int a_startY, int a_endX, int a_endY) {
        a_startX = Math.max(Math.min(a_startX, Constants.COUNT_WIDTH - 1), 0);
        a_startY = Math.max(Math.min(a_startY, Constants.COUNT_HEIGHT - 1), 0);
        a_endX = Math.max(Math.min(a_endX, Constants.COUNT_WIDTH - 1), 0);
        a_endY = Math.max(Math.min(a_endY, Constants.COUNT_HEIGHT - 1), 0);

        //Initalise A*.
        m_openNodes.clear();
        m_closedNodes.clear();
        m_openNodes.add(Constants.GET_NODE(a_startY, a_startX));
        m_endNode = Constants.GET_NODE(a_endY, a_endX);

        for (int row = 0; row < Constants.COUNT_HEIGHT; row++) {
            for (int col = 0; col < Constants.COUNT_WIDTH; col++) {
                Constants.GET_NODE(row, col).AS_fScore = Integer.MAX_VALUE;
                Constants.GET_NODE(row, col).AS_gScore = Integer.MAX_VALUE;
                Constants.GET_NODE(row, col).AS_parent = null;
            }
        }

        m_openNodes.get(0).AS_gScore = 0;
        m_openNodes.get(0).AS_fScore = ASToEnd(m_openNodes.get(0));

        //Do A*.
        while (m_openNodes.size() > 0) {
            m_currentNode = ASGetClosestNode();
            if (m_currentNode == m_endNode)
                break;

            m_openNodes.remove(m_currentNode);
            m_closedNodes.add(m_currentNode);

            ASEvaluateNeighbors(m_currentNode, Constants.NODE_GROUND);
        }

        //Check if the path was correct. If it was not, find the closest node.
        if (m_currentNode != m_endNode) {
            for (Node it : m_closedNodes)
                if (ASToEnd(it) < ASToEnd(m_currentNode))
                    m_currentNode = it;

            if (Math.abs(m_currentNode.m_x - m_endNode.m_x) + Math.abs(m_currentNode.m_y - m_endNode.m_y) > 2) {
                return new ArrayList<>();
            }
        }

        //Update A* route.
        Node node = m_currentNode;
        ArrayList<Point> route = new ArrayList<>();

        while (node != null) {
            route.add(new Point(node.m_x, node.m_y));
            node = node.AS_parent;
        }

        return route;
    }

    private static int ASToEnd(Node a_node) {
        return (Math.abs((a_node.m_x) - m_endNode.m_x) + Math.abs((a_node.m_y) - m_endNode.m_y) * 10);
    }

    private static Node ASGetClosestNode() {
        Node selected = m_openNodes.get(0);

        for (Node it : m_openNodes)
            if (it.AS_fScore < selected.AS_fScore)
                selected = it;

        return selected;
    }

    private static void ASEvaluateNeighbors(Node a_currentNode, Integer a_type) {
        int newX;
        int newY;
        int score;
        boolean better;

        for (int y = -1; y < 2; y++) {
            for (int x = -1; x < 2; x++) {
                newY = a_currentNode.m_y + y;
                newX = a_currentNode.m_x + x;

                if (newX >= 0 && newY >= 0 && newX < Constants.COUNT_WIDTH && newY < Constants.COUNT_HEIGHT) {
                    if (Constants.GET_NODE(newY, newX).m_area == a_type) {
                        better = true;
                        if (x != 0 && y != 0) {
                            if (Constants.GET_NODE(a_currentNode.m_y, a_currentNode.m_x + x).m_area != Constants.NODE_GROUND ||
                                    Constants.GET_NODE(a_currentNode.m_y + y, a_currentNode.m_x).m_area != Constants.NODE_GROUND)
                                better = false;
                        }
                        if (better) {
                            if (!m_closedNodes.contains(Constants.GET_NODE(newY, newX))) {
                                score = a_currentNode.AS_gScore + Math.abs(x) * 10 + Math.abs(y) * 10 + ((x != 0 && y != 0) ? 4 : 0);
                                better = true;

                                if (!m_openNodes.contains(Constants.GET_NODE(newY, newX)))
                                    m_openNodes.add(Constants.GET_NODE(newY, newX));
                                else if (score < Constants.GET_NODE(newY, newX).AS_gScore)
                                    better = false;

                                if (better) {
                                    Constants.GET_NODE(newY, newX).AS_parent = a_currentNode;
                                    Constants.GET_NODE(newY, newX).AS_gScore = score;
                                    Constants.GET_NODE(newY, newX).AS_fScore = score + ASToEnd(Constants.GET_NODE(newY, newX));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
