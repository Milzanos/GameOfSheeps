package com.missingmarblesstudio.gameofsheeps.AI;

import android.graphics.Point;
import android.util.Pair;

import com.missingmarblesstudio.gameofsheeps.AI.Actions.AIActionDoAction;
import com.missingmarblesstudio.gameofsheeps.Constants;
import com.missingmarblesstudio.gameofsheeps.Node;

import java.util.ArrayList;

/**
 * Created by Vonck-PC on 10/11/2016.
 */
public class AIAreaManager {
    ArrayList<Pair<Integer, ArrayList<AIArea>>> m_areas = new ArrayList();

    public AIAreaManager() {
    }

    public void AddNode(Node a_node) {
        for (Integer resource : a_node.AI_resources) {
            boolean found = false;
            for (Pair<Integer, ArrayList<AIArea>> pair : m_areas) {
                if (pair.first == resource) {
                    AddNodeSub(a_node, pair);
                    found = true;
                }
            }
            if (!found) {
                m_areas.add(new Pair<>(resource, new ArrayList<AIArea>()));
                AddNodeSub(a_node, m_areas.get(m_areas.size() - 1));
            }
        }
    }

    public void Clear() {
        for (Pair<Integer, ArrayList<AIArea>> pair : m_areas) {
            for (AIArea aiArea : pair.second) {
                aiArea.ClearNodes();
            }
        }
    }

    public void CalculateTotalCount() {
        Constants.CLEAR_RESOURCES();
        for (Pair<Integer, ArrayList<AIArea>> pair : m_areas) {
            int totalCount = 0;

            for (AIArea aiArea : pair.second) {
                totalCount += aiArea.GetCount();
            }
            Constants.SET_RESOURCE(pair.first, totalCount);
        }
    }

    public Point GetResourceByValue(Point a_position, Integer a_areaName, boolean a_highestValue) {
        for (Pair<Integer, ArrayList<AIArea>> pair : m_areas) {
            if (HasAreaSub(pair.second, a_areaName)) {
                int value = (a_highestValue) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

                Point point = new Point(0, 0);

                for (AIArea aiArea : pair.second) {
                    if (!aiArea.IsAreaClaimed() && aiArea.IsArea(a_areaName)) {
                        int newValue = aiArea.GetCount();
                        Point newPoint = aiArea.GetClosestNode(a_position);

                        if (Math.sqrt(newPoint.x * newPoint.x + newPoint.y * newPoint.y) < 100) {
                            if (a_highestValue) {
                                if (newValue >= value) {
                                    point = newPoint;
                                    value = newValue;
                                }
                            } else {
                                if (newValue <= value) {
                                    point = newPoint;
                                    value = newValue;
                                }
                            }
                        }
                    }
                }
                return point;
            }
        }
        return new Point(0, 0);
    }

    public boolean CanDoAction(AIActionDoAction a_action) {
        if (Constants.GET_RESOURCE(a_action.GetResourceName()) + a_action.GetValue() < 0)
            return false;

        for (Pair<Integer, ArrayList<AIArea>> pair : m_areas) {
            if (pair.first == a_action.GetResourceName()) {
                boolean possible = false;
                int max = 0;
                for (AIArea aiArea : pair.second) {
                    if (aiArea.GetCount() + a_action.GetValue() >= 0) {
                        if (aiArea.GetCount() > max)
                            max = aiArea.GetCount();
                        possible = true;
                    }
                }
                return possible;
            }
        }
        return true;
    }

    public void IncrementResource(Point a_resourceLocation, Integer a_resourceType, Integer a_value) {
        Node node = Constants.GET_NODE(a_resourceLocation);

        for (Pair<Integer, ArrayList<AIArea>> pair : m_areas) {
            if (pair.first == a_resourceType) {
                for (AIArea aiArea : pair.second) {
                    if (aiArea.GetSet() == node.FF_set) {
                        aiArea.IncrementCount(a_value);
                        return;
                    }
                }
            }
        }
    }

    public void ClaimResource(Point a_resource, int a_resourceName, boolean a_claimed) {
        for (Pair<Integer, ArrayList<AIArea>> pair : m_areas) {
            if (pair.first == a_resourceName) {
                for (AIArea aiArea : pair.second) {
                    if (aiArea.GetSet() == Constants.GET_NODE(a_resource).FF_set) {
                        aiArea.ClaimArea(a_claimed);
                        return;
                    }
                }
            }
        }
    }

    public boolean IsResourceClaimed(Point a_resource, int a_resourceName) {
        for (Pair<Integer, ArrayList<AIArea>> pair : m_areas) {
            if (pair.first == a_resourceName) {
                for (AIArea aiArea : pair.second) {
                    if (aiArea.GetSet() == Constants.GET_NODE(a_resource).FF_set) {
                        return aiArea.IsAreaClaimed();
                    }
                }
            }
        }
        return true;
    }

    public boolean IsResourceClaimed(int a_resourceName) {

        for (Pair<Integer, ArrayList<AIArea>> pair : m_areas) {
            if (pair.first == a_resourceName) {
                boolean claimed = true;
                for (AIArea aiArea : pair.second) {
                    if (!aiArea.IsAreaClaimed()) {
                        claimed = false;
                    }
                }
                return claimed;
            }
        }
        return false;
    }

    //PRIVATE // PRIVATE // PRIVATE // PRIVATE // PRIVATE // PRIVATE // PRIVATE // PRIVATE // PRIVATE // PRIVATE // PRIVATE // PRIVATE

    private void AddNodeSub(Node a_node, Pair<Integer, ArrayList<AIArea>> a_pair) {
        for (AIArea area : a_pair.second) {
            if (area.GetSet() == a_node.FF_set) {
                area.AddNode(a_node);
                return;
            }
        }

        a_pair.second.add(new AIArea(a_node.FF_set, a_node.m_area));
        a_pair.second.get(a_pair.second.size() - 1).AddNode(a_node);
        a_pair.second.get(a_pair.second.size() - 1).SetCount(Constants.GET_DEFAULT_RESOURCE_VALUE(a_node.m_area, a_pair.first));
    }

    private boolean HasAreaSub(ArrayList<AIArea> a_aiAreas, int a_area) {
        for (AIArea aiArea : a_aiAreas) {
            if (aiArea.IsArea(a_area)) {
                return true;
            }
        }
        return false;
    }
}
