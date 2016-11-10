package com.missingmarblesstudio.gameofsheeps.AI;

import com.missingmarblesstudio.gameofsheeps.AI.Actions.AIAction;

import java.util.ArrayList;

/**
 * Created by Vonck-PC on 08/11/2016.
 */
public class AIPriority {
    private ArrayList<AIAction> m_actions = new ArrayList<>();
    private int m_importance;
    private String m_name;

    public AIPriority(String a_name) {
        m_importance = 0;
        m_name = a_name;
    }

    public void AddAction(AIAction a_action) {
        m_actions.add(a_action);
    }

    public ArrayList<AIAction> GetActions() {
        return m_actions;
    }

    public void SetImportance(int a_importance) {
        m_importance = a_importance;
    }

    public int GetImportance() {
        return m_importance;
    }

    public String GetName() {
        return m_name;
    }
}