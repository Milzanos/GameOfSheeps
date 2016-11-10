package com.missingmarblesstudio.gameofsheeps;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.missingmarblesstudio.gameofsheeps.AI.AIWorld;

import java.util.ArrayList;

//TODO: Add multi-threading support.

/**
 * Created by Vonck-PC on 06/11/2016.
 */
public class World {
    ArrayList<AIFarmer> m_farmers = new ArrayList<>();


    public World(int a_countWidth, int a_countHeight) {
        Constants.COUNT_WIDTH = a_countWidth;
        Constants.COUNT_HEIGHT = a_countHeight;
        Constants.COUNT_WIDTH = Constants.SCREEN_WIDTH / (Constants.SCREEN_HEIGHT / a_countHeight);
        Constants.INIT_GRID();

        Constants.SET_DEFAULT_RESOURCE_VALUE(Constants.NODE_FOOD, Constants.RESOURCE_FOOD, 6);
        Constants.ADD_DEFAULT_RESOURCE(Constants.NODE_FOOD, Constants.RESOURCE_FOOD);
        Constants.SET_DEFAULT_RESOURCE_VALUE(Constants.NODE_SHEEP_PEN, Constants.RESOURCE_FOOD, 0);
        Constants.ADD_DEFAULT_RESOURCE(Constants.NODE_SHEEP_PEN, Constants.RESOURCE_FOOD);
        Populate();
        FloodFill.RunFloodFill();
    }

    public void Populate() {
        for (int row = 0; row < Constants.COUNT_HEIGHT; row++) {
            for (int col = 0; col < Constants.COUNT_WIDTH; col++) {
                Constants.SET_NODE(row, col, Constants.NODE_GROUND);

                if (Constants.RANDOM.nextInt(50) == 0) {
                    if (row >= 3 && col >= 3 && row < Constants.COUNT_HEIGHT - 1 && col < Constants.COUNT_WIDTH - 1) { //Give us some walking room man.
                        boolean possible = true;

                        for (int i = -2; i <= 0; i++)
                            for (int j = -2; j <= 0; j++)
                                if (row + i < 0 || col + j < 0)
                                    possible = false;
                                else if (Constants.GET_NODE(row + i, col + j).m_area != Constants.NODE_GROUND)
                                    possible = false;

                        if (possible) {
                            for (int i = -2; i <= 0; i++)
                                for (int j = -2; j <= 0; j++)
                                    Constants.SET_NODE(row + i, col + j, Constants.NODE_SHEEP_PEN);

                            boolean found = false;

                            while (!found) {
                                int x = Constants.RANDOM.nextInt(Constants.COUNT_WIDTH);
                                int y = Constants.RANDOM.nextInt(Constants.COUNT_HEIGHT);

                                if (Constants.GET_NODE(y, x).m_area == Constants.NODE_GROUND) {
                                    found = true;
                                    Constants.SET_NODE(y, x, Constants.NODE_FOOD);
                                }
                            }
                        }
                    }
                }
            }
        }

        m_farmers.add(new AIFarmer(new Point(0, 0)));
    }

    public void Update() {
        AIWorld.CalculateTotalCount();
        //Update all agents.
        for (AIFarmer it : m_farmers)
            it.Update(Constants.DELTA_TIME);
    }

    public void OnTouch(Point a_point) {
        /*int size = Constants.SCREEN_HEIGHT / Constants.COUNT_HEIGHT;
        int row = a_point.y / size;
        int col = a_point.x / size;

        if (row >= 0 && row < Constants.COUNT_HEIGHT && col >= 0 && col < Constants.COUNT_WIDTH) {
            if (Constants.GET_NODE(row, col).m_area == Constants.NODE_WALL)
                Constants.SET_NODE(row, col, Constants.GET_NODE(row, col).m_area = Constants.NODE_GROUND);
            else if (Constants.GET_NODE(row, col).m_area == Constants.NODE_GROUND)
                Constants.SET_NODE(row, col, Constants.NODE_WALL);
        }

        //Update flood fill.
        FloodFill.RunFloodFill();*/ //TODO: Revert this back.

        int size = Constants.SCREEN_HEIGHT / Constants.COUNT_HEIGHT;
        m_farmers.add(new AIFarmer(new Point(a_point.x / size, a_point.y / size)));
    }

    public void Draw(Canvas a_canvas) {
        int size = Constants.SCREEN_HEIGHT / Constants.COUNT_HEIGHT;
        Paint paint = new Paint();

        for (int row = 0; row < Constants.COUNT_HEIGHT; row++) {
            for (int col = 0; col < Constants.COUNT_WIDTH; col++) {

                switch (Constants.GET_NODE(row, col).m_area) {
                    case Constants.NODE_GROUND:
                        paint.setColor(Color.YELLOW);
                        break;
                    case Constants.NODE_WALL:
                        paint.setColor(Color.BLACK);
                        break;
                    case Constants.NODE_FOOD:
                        paint.setColor(Color.RED);
                        break;
                    case Constants.NODE_SHEEP_PEN:
                        paint.setColor(Color.BLUE);
                        break;
                }
                a_canvas.drawRect(col * size, row * size, (col + 1) * size, (row + 1) * size, paint);
            }
        }

        //Draw all agents.
        for (AIFarmer it : m_farmers)
            it.Draw(a_canvas);

        //Draw debug information.
        paint.setColor(Color.BLACK);
        paint.setTextSize(Constants.SCREEN_HEIGHT * 0.05f);
        a_canvas.drawText("Food: " + Constants.GET_RESOURCE(Constants.RESOURCE_FOOD), Constants.SCREEN_WIDTH / 10, Constants.SCREEN_HEIGHT / 10, paint);
    }
}