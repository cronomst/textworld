import java.util.*;

class TextInterface implements GameInterface, GameConstants {
	boolean playing;
	final Parser parser = new Parser();
	final Interpreter interp = new Interpreter(this);
	
	public static void main(String[] args) {
		new TextInterface();
	}
	
	public TextInterface() {
		playing = true;
		gameLoop();
	}
	
	public void gameLoop() {
		Scanner in = new Scanner(System.in);
		String input;
		ParsedCommand cmd;
		
		GameData.getGameData().genLists();
		
		// Perform initial "look" when player starts the game.
		cmd = parser.parseString("look");
		interp.interpretCommand(cmd);

		// Begin game loop
		while (playing) {
			System.out.print(">");
			input = in.nextLine();
			
			if (input.equalsIgnoreCase("QUIT"))
				playing = false;
			else if (input.startsWith("port")) {
				try {
					GameData.getGameData().movePlayerToRoom(input.substring(5));
					input = "look";
				} catch (ArrayIndexOutOfBoundsException e) {}
			}
				
			if (playing && !input.trim().equals("")) {
				cmd = parser.parseString(input);
				interp.interpretCommand(cmd);
			}
		}
	}
	
	public void setRoomText(String text) {
		System.out.println("\n\n" + GameUtil.wordWrap(text) + "\n\nObvious Exits:");
	}
	public void setRoomExit(int exitdirection, boolean passable, String dest_name) {
		if (passable) {
			System.out.print("[" + EXIT_TEXT[exitdirection] + "]");
			if (dest_name != null)
				System.out.print(" - " + dest_name);
			System.out.println();
		}
	}
	public void setRoomItems(String[] text) {
		if (text.length > 0) {
			System.out.println("You see:");
			for (int i=0; i<text.length; i++)
				System.out.println(" + " + text[i]);
		}
		System.out.println();
	}
	public void setInventoryItems(String[] itemtext) {
		System.out.println("You are carrying:");
		if (itemtext.length > 0) {
			for (int i=0; i<itemtext.length; i++)
				System.out.println(itemtext[i]);
		} else
			System.out.println("nothing");
		System.out.println();
	}
	public void addEventText(String text) {
		if (text != null)
			System.out.println(GameUtil.wordWrap(text) + "\n");
		else
			System.out.println();
	}
}