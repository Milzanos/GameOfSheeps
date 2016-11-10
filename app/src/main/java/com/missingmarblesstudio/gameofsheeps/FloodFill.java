package com.missingmarblesstudio.gameofsheeps;

import com.missingmarblesstudio.gameofsheeps.AI.AIWorld;

/**
 * Created by Vonck-PC on 08/11/2016.
 */
public class FloodFill {

    private static int m_set;
    private static boolean m_worked;

    private FloodFill() {

    }

    public static int GetSetCount() {
        return m_set + 1;
    }

    public static void RunFloodFill() {
        //Reset the flood fill.
        AIWorld.ResetResources();
        for (int row = 0; row < Constants.COUNT_HEIGHT; row++) {
            for (int col = 0; col < Constants.COUNT_WIDTH; col++) {
                Constants.GET_NODE(row, col).FF_set = 0;
            }
        }

        //Run the flood fill.
        m_set = 1;
        for (int row = 0; row < Constants.COUNT_HEIGHT; row++) {
            for (int col = 0; col < Constants.COUNT_WIDTH; col++) {

                m_worked = false;
                CheckTile(row, col, Constants.GET_NODE(row, col).m_area, m_set);
                if (m_worked)
                    m_set++;
            }
        }

        System.out.println(m_set);
    }

    private static boolean CheckTile(int a_row, int a_col, Integer a_target, int a_set) {
        if (a_row < 0 || a_row > Constants.COUNT_HEIGHT)
            return false;
        if (a_col < 0 || a_col > Constants.COUNT_WIDTH)
            return false;
        if (Constants.GET_NODE(a_row, a_col).m_area != a_target)
            return false;
        if (Constants.GET_NODE(a_row, a_col).FF_set != 0)
            return false;

        Constants.GET_NODE(a_row, a_col).FF_set = a_set;
        AIWorld.AddResource(Constants.GET_NODE(a_row, a_col));
        m_worked = true;

        if (a_row + 1 < Constants.COUNT_HEIGHT)
            CheckTile(a_row + 1, a_col, a_target, a_set);
        if (a_row - 1 >= 0)
            CheckTile(a_row - 1, a_col, a_target, a_set);
        if (a_col + 1 < Constants.COUNT_WIDTH)
            CheckTile(a_row, a_col + 1, a_target, a_set);
        if (a_col - 1 >= 0)
            CheckTile(a_row, a_col - 1, a_target, a_set);

        return true;
    }
}
