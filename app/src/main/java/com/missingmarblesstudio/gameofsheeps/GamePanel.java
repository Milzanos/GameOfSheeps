package com.missingmarblesstudio.gameofsheeps;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Vonck-PC on 06/11/2016.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread m_thread;
    private Point      m_point;
    private World      m_world;

    public GamePanel(Context a_context)
    {
        super(a_context);
        getHolder().addCallback(this);
        m_thread = new MainThread(getHolder(), this);
        m_point  = new Point(0, 0);

        m_world = new World(20, 15);

        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder a_surfaceHolder, int a_format, int a_width, int a_height)
    {

    }

    @Override
    public void surfaceCreated(SurfaceHolder a_surfaceHolder)
    {
        m_thread = new MainThread(getHolder(), this);

        m_thread.SetRunning(true);
        m_thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder a_surfaceHolder)
    {
        boolean retry = true;

        while(retry)
        {
            try
            {
                m_thread.SetRunning(false);
                m_thread.join();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent a_event)
    {
        switch(a_event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                m_point.set((int)a_event.getX(), (int)a_event.getY());
                m_world.OnTouch(m_point);
                break;
            case MotionEvent.ACTION_MOVE:
                m_point.set((int)a_event.getX(), (int)a_event.getY());
                break;
        }

        return true;
        //return super.onTouchEvent(a_event);
    }

    public void Update()
    {
        m_world.Update();
    }

    @Override
    public void draw(Canvas a_canvas)
    {
        super.draw(a_canvas);

        a_canvas.drawColor(Color.WHITE);
        m_world.Draw(a_canvas);
    }
}
