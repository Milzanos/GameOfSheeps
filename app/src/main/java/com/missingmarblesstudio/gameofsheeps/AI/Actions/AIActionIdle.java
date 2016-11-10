package com.missingmarblesstudio.gameofsheeps.AI.Actions;

import com.missingmarblesstudio.gameofsheeps.AI.AIAgent;
import com.missingmarblesstudio.gameofsheeps.AI.AIPriority;
import com.missingmarblesstudio.gameofsheeps.AI.AIWorld;

import java.util.ArrayList;

/**
 * Created by Vonck-PC on 08/11/2016.
 */
public class AIActionIdle extends AIAction {
    private AIAgent m_agent;

    public AIActionIdle(AIAgent a_agent) {
        m_agent = a_agent;
    }

    @Override
    public void Init() {

    }

    @Override
    public void Update() {
        ArrayList<AIPriority> priorities = m_agent.GetPriorities();

        AIPriority priority = null;

        for (AIPriority it : priorities)
            if (it.GetImportance() > ((priority == null) ? 0 : priority.GetImportance()) && AIWorld.CanDoAction(it.GetActions()))
                priority = it;

        if (priority != null)
            m_agent.SetPriority(priority);

        m_isDone = true;
    }
}
