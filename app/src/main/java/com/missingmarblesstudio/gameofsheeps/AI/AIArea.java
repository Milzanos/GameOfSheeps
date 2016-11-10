package com.missingmarblesstudio.gameofsheeps.AI;

import android.graphics.Point;

import com.missingmarblesstudio.gameofsheeps.Node;

import java.util.ArrayList;

/**
 * Created by Vonck-PC on 10/11/2016.
 */
public class AIArea {
    private ArrayList<Node> m_nodes = new ArrayList<>();
    private int FF_set;
    private int m_area;
    private int m_count;
    private boolean m_isClaimed;

    public AIArea(int a_FFSet, int a_area) {
        FF_set = a_FFSet;
        m_area = a_area;
        m_count = 0;
        m_isClaimed = false;
    }

    public void AddNode(Node a_node) {
        m_nodes.add(a_node);
    }

    public void ClearNodes() {
        m_nodes.clear();
    }

    public Point GetClosestNode(Point a_position) {
        Point closest = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        float distance = Float.MAX_VALUE;

        for (Node node : m_nodes) {
            float dx = node.m_x - a_position.x;
            float dy = node.m_y - a_position.y;
            float newDistance = dx * dx + dy * dy;
            if (newDistance < distance) {
                distance = newDistance;
                closest.x = node.m_x;
                closest.y = node.m_y;
            }
        }

        return closest;
    }

    public void IncrementCount(int a_addition) {
        m_count += a_addition;
    }

    //GETTERS // GETTERS // GETTERS // GETTERS // GETTERS // GETTERS // GETTERS // GETTERS // GETTERS // GETTERS // GETTERS

    public int GetSet() {
        return FF_set;
    }

    public int GetCount() {
        return m_count;
    }

    public boolean IsAreaClaimed() {
        return m_isClaimed;
    }

    public boolean IsArea(int a_area) {
        return (m_area == a_area);
    }

    //SETTERS // SETTERS // SETTERS // SETTERS // SETTERS // SETTERS // SETTERS // SETTERS // SETTERS // SETTERS // SETTERS

    public void SetCount(int a_count) {
        m_count = a_count;
    }

    public void ClaimArea(boolean a_isClaimed) {
        m_isClaimed = a_isClaimed;
    }
}
