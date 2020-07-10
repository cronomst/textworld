import javax.swing.*;
import java.awt.event.*;

public class ConsoleApplet extends JApplet implements GameInterface,
													  ConsoleListener,
													  GameConstants,
													  FocusListener {
	final Parser parser = new Parser();
	final Interpreter interp = new Interpreter(this);
	final ConsolePane pane = new ConsolePane(this);
	
	public ConsoleApplet() {
	}
	
	public void init() {
		getContentPane().add(pane);
		pane.addKeyListener(pane);
		addFocusListener(this);
		getContentPane().addFocusListener(this);
		requestFocusInWindow();

		GameData.getGameData().genLists();
		
		// Perform initial "look" when player starts the game.
		ParsedCommand cmd = parser.parseString("look");
		interp.interpretCommand(cmd);

		/*
		pane.appendOutputLn("");
		pane.appendOutputLn("It’s probably about 5,000 pounds and bolted directly into the foundation of the dungeon.  Become a muscle-bound titan and come back for this one." + "\n");
		*/
		pane.appendOutput(">");
		
	}
	
	public void textEntered(String s) {
		ParsedCommand cmd = parser.parseString(s);
		interp.interpretCommand(cmd);
		pane.appendOutput(">");
	}

	public void setRoomText(String text) {
		pane.appendOutputLn("\n" + text + "\n\n`1Obvious Exits:`0");
	}
	public void setRoomExit(int exitdirection, boolean passable, String dest_name) {
		if (passable) {
			pane.appendOutput("[" + EXIT_TEXT[exitdirection] + "]");
			if (dest_name != null)
				pane.appendOutput(" - `2" + dest_name + "`0");
			pane.appendOutput("\n");
		}
	}
	public void setRoomItems(String[] text) {
		if (text.length > 0) {
			pane.appendOutputLn("`1You see:`0");
			for (int i=0; i<text.length; i++)
				pane.appendOutputLn(" + `5" + text[i] + "`0");
		}
		pane.appendOutput("\n");
	}
	public void setInventoryItems(String[] itemtext) {
		pane.appendOutputLn("`1You are carrying:`0");
		if (itemtext.length > 0) {
			for (int i=0; i<itemtext.length; i++)
				pane.appendOutputLn(" + `5" + itemtext[i] + "`0");
		} else
			pane.appendOutputLn("`4nothing`0");
		pane.appendOutput("\n");
	}
	public void addEventText(String text) {
		if (text != null) {
			pane.appendOutputLn(text + "\n");
		} else
			pane.appendOutput("\n");
	}
	
	public void focusGained(FocusEvent e) {
		pane.requestFocusInWindow();
	}
	public void focusLost(FocusEvent e) {}
}