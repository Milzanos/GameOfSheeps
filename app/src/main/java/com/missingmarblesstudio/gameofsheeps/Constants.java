package com.missingmarblesstudio.gameofsheeps;

import android.graphics.Point;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Vonck-PC on 06/11/2016.
 */
public class Constants {
    public static final int NODE_GROUND = 1;
    public static final int NODE_WALL = 2;
    public static final int NODE_FOOD = 3;
    public static final int NODE_SHEEP_PEN = 4;
    public static final int RESOURCE_FOOD = 5;
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static float DELTA_TIME;
    public static Random RANDOM = new Random();
    public static int COUNT_WIDTH;
    public static int COUNT_HEIGHT;
    private static ArrayList<Pair<Integer, Integer>> RESOURCES = new ArrayList<>();
    private static ArrayList<Pair<Integer, ArrayList<Pair<Integer, Integer>>>> DEFAULT_RESOURCE_VALUE = new ArrayList<>();
    private static ArrayList<Pair<Integer, ArrayList<Integer>>> DEFAULT_RESOURCES = new ArrayList<>();
    private static Node GRID[][];
    private static Node NO_VALUE;

    public static void INIT_GRID() {
        GRID = new Node[COUNT_HEIGHT][COUNT_WIDTH];

        for (int row = 0; row < COUNT_HEIGHT; row++) {
            for (int col = 0; col < COUNT_WIDTH; col++) {
                GRID[row][col] = new Node();
                GRID[row][col].m_x = col;
                GRID[row][col].m_y = row;
            }
        }

        NO_VALUE = new Node();
        NO_VALUE.m_x = -1;
        NO_VALUE.m_y = -1;
        NO_VALUE.m_area = -1;
        NO_VALUE.FF_set = -1;
    }

    public static Node[][] GET_GRID() {
        return GRID;
    }

    public static Node GET_NODE(Point a_position) {
        if (a_position.x >= 0 && a_position.x < COUNT_WIDTH &&
                a_position.y >= 0 && a_position.y < COUNT_HEIGHT)
            return GRID[a_position.y][a_position.x];
        else
            return NO_VALUE;
    }

    public static Node GET_NODE(int a_y, int a_x) {
        if (a_x >= 0 && a_x < COUNT_WIDTH && a_y >= 0 && a_y < COUNT_HEIGHT)
            return GRID[a_y][a_x];
        else
            return NO_VALUE;
    }

    public static void SET_NODE(Point a_position, int a_type) {
        Node node = GET_NODE(a_position);

        node.m_area = a_type;
        node.AI_resources = GET_DEFAULT_RESOURCES(a_type);
    }

    public static void SET_NODE(int a_y, int a_x, int a_type) {
        Node node = GRID[a_y][a_x];

        node.m_area = a_type;
        node.AI_resources = GET_DEFAULT_RESOURCES(a_type);
    }

    public static ArrayList<Integer> GET_DEFAULT_RESOURCES(int a_type) {
        for (Pair<Integer, ArrayList<Integer>> resources : DEFAULT_RESOURCES)
            if (resources.first == a_type)
                return resources.second;
        return new ArrayList<>();
    }

    public static void ADD_DEFAULT_RESOURCE(int a_type, int a_resource) {
        boolean found = false;
        for (Pair<Integer, ArrayList<Integer>> resources : DEFAULT_RESOURCES) {
            if (resources.first == a_type) {
                resources.second.add(a_resource);
                found = true;
            }
        }

        if (!found) {
            DEFAULT_RESOURCES.add(new Pair<>(a_type, new ArrayList<Integer>()));
            DEFAULT_RESOURCES.get(DEFAULT_RESOURCES.size() - 1).second.add(a_resource);
        }
    }

    public static int GET_RESOURCE(int a_resourceName) {
        for (Pair<Integer, Integer> it : RESOURCES) {
            if (it.first == a_resourceName) {
                return it.second;
            }
        }

        return 0;
    }

    public static void SET_RESOURCE(int a_resourceName, int a_value) {
        RESOURCES.add(new Pair<>(a_resourceName, a_value));
    }

    public static void CLEAR_RESOURCES() {
        RESOURCES.clear();
    }

    public static int GET_DEFAULT_RESOURCE_VALUE(int a_nodeName, int a_resourceName) {
        for (Pair<Integer, ArrayList<Pair<Integer, Integer>>> it : DEFAULT_RESOURCE_VALUE) {
            if (it.first == a_nodeName) {
                for (Pair<Integer, Integer> resources : it.second) {
                    if (resources.first == a_resourceName) {
                        return resources.second;
                    }
                }
            }
        }

        return 0;
    }

    public static void SET_DEFAULT_RESOURCE_VALUE(int a_nodeName, int a_resourceName, int a_value) {
        boolean found = false;

        for (Pair<Integer, ArrayList<Pair<Integer, Integer>>> it : DEFAULT_RESOURCE_VALUE) {
            if (it.first == a_resourceName) {
                it.second.add(new Pair<>(a_resourceName, a_value));
                found = true;
            }
        }

        if (!found) {
            DEFAULT_RESOURCE_VALUE.add(new Pair<>(a_nodeName, new ArrayList<Pair<Integer, Integer>>()));
            DEFAULT_RESOURCE_VALUE.get(DEFAULT_RESOURCE_VALUE.size() - 1).second.add(new Pair<>(a_resourceName, a_value));
        }
    }
}
