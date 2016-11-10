package com.missingmarblesstudio.gameofsheeps;

import java.util.ArrayList;

/**
 * Created by Vonck-PC on 06/11/2016.
 */
public class Node {
    //General.
    public int m_x = 0;
    public int m_y = 0;
    public int m_area = 0;

    //AI.
    public ArrayList<Integer> AI_resources = new ArrayList<>();

    //Flood Fill.
    public int FF_set = 0;

    //A*.
    public int AS_gScore = 0;
    public int AS_fScore = 0;
    public Node AS_parent = null;
}
