package com.missingmarblesstudio.gameofsheeps.AI;

import android.graphics.Point;

import com.missingmarblesstudio.gameofsheeps.AI.Actions.AIAction;
import com.missingmarblesstudio.gameofsheeps.AI.Actions.AIActionDoAction;
import com.missingmarblesstudio.gameofsheeps.AI.Actions.AIActionMoveTo;
import com.missingmarblesstudio.gameofsheeps.Node;

import java.util.ArrayList;

/**
 * Created by Vonck-PC on 08/11/2016.
 */
public class AIWorld {

    private static AIAreaManager m_areas = new AIAreaManager();

    private AIWorld() {

    }

    public static void ResetResources() {
        m_areas.Clear();
    }

    public static void AddResource(Node a_node) {
        m_areas.AddNode(a_node);
    }

    public static boolean CanDoAction(ArrayList<AIAction> a_actions) {

        boolean returnAction = true;

        for (AIAction it : a_actions)
            if (it instanceof AIActionMoveTo) {
                AIActionMoveTo action = (AIActionMoveTo) it;

                if (!action.CanGo())
                    returnAction = false;
            } else if (it instanceof AIActionDoAction) {
                AIActionDoAction action = (AIActionDoAction) it;

                if (IsResourceClaimed(action.GetResourceName()))
                    returnAction = false;
                if (!m_areas.CanDoAction(action))
                    returnAction = false;
            }

        return returnAction;
    }

    public static void CalculateTotalCount() {
        m_areas.CalculateTotalCount();
    }

    public static void IncrementResource(Point a_resourceLocation, Integer a_resourceType, Integer a_value) {
        m_areas.IncrementResource(a_resourceLocation, a_resourceType, a_value);
    }

    public static Point GetResourceByValue(Point a_position, Integer a_areaName, boolean a_highestValue) {
        return m_areas.GetResourceByValue(a_position, a_areaName, a_highestValue);
    }

    public static void ClaimResource(Point a_resource, int a_resourceName, boolean a_claimed) {
        m_areas.ClaimResource(a_resource, a_resourceName, a_claimed);
    }

    public static boolean IsResourceClaimed(Point a_resource, int a_resourceName) {
        return m_areas.IsResourceClaimed(a_resource, a_resourceName);
    }

    public static boolean IsResourceClaimed(int a_resourceName) {
        return m_areas.IsResourceClaimed(a_resourceName);
    }
}
