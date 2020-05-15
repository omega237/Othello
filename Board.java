/*
 * Board.java
 *
 * Created on 28. Februar 2005, 19:56
 */

import com.nokia.mid.ui.*;

import javax.microedition.lcdui.*;
import java.util.Vector;

/**
 *
 * @author  Administrator
 */
public class Board extends /*Full*/Canvas implements Runnable {
    
    private Field[][] myBoard;
    private Vector freeFields;
    private int actualPossibility;
    private int previousPossibility;
    private int colp;
    private int rowp;
    private int numPossibilities;
    private int BoardSize;
    private Thread GameThread;
    private Command mainmenu;
    private OthelloMIDlet omi;
    private Thread anim = null;
    private int toMove;
    private int numBlack;
    private int numWhite;
    private List lst;
    
    private int _getcol(int n) {
        return n%BoardSize;
    }
    
    private int _getrow(int n) {
        return n/BoardSize;
    }
    
    private void nextpossibility() {
        previousPossibility = actualPossibility;
        actualPossibility++;
        if(actualPossibility > numPossibilities-1) {
            actualPossibility = 0;
        }
        myBoard[rowp][colp].SetHighlight(false);
        Integer i = (Integer)freeFields.elementAt(actualPossibility);
        int idx = i.intValue();
        myBoard[_getrow(idx)][_getcol(idx)].SetHighlight(true);
        colp = _getcol(idx);
        rowp = _getrow(idx);
    }
    
    private void previouspossibility() {
        previousPossibility = actualPossibility;
        actualPossibility--;
        if(actualPossibility < 0) {
            actualPossibility = numPossibilities-1;
        }
        myBoard[rowp][colp].SetHighlight(false);
        Integer i = (Integer)freeFields.elementAt(actualPossibility);
        int idx = i.intValue();
        myBoard[_getrow(idx)][_getcol(idx)].SetHighlight(true);
        colp = _getcol(idx);
        rowp = _getrow(idx);
    }
 
