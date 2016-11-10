package com.missingmarblesstudio.gameofsheeps.AI;

import android.graphics.Point;

import com.missingmarblesstudio.gameofsheeps.AI.Actions.AIAction;
import com.missingmarblesstudio.gameofsheeps.AI.Actions.AIActionIdle;
import com.missingmarblesstudio.gameofsheeps.Constants;

import java.util.ArrayList;

/**
 * Created by Vonck-PC on 08/11/2016.
 */
public class AIAgent {
    protected String m_priorityName;
    private ArrayList<AIAction> m_actions = new ArrayList<>();
    private ArrayList<AIPriority> m_priorities = new ArrayList<>();
    private Point m_gridPosition;
    private Point m_smoothPosition;
    private Point m_destination;

    public AIAgent(Point a_gridPosition) {
        m_gridPosition = a_gridPosition;

        int size = Constants.SCREEN_HEIGHT / Constants.COUNT_HEIGHT;
        m_smoothPosition = new Point(a_gridPosition.x * size, a_gridPosition.y * size);
        m_destination = new Point(0, 0);

        m_actions.add(new AIActionIdle(this));
    }

    public void Update(float a_deltaTime) {
        if (!m_actions.isEmpty()) {
            m_actions.get(0).Update(a_deltaTime);

            if (m_actions.get(0).IsDone()) {
                m_actions.remove(m_actions.get(0));
                if (m_actions.size() > 0)
                    m_actions.get(0).Init();
            }
        } else {
            m_actions.add(new AIActionIdle(this));
        }
    }

    public Point GetGridPosition() {
        return m_gridPosition;
    }

    public Point GetSmoothPosition() {
        return m_smoothPosition;
    }

    public Point GetDestination() {
        return m_destination;
    }

    public void SetDestination(Point a_destination) {
        m_destination = a_destination;
    }

    public void SetGridPosition(Point a_gridPosition) {
        m_gridPosition = a_gridPosition;
    }

    public void SetSmoothPosition(Point a_smoothPosition) {
        m_smoothPosition = a_smoothPosition;
    }

    public void AddPriority(AIPriority a_priority) {
        m_priorities.add(a_priority);
    }

    public ArrayList<AIPriority> GetPriorities() {
        return m_priorities;
    }

    public void AddAction(AIAction a_action) {
        m_actions.add(a_action);
    }

    public void SetPriority(AIPriority a_priority) {
        for (AIAction it : a_priority.GetActions()) {
            m_actions.add(it);
        }

        m_priorityName = a_priority.GetName();
    }
}