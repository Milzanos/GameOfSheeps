package com.missingmarblesstudio.gameofsheeps;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Vonck-PC on 06/11/2016.
 */
public class World {
    int m_types[][];
    int m_floodFill[][];
    Node m_aStar[][];
    Node m_currentNode;
    Node m_endNode;
    Node m_beginNode;
    int m_countWidth;
    int m_countHeight;
    int m_colors[] = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.BLACK};
    ArrayList<Integer> m_randomColors = new ArrayList<>();


    public World(int a_countWidth, int a_countHeight) {
        m_countWidth  = a_countWidth;
        m_countHeight = a_countHeight;
        m_countWidth  = Constants.SCREEN_WIDTH / (Constants.SCREEN_HEIGHT / a_countHeight);
        m_types       = new  int[m_countHeight][m_countWidth];
        m_floodFill   = new  int[m_countHeight][m_countWidth];
        m_aStar       = new Node[m_countHeight][m_countWidth];

        for (int row = 0; row < m_countHeight; row++) {
            for (int col = 0; col < m_countWidth; col++) {
                m_aStar[row][col] = new Node();
                m_aStar[row][col].m_x = col;
                m_aStar[row][col].m_y = row;
            }
        }

        Populate();
    }

    public void Populate() {
        for (int row = 0; row < m_countHeight; row++) {
            for (int col = 0; col < m_countWidth; col++) {
                if(Constants.RANDOM.nextInt(5) == 0)
                    m_types[row][col] = 1;
                else
                    m_types[row][col] = 0;
            }
        }
    }

    public void Update() {

    }

    public void OnTouch(Point a_point) {
        int size = Constants.SCREEN_HEIGHT / m_countHeight;
        int row = a_point.y / size;
        int col = a_point.x / size;

        if (row >= 0 && row < m_countHeight && col >= 0 && col < m_countWidth) {
            if (m_types[row][col] == 0)
                m_types[row][col] = 1;
            else
                m_types[row][col] = 0;
            //m_types[row][col] = Constants.RANDOM.nextInt(2);
        }
    }

    public void Draw(Canvas a_canvas) {
        int size = Constants.SCREEN_HEIGHT / m_countHeight;
        Paint paint = new Paint();

        for (int row = 0; row < m_countHeight; row++) {
            for (int col = 0; col < m_countWidth; col++) {
                paint.setColor(m_colors[m_types[row][col]]);
                a_canvas.drawRect(col * size, row * size, (col + 1) * size, (row + 1) * size, paint);
            }
        }

        floodFill(a_canvas);
        aStar(a_canvas);
    }

    private void floodFill(Canvas a_canvas) {
        //Reset the flood fill.
        for (int row = 0; row < m_countHeight; row++) {
            for (int col = 0; col < m_countWidth; col++) {
                m_floodFill[row][col] = 0;
            }
        }

        //Run the flood fill.
        int set = 1;
        for (int row = 0; row < m_countHeight; row++) {
            for (int col = 0; col < m_countWidth; col++) {
                FFCheckTile(row, col, m_types[row][col], set++);
            }
        }

        //Visualize the flood fill.
        int size = Constants.SCREEN_HEIGHT / m_countHeight;
        Paint paint = new Paint();

        while (m_randomColors.size() < set + 1)
            m_randomColors.add(Color.rgb(Constants.RANDOM.nextInt(256), Constants.RANDOM.nextInt(256), Constants.RANDOM.nextInt(256)));

        for (int row = 0; row < m_countHeight; row++) {
            for (int col = 0; col < m_countWidth; col++) {
                if (m_floodFill[row][col] != -1) {
                    paint.setColor(m_randomColors.get(m_floodFill[row][col]));
                    a_canvas.drawRect(col * size, row * size, (col + 1) * size, (row + 1) * size, paint);
                }
            }
        }
    }

    private boolean FFCheckTile(int a_row, int a_col, int a_target, int a_set) {
        if (a_row < 0 || a_row > m_countHeight)
            return false;
        if (a_col < 0 || a_col > m_countWidth)
            return false;
        if (m_types[a_row][a_col] != a_target)
            return false;
        if (m_floodFill[a_row][a_col] == a_set)
            return false;

        m_floodFill[a_row][a_col] = a_set;

        if (a_row + 1 < m_countHeight)
            FFCheckTile(a_row + 1, a_col, a_target, a_set);
        if (a_row - 1 >= 0)
            FFCheckTile(a_row - 1, a_col, a_target, a_set);
        if (a_col + 1 < m_countWidth)
            FFCheckTile(a_row, a_col + 1, a_target, a_set);
        if (a_col - 1 >= 0)
            FFCheckTile(a_row, a_col - 1, a_target, a_set);

        return true;
    }

    private void aStar(Canvas a_canvas)
    {
        m_beginNode = m_aStar[5][5];
        m_endNode   = m_aStar[0][0];

        //Initialize A*.
        for (int row = 0; row < m_countHeight; row++) {
            for (int col = 0; col < m_countWidth; col++) {
                m_aStar[row][col].m_costCurrent = 0;
                m_aStar[row][col].m_costToEnd   = Math.abs(m_endNode.m_x - col) + Math.abs(m_endNode.m_y - row);
                m_aStar[row][col].m_parent      = null;
            }
        }

        //Do A*.

    }

    private void ASCheckTile(int a_row, int a_col)
    {

    }
}
