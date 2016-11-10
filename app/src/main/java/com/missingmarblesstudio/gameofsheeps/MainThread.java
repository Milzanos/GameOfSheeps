package com.missingmarblesstudio.gameofsheeps;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Vonck-PC on 06/11/2016.
 */
public class MainThread extends Thread {
    public static final int MAX_FPS         = 30;
    public static Canvas    m_canvas        = null;
    private double          m_averageFPS    = 0;
    private SurfaceHolder   m_surfaceHolder = null;
    private GamePanel       m_gamePanel     = null;
    private boolean         m_running       = false;

    public MainThread(SurfaceHolder a_surfaceHolder, GamePanel a_gamePanel)
    {
        m_surfaceHolder = a_surfaceHolder;
        m_gamePanel     = a_gamePanel;
    }

    public void SetRunning(boolean a_running)
    {
        m_running = a_running;
    }

    @Override
    public void run()
    {
        long startTime;
        long timeMillis;
        long targetTime = 1000 / MAX_FPS;
        long waitTime;
        int  frameCount = 0;
        long totalTime  = 0;

        while(m_running)
        {
            startTime = System.nanoTime();

            try
            {
                m_canvas = m_surfaceHolder.lockCanvas();
                synchronized (m_surfaceHolder)
                {
                    m_gamePanel.Update();
                    if (m_canvas != null)
                        m_gamePanel.draw(m_canvas);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if(m_canvas != null) {
                    try {
                        m_surfaceHolder.unlockCanvasAndPost(m_canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try
            {
                if(waitTime > 0)
                {
                    sleep(waitTime);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;

            //Set delta time.
            Constants.DELTA_TIME = (float) (System.nanoTime() - startTime) / 1000000000.0f;

            if(frameCount >= MAX_FPS)
            {
                m_averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount   = 0;
                totalTime = 0;
                System.out.println(m_averageFPS);
            }
        }
    }
}
