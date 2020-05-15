/*
 * Field.java
 *
 * Created on 28. Februar 2005, 19:59
 */

import javax.microedition.lcdui.*;

import com.nokia.mid.ui.*;

/**
 *
 * @author  Administrator
 */
public class Field {
    
    private int top;
    private int left;
    private int right;
    private int bottom;
    private boolean isHighlighted;
    private Stone stone = null;
    
    /** Creates a new instance of Field */
    public Field() {
        top = left = right = bottom = 0;
    }
    
    /*
     * creates a new instance of field
     */
    public Field(int x1, int y1, int x2, int y2) {
        setPlacement(x1, y1, x2, y2);
    }
    
    /*
     * sets the coordiinates of the field
     */
    public void setPlacement(int x1, int y1, int x2, int y2) {
        top = y1;
        left = x1;
        right = x2;
        bottom = y2;
        isHighlighted = false;
    }
    
    public int getTop() {
        return top;
    }
    
    public int getLeft() {
        return left;
    }
    
    public int getRight() {
        return right;
    }
    
    public int getBottom() {
        return bottom;
    }
    
    /*
     * draws the field, highlight if needed and stone if available
     */
    public void Draw(Graphics g) {
        if(stone != null) {
            stone.Draw(g);
        }
        DrawSurroundings(g);
    }
    
    /*
     * draws the field and highlights it if necessary
     */
    public void DrawSurroundings(Graphics g) {
        g.setColor(0, 0, 0);
        g.drawRect(left, top, right, bottom);
        if(isHighlighted) {
            g.drawRect(left+1, top+1, right-2, bottom-2);
        }
        else {
            g.setColor(255, 255, 255);
            g.drawRect(left+1, top+1, right-2, bottom-2);
        }
    }
    
    public void SetHighlight(boolean highlight) {
        isHighlighted = highlight;
    }
    
    public boolean getHighlighted() {
        return isHighlighted;
    }
    
    public void SetStone(Stone stone) {
        this.stone = stone;
        stone.SetBounds(left, top, right, bottom);
    }
    
    public Stone GetStone() {
        return stone;
    }
    
    public boolean isEmpty() {
        if(stone == null) {
            return true;
        }
        return false;
    }
    
    /* 
     * changes the stones color
     */
    public Thread changeStoneColor() {
        if(stone.getColor() == Stone.COLOR_WHITE) {
            return stone.setColor(Stone.COLOR_BLACK);
        }
        return stone.setColor(Stone.COLOR_WHITE);
    }
    
    public int GetStoneColor() {
        if(stone != null) {
            return stone.getColor();
        }
        return 0;
    }
}
