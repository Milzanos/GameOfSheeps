package com.missingmarblesstudio.gameofsheeps;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.missingmarblesstudio.gameofsheeps.AI.AIAgent;
import com.missingmarblesstudio.gameofsheeps.AI.AIPriority;
import com.missingmarblesstudio.gameofsheeps.AI.Actions.AIActionDoAction;
import com.missingmarblesstudio.gameofsheeps.AI.Actions.AIActionMoveTo;

/**
 * Created by Vonck-PC on 08/11/2016.
 */
public class AIFarmer extends AIAgent {
    int m_color;

    public AIFarmer(Point a_position) {
        super(a_position);

        AIPriority priority = new AIPriority("Feeding the sheep.");
        priority.SetImportance(10);

        //Add the Get Food action.
        AIActionMoveTo getFood = new AIActionMoveTo(this, Constants.NODE_FOOD, true);
        priority.AddAction(getFood);
        priority.AddAction(new AIActionDoAction(getFood, Constants.RESOURCE_FOOD, -3, 1.0f));

        //Add the Feed Sheep action.
        AIActionMoveTo feedSheep = new AIActionMoveTo(this, Constants.NODE_SHEEP_PEN, false);
        priority.AddAction(feedSheep);
        priority.AddAction(new AIActionDoAction(feedSheep, Constants.RESOURCE_FOOD, 2, 1.0f));
        AddPriority(priority);

        priority = new AIPriority("Waiting..");
        priority.SetImportance(3);
        priority.AddAction(new AIActionDoAction(1.0f));
        AddPriority(priority);

        priority = new AIPriority("Random Movement.");
        priority.SetImportance(5);
        priority.AddAction(new AIActionMoveTo(this));
        AddPriority(priority);

        m_color = Color.rgb(Constants.RANDOM.nextInt(255), Constants.RANDOM.nextInt(255), Constants.RANDOM.nextInt(255));
    }

    public void Draw(Canvas a_canvas) {
        int size = Constants.SCREEN_HEIGHT / Constants.COUNT_HEIGHT;
        Paint paint = new Paint();
        paint.setColor(m_color);

        //Draw the agent.
        Point current = GetSmoothPosition();
        a_canvas.drawRect(current.x, current.y, current.x + size, current.y + size, paint);

        //Draw the destination.
        paint.setAlpha(100);
        Point point = GetDestination();
        a_canvas.drawCircle(point.x * size + size * 0.5f, point.y * size + size * 0.5f, size * 0.25f, paint);

        //Draw the current action.
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(Constants.SCREEN_HEIGHT * 0.05f);
        a_canvas.drawText(m_priorityName, current.x + size * 0.5f, current.y, paint);
    }
}
