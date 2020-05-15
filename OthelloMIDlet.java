/*
 * OthelloMIDlet.java
 *
 * Created on 27. Februar 2005, 23:02
 */

import java.io.*;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

import com.nokia.mid.ui.*;

/**
 * An Othello Game
 * @author Administrator
 * @version 1.0
 */
public class OthelloMIDlet extends MIDlet implements CommandListener {
    
    private Display display;    // The display for this MIDlet
    
    /*
     * the MIDlet's main menu
     */
    private List MainMenu;
    private Command SelectCommand;
    private Command ExitCommand;
    
    private Board Game;
    
    /*
     * the MIDlet's About Screen
     */
    private Alert AboutScreen;
    private Image AboutImage;
    
    private Form OptionsScreen;
    private ChoiceGroup OptionsChoices;
    
    private int BoardSize = 0;
    
    public OthelloMIDlet() {
        display = Display.getDisplay(this);
        
        MainMenu = new List("Othello", List.IMPLICIT);
        MainMenu.append("New Game", null);
        MainMenu.append("Options", null);
        MainMenu.append("Help", null);
        MainMenu.append("About", null);
        SelectCommand = new Command("OK", Command.OK, 1);
        ExitCommand = new Command("Exit", Command.EXIT, 1);
        MainMenu.addCommand(SelectCommand);
        MainMenu.addCommand(ExitCommand);
        MainMenu.setCommandListener(this);
        
        
        
        try {
            AboutImage = Image.createImage("/nb16x16.png");
        } catch(IOException e) {
            // do nothing
        }
        AboutScreen = new Alert("Othello");
        AboutScreen.setTimeout(Alert.FOREVER);
        AboutScreen.setString("Othello Version 1.0\nCoded by omega237\nVisit --- for more Applications");
        AboutScreen.setType(AlertType.INFO);
        if(AboutImage != null) {
            AboutScreen.setImage(AboutImage);
        }
        
        OptionsScreen = new Form("Options");
        OptionsChoices = new ChoiceGroup("Board Size", Choice.EXCLUSIVE);
        OptionsChoices.append("6x6", null);
        OptionsChoices.append("8x8", null);
        OptionsChoices.append("10x10", null);
        OptionsScreen.append(OptionsChoices);
        OptionsScreen.addCommand(ExitCommand);
        OptionsScreen.setCommandListener(this);
    }
    
    /**
     * Start up the Hello MIDlet by creating the TextBox and associating
     * the exit command and listener.
     */
    public void startApp() {
        display.setCurrent(MainMenu);
    }
    
    /**
     * Pause is a no-op since there are no background activities or
     * record stores that need to be closed.
     */
    public void pauseApp() {
    }
    
    /**
     * Destroy must cleanup everything not handled by the garbage collector.
     * whatever that may be
     */
    public void destroyApp(boolean unconditional) {
    }
    
    private int GetBoardSize() {
        int idx = OptionsChoices.getSelectedIndex();
            switch(idx) {
                case 0: // 6x6
                    return 6;
                case 1:
                    return 8;
                case 2:
                    return 10;
            }
            return 0; // shouldnt reach this
    }
    
    /*
     * Respond to commands, including exit
     * On the exit command, cleanup and notify that the MIDlet has been destroyed.
     */
    public void commandAction(Command c, Displayable s) {
        if(s == MainMenu) {
            // get selected element and process choice
            if(c == ExitCommand) {
                // the user wants to exit apllication, let him proceed
                destroyApp(false);
                notifyDestroyed();
            }
            if(c == SelectCommand) {
                // user has selected something, retrieve what was selected and proceed accordingly
                switch(MainMenu.getSelectedIndex()) {
                    case 0: // New Game
                        BoardSize = GetBoardSize();
                        Game = new Board(ExitCommand, this, BoardSize, MainMenu);
                        display.setCurrent(Game);
                        DeviceControl.setLights(0, 100); // activate the backlight
                        break;
                    case 1: // Options
                        display.setCurrent(OptionsScreen);
                        break;
                    case 2: // Help
                        break;
                    case 3: // About
                        display.setCurrent(AboutScreen, MainMenu);
                        break;
                }
            }
            
        }
        else if(s == Game) {
            // keypress in game, only process softbutton presses
            if(c == ExitCommand) {
                display.setCurrent(MainMenu);
                DeviceControl.setLights(0, 0); // deactivate Backlight
                Game = null;
            }
        }
        else if(s == OptionsScreen) {
            if(c == ExitCommand) {
                // get selected board size
                display.setCurrent(MainMenu);
            }
        }
    }
    
}