    /*
     * creates and initializes the board
     */
    private void InitBoard(int size) {
        BoardSize = size;
        myBoard = new Field[size][size];
        int inc = 64/size;
        int pos = 0;
        
        if(size==6) {
            pos = 2;
        }
        else if(size==8) {
            pos = 3;
        }
        else if(size==10) {
            pos = 4;
        }
        
        
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++) {
                myBoard[i][j] = new Field(j*inc, i*inc, inc, inc);
            }
        }
        
        /*
         * setup the inital board 
         */
        myBoard[pos][pos].SetStone(new Stone(Stone.COLOR_WHITE));
        myBoard[pos][pos+1].SetStone(new Stone(Stone.COLOR_BLACK));
        myBoard[pos+1][pos].SetStone(new Stone(Stone.COLOR_BLACK));
        myBoard[pos+1][pos+1].SetStone(new Stone(Stone.COLOR_WHITE));
        
        toMove = Stone.COLOR_BLACK;
        
        numBlack = numWhite = 2;
        
        freeFields = new Vector(1,1);
        FreeFieldsList();
        
        actualPossibility = 0;
        previousPossibility = 0;
        numPossibilities = freeFields.size();
        Integer i = (Integer)freeFields.elementAt(0);
        int idx = i.intValue();
        rowp = _getrow(idx);
        colp = _getcol(idx);
        nextpossibility();
       
        GameThread = new Thread(this);
        GameThread.start();
    }
    
    private void createstatistics() {
        numBlack = numWhite = 0;
        for(int i=0; i<BoardSize*BoardSize; i++) {
            if(!myBoard[_getrow(i)][_getcol(i)].isEmpty()) {
                if(myBoard[_getrow(i)][_getcol(i)].GetStoneColor() == Stone.COLOR_WHITE) {
                    numWhite++;
                }
                else {
                    numBlack++;
                }
            }
        }
        //System.out.println(numBlack);
        //System.out.println(numWhite);
    }
    
    /** Creates a new instance of Board */
    public Board(Command c, OthelloMIDlet o, int size, List l) {
        mainmenu = c;
        omi = o;
        lst = l;
        InitBoard(size);
    }
    
    /** stops the game's repaint thread */
    public void StopGame() {
        GameThread = null;
    }
    
    /*
     * paints what is necessary
     */
    public void paint(Graphics g) {
        // paint the statistics left of statistic6 66 right 96, top 0, bottom 35
        for(int i=0; i<BoardSize; i++) {
            for(int j=0; j<BoardSize; j++) {
                myBoard[i][j].Draw(g);
            }
        }
        g.setColor(255, 255, 255);
        g.fillRect(66,0,30,65 );
        Stone.draw_self(Stone.COLOR_BLACK, 70, 5, (65/BoardSize)-3, g);
        Stone.draw_self(Stone.COLOR_WHITE, 70, 5+Font.getDefaultFont().getHeight(), (65/BoardSize)-3, g);
        g.setColor(0, 0, 0);
        g.setFont(Font.getDefaultFont());
        g.drawString(String.valueOf(numBlack), 96, 5, Graphics.TOP|Graphics.RIGHT);
        g.drawString(String.valueOf(numWhite), 96, 5+Font.getDefaultFont().getHeight(), Graphics.TOP|Graphics.RIGHT);
        Stone.draw_self(toMove, 70, 35, 25, g);
    }
    
    private void do1() {
        // moves cursor nothwest if possible
        myBoard[rowp][colp].SetHighlight(false);
        if(colp > 0 && rowp > 0) {
            rowp--;
            colp--;
        }
        myBoard[rowp][colp].SetHighlight(true);
    }
    
    private void do2() {
        // moves cursor noth if possible
        myBoard[rowp][colp].SetHighlight(false);
        if(rowp > 0) {
            rowp--;
        }
        myBoard[rowp][colp].SetHighlight(true);
    }
    
    private void do3() {
        // moves cursor notheast if possible
        myBoard[rowp][colp].SetHighlight(false);
        if(colp < BoardSize-1 && rowp > 0) {
            rowp--;
            colp++;
        }
        myBoard[rowp][colp].SetHighlight(true);
    }
    
    private void do4() {
        // moves cursor west if possible
        myBoard[rowp][colp].SetHighlight(false);
        if(colp > 0) {
            colp--;
        }
        myBoard[rowp][colp].SetHighlight(true);
    }
    
    private void do6() {
        // moves cursor east if possible
        myBoard[rowp][colp].SetHighlight(false);
        if(colp < BoardSize-1) {
            colp++;
        }
        myBoard[rowp][colp].SetHighlight(true);
    }
    
    private void do7() {
        // moves cursor southwest if possible
        myBoard[rowp][colp].SetHighlight(false);
        if(colp > 0 && rowp < BoardSize-1) {
            rowp++;
            colp--;
        }
        myBoard[rowp][colp].SetHighlight(true);
    }
    
    private void do8() {
        // moves cursor south if possible
       myBoard[rowp][colp].SetHighlight(false);
        if(rowp < BoardSize-1) {
            rowp++;
        }
        myBoard[rowp][colp].SetHighlight(true);
    }
    
    private void do9() {
        // moves cursor southeast if possible
        myBoard[rowp][colp].SetHighlight(false);
        if(colp < BoardSize-1 && rowp < BoardSize-1) {
            rowp++;
            colp++;
        }
        myBoard[rowp][colp].SetHighlight(true);
    }
    
    /*
     * lays down a stone at rowp colp
     */
    private void makemove(int row, int col) {
        // check directions west northwest, southwest, north, south, northeast, southeast, east
        
//        System.out.println(row + "\n" + col);
        myBoard[row][col].SetHighlight(false);
        Stone s = new Stone(toMove);
        myBoard[row][col].SetStone(s);

        
        if(northwest(row, col)) {
//            System.out.println("NW");
            int rowc = row-1;
            int colc = col-1;
            while(colc > 0 && rowc > 0 && myBoard[rowc][colc].GetStoneColor() != toMove) {
                myBoard[rowc][colc].changeStoneColor();
                colc--; 
                rowc--;
            }
        }
        
        if(west(row, col)) {
//            System.out.println("W");
            int colc = col-1;
             while(colc > 0 && myBoard[row][colc].GetStoneColor() != toMove) {
                myBoard[row][colc].changeStoneColor();
                colc--; 
            }
        }
        
        if(southwest(row, col)) {
//            System.out.println("SW");
            int colc = col-1;
            int rowc = row+1;
            while(colc > 0 && rowc < BoardSize-1 && myBoard[rowc][colc].GetStoneColor() != toMove) {
                myBoard[rowc][colc].changeStoneColor();
                colc--; 
                rowc++;
            }
        }
        
        if(north(row, col)) {
//            System.out.println("N");
            int rowc = row-1;
//            System.out.println(row);
//            System.out.println(rowc);
            while(rowc > 0 && myBoard[rowc][col].GetStoneColor() != toMove) {
                myBoard[rowc][col].changeStoneColor();
                rowc--;
//                System.out.println("--" + rowc);
            }
        }
        
        if(south(row, col)) {
//            System.out.println("S");
           int rowc = row+1;
           while(rowc < BoardSize-1 && myBoard[rowc][col].GetStoneColor() != toMove) {
                myBoard[rowc][col].changeStoneColor();
                rowc++;
            } 
        }
        
        if(northeast(row, col)) {
//            System.out.println("NE");
            int colc = col+1;
            int rowc = row-1;
            while(colc < BoardSize-1 && rowc > 0 && myBoard[rowc][colc].GetStoneColor() != toMove) {
                myBoard[rowc][colc].changeStoneColor();
                colc++; 
                rowc--;
            }
        }
        
        if(east(row, col)) {
//            System.out.println("E");
            int colc = col+1;
            while(colc < BoardSize-1 && myBoard[row][colc].GetStoneColor() != toMove) {
                myBoard[row][colc].changeStoneColor();
                colc++; 
            }
        }
//        System.out.println("After checking east");
        
        if(southeast(row, col)) {
//            System.out.println("SE");
            int colc = col+1;
            int rowc = row+1;
            while(colc < BoardSize-1 && rowc < BoardSize-1 && myBoard[rowc][colc].GetStoneColor() != toMove) {
                myBoard[rowc][colc].changeStoneColor();
                colc++; 
                rowc++;
            }
        }
        
        
        toMove = Stone.OppositeColor(toMove);
        s = null;
        
//        System.out.println("After checking se, before freefieldslist");
        FreeFieldsList();
        createstatistics();
//        System.out.println("After freefieldslist");
        if(freeFields.size()>0) {
            //System.out.println("moove possible");
            actualPossibility = 0;
            previousPossibility = 0;
            Integer i = (Integer)freeFields.elementAt(actualPossibility);
            int idx = i.intValue();
            rowp = _getrow(idx);
            colp = _getcol(idx);
            nextpossibility();
        }
        else {
            //System.out.println("impossible, next player");
            toMove = Stone.OppositeColor(toMove);
            FreeFieldsList();
            if(freeFields.size() > 0) {
                actualPossibility = 0;
                previousPossibility = 0;
                Integer i = (Integer)freeFields.elementAt(actualPossibility);
                int idx = i.intValue();
                rowp = _getrow(idx);
                colp = _getcol(idx);
                nextpossibility();
            }
            else { // game has ended
                 //System.out.println("Else else");
                 Image AboutImage = null;
                 try {
                        AboutImage = Image.createImage("/nb16x16.png");
                    } catch(java.lang.Exception e) {
                        // do nothing
                    }
                    Alert AboutScreen = new Alert("Othello");
                    AboutScreen.setTimeout(Alert.FOREVER);
                    String text;
                    if(numBlack == numWhite) {
                        //System.out.println("Tie");
                        text = "Game ends tied";
                    }
                    else if (numBlack > numWhite) {
                        //System.out.println("White");
                        text = "White wins "+ numWhite + " to " + numBlack;
                    }
                    else {
                        //System.out.println("Black");
                        text = "White wins "+ numBlack + " to " + numWhite;
                    }
                    AboutScreen.setString(text);
                    AboutScreen.setType(AlertType.INFO);
                    if(AboutImage != null) {
                        AboutScreen.setImage(AboutImage);
                        }
                    StopGame();
                    Display.getDisplay(omi).setCurrent(AboutScreen, lst);
            }           
        }
//        System.out.println("Before return");
        return;
    }
    
    public void keyPressed(int keyCode) {
        if(keyCode == FullCanvas.KEY_SOFTKEY1 || keyCode == FullCanvas.KEY_SOFTKEY2) {
            omi.commandAction(mainmenu, this);
            StopGame();
        }
        else if(keyCode == FullCanvas.KEY_DOWN_ARROW) {
            // next possibility
            nextpossibility();
        }
        else if(keyCode == FullCanvas.KEY_UP_ARROW) {
            // previous possibility
            previouspossibility();
        }
        else if(keyCode == FullCanvas.KEY_NUM1) {
            do1();
        }
        else if(keyCode == FullCanvas.KEY_NUM2) {
            do2();
        }
        else if(keyCode == FullCanvas.KEY_NUM3) {
            do3();
        }
        else if(keyCode == FullCanvas.KEY_NUM4) {
            do4();
        }
        else if(keyCode == FullCanvas.KEY_NUM5) {
//            System.out.println(rowp + "\n" + colp);
            if(isMovePossible(rowp, colp)) {
                makemove(rowp, colp);
            }
            else {
                nextpossibility();
            }
        }
        else if(keyCode == FullCanvas.KEY_NUM6) {
            do6();
        }
        else if(keyCode == FullCanvas.KEY_NUM7) {
            do7();
        }
        else if(keyCode == FullCanvas.KEY_NUM8) {
            do8();
        }
        else if(keyCode == FullCanvas.KEY_NUM9) {
            do9();
        }
    }
    
    public void keyRepeated(int keyCode) {
        
    }
    
    public void run() {
        Thread currentThread = Thread.currentThread();
        while(currentThread == GameThread) {
            repaint();
            serviceRepaints();
            if(anim != null) {
                if(!anim.isAlive()) {
                    anim = null;
                }
            }
        }
    }  
    
    private boolean north(int row, int col) {
        if(row > 1) {
            if(!myBoard[row-1][col].isEmpty() && myBoard[row-1][col].GetStoneColor() == Stone.OppositeColor(toMove)) {
                for(int i=1; i<=row; i++) {
                    if(myBoard[row-i][col].GetStoneColor() == toMove) {
                        return true;
                    }
                    else if (myBoard[row-i][col].GetStoneColor() == 0) {
                        return false;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean northeast(int row, int col) {
        if((col < (BoardSize-2)) && (row > 1)) {
            if(!myBoard[row-1][col+1].isEmpty() && myBoard[row-1][col+1].GetStoneColor() == Stone.OppositeColor(toMove)) {
                int _col = col;
                int _row = row;
                while(_col < BoardSize-1 && _row > 0) {
                    _col++;
                    _row--;
                    if(myBoard[_row][_col].GetStoneColor() == toMove) {
                        return true;
                    }
                    else if (myBoard[_row][_col].GetStoneColor() == 0) {
                        return false;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean east(int row, int col) {
        if(col < (BoardSize-2)) {
            if(!myBoard[row][col+1].isEmpty() && myBoard[row][col+1].GetStoneColor() == Stone.OppositeColor(toMove)) {
                for(int i=1; i<(BoardSize-col); i++) {
                    if(myBoard[row][col+i].GetStoneColor() == toMove) {
                        return true;
                    }
                    else if (myBoard[row][col+i].GetStoneColor() == 0) {
                        return false;
                    }
                }
            }
            
        }
        return false;
    }
    
    private boolean southeast(int row, int col) {
        if((col < (BoardSize-2)) && (row < (BoardSize-2))) {
            if(!myBoard[row+1][col+1].isEmpty() && myBoard[row+1][col+1].GetStoneColor() == Stone.OppositeColor(toMove)) {
                int _col = col;
                int _row = row;
                while(_col < BoardSize-1 && _row < BoardSize-1) { // mind!! incrmenting before action is bad... have to substract -1 from borad size!!
                    _col++;
                    _row++;
                    if(myBoard[_row][_col].GetStoneColor() == toMove) {
                        return true;
                    }
                    else if (myBoard[_row][_col].GetStoneColor() == 0) {
                        return false;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean south(int row, int col) {
       if(row < (BoardSize-2)) {
            if(!myBoard[row+1][col].isEmpty() && myBoard[row+1][col].GetStoneColor() == Stone.OppositeColor(toMove)) {
                for(int i=1; i<(BoardSize-row); i++) {
                    if(myBoard[row+i][col].GetStoneColor() == toMove) {
                        return true;
                    }
                    else if (myBoard[row+i][col].GetStoneColor() == 0) {
                        return false;
                    }
                }
            }
        } 
       return false;
    }
    
    private boolean southwest(int row, int col) {
         if((col > 1) && (row < (BoardSize-2))) {
            if(!myBoard[row+1][col-1].isEmpty() && myBoard[row+1][col-1].GetStoneColor() == Stone.OppositeColor(toMove)) {
                int _col = col;
                int _row = row;
                while(_col > 0 && _row < BoardSize-1) {
                    _col--;
                    _row++;
                    if(myBoard[_row][_col].GetStoneColor() == toMove) {
                        return true;
                    }
                    else if (myBoard[_row][_col].GetStoneColor() == 0) {
                        return false;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean west(int row, int col) {
        if(col > 1) {
            if(!myBoard[row][col-1].isEmpty() && myBoard[row][col-1].GetStoneColor() == Stone.OppositeColor(toMove)) {
                for(int i=1; i<=col; i++) {
                    if(myBoard[row][col-i].GetStoneColor() == toMove) {
                        return true;
                    }
                    else if (myBoard[row][col-i].GetStoneColor() == 0) {
                        return false;
                    }
                }
            }
            
        }
        return false;
    }
    
    private boolean northwest(int row, int col) {
        if((col > 1) && (row > 1)) {
            if(!myBoard[row-1][col-1].isEmpty() && myBoard[row-1][col-1].GetStoneColor() == Stone.OppositeColor(toMove)) {
                int _col = col;
                int _row = row;
                while(_col > 0 && _row > 0) {
                    _col--;
                    _row--;
                    if(myBoard[_row][_col].GetStoneColor() == toMove) {
                        return true;
                    }
                    else if (myBoard[_row][_col].GetStoneColor() == 0) {
                        return false;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean isMovePossible(int row, int col) {
        
//        System.out.println("ismovepossible N");
        if(north(row, col)) {
            return true;
        }
        
//        System.out.println("ismovepossible NE");
        if(northeast(row, col)) {
            return true;
        }
        
//        System.out.println("ismovepossible E");
        if(east(row, col)) {
            return true;
        }
        
//        System.out.println("ismovepossible SE");
        if(southeast(row,col)) {
            return true;
        }
        
//        System.out.println("ismovepossible S");
        if(south(row, col)) {
            return true;
        }
        
//        System.out.println("ismovepossible SW");
        if(southwest(row, col)) {
            return true;
        }
        
//        System.out.println("ismovepossible W");
        if(west(row, col)) {
            return true;
        }
        
//        System.out.println("ismovepossible NW");
        if(northwest(row, col)) {
            return true;
        }
        
//        System.out.println("ismovepossible ret");
        return false;
    }
    
    private void FreeFieldsList() {
        freeFields.removeAllElements();
        for(int i=0; i<(BoardSize*BoardSize); i++) {
            int col = i%BoardSize;
            int row = i/BoardSize;
            if(myBoard[row][col].isEmpty()) {
//                System.out.println(i);
                // if empty look, if stone of actual color could be placed here
                if(isMovePossible(row, col)) {

                    freeFields.addElement(new Integer(i));
                }
            }
        }
        numPossibilities = freeFields.size();
    }
    
    
    
}
