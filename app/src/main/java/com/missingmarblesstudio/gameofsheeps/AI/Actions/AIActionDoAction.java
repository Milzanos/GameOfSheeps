package com.missingmarblesstudio.gameofsheeps.AI.Actions;

import com.missingmarblesstudio.gameofsheeps.AI.AIWorld;
import com.missingmarblesstudio.gameofsheeps.Constants;

/**
 * Created by Vonck-PC on 08/11/2016.
 */
public class AIActionDoAction extends AIAction {
    private AIActionMoveTo m_actionMoveTo;
    private int m_value;
    private int m_resourceName;
    private float m_time = 0.0f;
    private float m_totalTime;

    public AIActionDoAction(AIActionMoveTo a_actionMoveTo, int a_resourceName, int a_value, float a_time) {
        m_actionMoveTo = a_actionMoveTo;
        m_resourceName = a_resourceName;
        m_value = a_value;
        m_totalTime = a_time;
    }

    public AIActionDoAction(float a_time) {
        m_value = 0;
        m_resourceName = -1;
        m_totalTime = a_time;
    }

    public int GetResourceName() {
        return m_resourceName;
    }

    public int GetValue() {
        return m_value;
    }

    @Override
    public void Init() {
        m_isDone = false;

        if (m_actionMoveTo != null) {
            AIWorld.IncrementResource(m_actionMoveTo.GetDestination(), m_resourceName, m_value);
            AIWorld.ClaimResource(m_actionMoveTo.GetDestination(), m_resourceName, false);
        }
    }

    @Override
    public void Update() {
        m_time += Constants.DELTA_TIME;

        if (m_time >= m_totalTime)
            m_isDone = true;
    }
}
