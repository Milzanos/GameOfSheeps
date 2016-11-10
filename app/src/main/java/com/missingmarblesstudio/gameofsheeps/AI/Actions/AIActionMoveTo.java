package com.missingmarblesstudio.gameofsheeps.AI.Actions;

import android.graphics.Point;

import com.missingmarblesstudio.gameofsheeps.AI.AIAgent;
import com.missingmarblesstudio.gameofsheeps.AI.AIWorld;
import com.missingmarblesstudio.gameofsheeps.AStar;
import com.missingmarblesstudio.gameofsheeps.Constants;

import java.util.ArrayList;

/**
 * Created by Vonck-PC on 08/11/2016.
 */
public class AIActionMoveTo extends AIAction {
    ArrayList<Point> m_route = new ArrayList<>();
    private float m_totalTime = 0.0f;
    private Point m_nextNode;

    //Constructor variables.
    private AIAgent m_agent;
    private Point m_destination;
    private Integer m_areaName;
    private boolean m_highestValue;

    public AIActionMoveTo(AIAgent a_agent) {
        m_agent = a_agent;
        m_areaName = -1;
        m_destination = new Point(0, 0);
    }

    public AIActionMoveTo(AIAgent a_agent, Integer a_resourceName, boolean a_highestValue) {
        m_agent = a_agent;
        m_areaName = a_resourceName;
        m_highestValue = a_highestValue;
    }

    public Point GetDestination() {
        return m_destination;
    }

    @Override
    public void Init() {
        m_isDone = false;

        if (m_areaName > 0) {
            m_destination = AIWorld.GetResourceByValue(m_agent.GetGridPosition(), m_areaName, m_highestValue);
        } else {
            boolean found = false;

            while (!found) {
                m_destination.x = Constants.RANDOM.nextInt(Constants.COUNT_WIDTH);
                m_destination.y = Constants.RANDOM.nextInt(Constants.COUNT_HEIGHT);

                if (Constants.GET_NODE(m_destination).m_area == Constants.NODE_GROUND)
                    found = true;
            }
        }

        m_route.clear();
        m_route = AStar.CalculateRoute(m_agent.GetGridPosition().x, m_agent.GetGridPosition().y, m_destination.x, m_destination.y);

        if (m_route.size() <= 0) {
            m_isDone = true;
            return;
        }

        if (m_route.get(m_route.size() - 1).x != m_destination.x || m_route.get(m_route.size() - 1).y != m_destination.y) {
            if (Math.abs(m_route.get(0).x - m_destination.x) + Math.abs(m_route.get(0).y - m_destination.y) < 2)
                ;
                //    m_route.add(0, m_destination);
            else
                m_isDone = true;
        }

        if (m_route.size() > 1)
            m_nextNode = m_route.get(m_route.size() - 2);
    }

    @Override
    public void Update() {
        m_totalTime += Constants.DELTA_TIME;
        if (m_totalTime > 0.5f) {
            m_totalTime -= 0.5f;

            if (m_route.size() > 0)
                m_route.remove(m_route.size() - 1);

            if (m_route.size() > 1)
                m_nextNode = m_route.get(m_route.size() - 2);
            else if (m_route.size() == 1)
                m_nextNode = m_route.get(0);
        }

        if (m_route.size() > 0) {
            Point currentNode = m_route.get(m_route.size() - 1);
            m_agent.SetGridPosition(currentNode);

            if (m_nextNode == null) {
                if (m_route.size() > 1)
                    m_nextNode = m_route.get(m_route.size() - 2);
                else
                    m_nextNode = m_route.get(m_route.size() - 1);
            }

            int size = Constants.SCREEN_HEIGHT / Constants.COUNT_HEIGHT;
            m_agent.SetSmoothPosition(new Point(
                    (int) (currentNode.x * size + ((m_nextNode.x - currentNode.x) * size) * (m_totalTime * 2.0f)),
                    (int) (currentNode.y * size + ((m_nextNode.y - currentNode.y) * size) * (m_totalTime * 2.0f))));
            m_agent.SetDestination(m_destination);
        }


        if (m_destination.x != 0 && m_destination.y != 0) {
            AIWorld.ClaimResource(m_destination, m_areaName, true);
        }

        m_isDone = m_route.isEmpty();
    }

    public boolean CanGo() {
        if (m_areaName > 0) {
            m_destination = new Point(0, 0);
            m_destination = AIWorld.GetResourceByValue(m_agent.GetGridPosition(), m_areaName, m_highestValue);
        } else {
            return true;
        }

        m_route.clear();
        m_route = AStar.CalculateRoute(m_agent.GetGridPosition().x, m_agent.GetGridPosition().y, m_destination.x, m_destination.y);

        if (m_route.size() <= 0) {
            return false;
        }

        if (m_route.get(m_route.size() - 1).x != m_destination.x || m_route.get(m_route.size() - 1).y != m_destination.y) {
            if (!(Math.abs(m_route.get(0).x - m_destination.x) + Math.abs(m_route.get(0).y - m_destination.y) < 2)) {
                return false;
            }
        }

        return true;
    }
}
