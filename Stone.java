/*
 * Stone.java
 *
 * Created on 28. Februar 2005, 23:18
 */

import javax.microedition.lcdui.*;

/**
 *
 * @author  Administrator
 */
public class Stone implements Runnable {
    private int Color;
    public static int COLOR_WHITE = 1;
    public static int COLOR_BLACK = 2;
    private Thread animation = null;
    private int x=0, y=0, w=0, h=0;
    private boolean outline = false;
    private static long MILLIS_PER_TICK = 250;
    private boolean drawside = false;
    
    public static int OppositeColor(int color) {
        if (color == COLOR_WHITE) {
            return COLOR_BLACK;
        }
        return COLOR_WHITE;
    }
    
    public static void draw_self(int color, int x, int y, int size, Graphics g) {
        if(color == COLOR_WHITE) {
            g.setColor(0, 0, 0);
            g.drawArc(x, y, size, size, 0, 360);
            return;
        }
        else {
            g.setColor(0, 0, 0);
            g.fillArc(x, y, size, size, 0, 360);
            return;
        }
    }

    /** Creates a new instance of Stone */
    public Stone(int Color) {
        this.Color = Color;
        if(Color == COLOR_WHITE) {
            outline = true;
        }
    }
    
    public void SetBounds(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    
    /*
     * draws the stone 
     */
    public void Draw(Graphics g) {
        g.setColor(255, 255, 255);
        g.fillRect(x+1, y+1, w-1, h-1);
        g.setColor(0, 0, 0);

        if(outline) {
            if(drawside){
                g.drawLine((x+x+w)/2, y+2, (x+x+w)/2, y+h-2);
            }
            else {
                g.drawArc(x+2, y+2, w-4, h-4, 0, 360);
            }
        }
        else {
            if(drawside) {
                g.drawLine((x+x+w)/2, y+2, (x+x+w)/2, y+h-2);
            }
            else {
                g.fillArc(x+2, y+2, w-3, h-3, 0, 360);
            }
        }
    }
    
    /*
     * gets stone's color
     */
    public int getColor() {
        return Color;
    }
    
    /*
     * sets stone's color
     */
    public Thread setColor(int Color) {
        this.Color = Color;
        if(animation==null) {
           return animate();
        }
        return null;
    }
    
    public Thread animate() {
        animation = new Thread(this);
        animation.start();
        return animation;
    }
    
    public void run() {
        Thread currentThread = Thread.currentThread();
        long		startTime		= 0;
	long		timeTaken		= 0;
        boolean run = true;
        while(animation == currentThread && run) {
            try {
                    startTime = System.currentTimeMillis();
                
                    if(drawside == false) {
                        drawside = true;
                    }
                    else if(drawside == true) {
                        outline = !outline;
                        drawside = false;
                        run = false;
                    }
               
                timeTaken = System.currentTimeMillis()-startTime;
                if (timeTaken < MILLIS_PER_TICK)
                {
                    synchronized (this)
                    {
                            wait(MILLIS_PER_TICK - timeTaken);
                    }
                }
                else
                {
                    currentThread.yield();
                }
            }
            catch (InterruptedException e) {}
        }
        animation = null;
    }
}
