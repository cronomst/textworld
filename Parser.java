class Parser implements GameConstants {
	public Parser() {
	}
	
	public ParsedCommand parseString(String cmd) {
		// Convert to lowercase for comparison purposes.
		cmd = cmd.toLowerCase();
		// Remove extra words and punctuation.
		cmd = removePrepositions(cmd);
		cmd = removePunct(cmd);
		
		StringBuilder line = new StringBuilder(cmd);
		
		Player player = GameData.getGameData().getPlayer();
		Room room = player.getCurrentRoom();
		
		String[] splitcmd = cmd.split(" ");
		int action = 0; // Action value (specified in GameConstants)
		String verb = null; // Verh used by player
		String objectid1 = null;
		String objectid2 = null;
		Item object1 = null;
		Item object2 = null;
		
		if (splitcmd.length > 0)
			verb = splitcmd[0];
		if (splitcmd.length > 1)
			objectid1 = splitcmd[1];
		if (splitcmd.length > 2)
			objectid2 = splitcmd[2];
		if (splitcmd.length > 3)
			objectid2 = splitcmd[3];
		

		object1 = player.findItem(line);
		if (object1==null)
			object1 = room.findItem(line);
		if (object1 != null)
		
		object2 = player.findItem(line);
		if (object2==null)
			object2 = room.findItem(line);

/*		
		object1 = player.findItem(objectid1);
		if (object1==null)
			object1 = room.findItem(objectid1);
		
		object2 = player.findItem(objectid2);
		if (object2==null)
			object2 = room.findItem(objectid2);
*/
		action = getAction(verb, object1, object2);
		
		// Trying to look at an item that doesn't exist?  Make the player
		// look at the no_item.  Otherwise, they'd end up looking at the room again.
		// A bad design, but... meh...
		if (action==VERB_LOOK && objectid1 != null && object1 == null) {
			object1 = GameData.getGameData().getItem("no_item");
		}
			
		return new ParsedCommand(action, object1, object2);
	}
	
	/* Returns the "action" integer representing the verb. */
	private int getAction(String verb, Item obj1, Item obj2) {
		int action;
		
		if (verb.equals("look") || verb.equals("examine") || verb.equals("ex") || verb.equals("l"))
			action = VERB_LOOK;
		else if (verb.equals("take") || verb.equals("get"))
			action = VERB_TAKE;
		else if (verb.equals("drop"))
			action = VERB_DROP;
		else if (verb.equals("use") ||
			(obj1 != null && obj1.matchesUse(verb)) ||
			(obj2 != null && obj2.matchesUse(verb)))
			action = VERB_USE;
		else if (verb.equals("move") || verb.equals("pull") || verb.equals("push"))
			action = VERB_MOVE;
		else if (verb.equals("north") || verb.equals("n"))
			action = VERB_GO_NORTH;
		else if (verb.equals("south") || verb.equals("s"))
			action = VERB_GO_SOUTH;
		else if (verb.equals("east") || verb.equals("e"))
			action = VERB_GO_EAST;
		else if (verb.equals("west") || verb.equals("w"))
			action = VERB_GO_WEST;
		else if (verb.equals("up") || verb.equals("u"))
			action = VERB_GO_UP;
		else if (verb.equals("down") || verb.equals("d"))
			action = VERB_GO_DOWN;
		else if (verb.equals("inventory") || verb.equals("inv") || verb.equals("i"))
			action = VERB_INVENTORY;
		else if (verb.equals("help") || verb.equals("?") || verb.equals("h"))
			action = VERB_HELP;
		else
			action = VERB_UNKNOWN;
		
		return action;
	}
	
	private String removePunct(String cmd) {
		StringBuilder s = new StringBuilder();
		for (int i=0; i<cmd.length(); i++) {
			if (cmd.charAt(i) != '.') {
				s.append(cmd.charAt(i));
			}
		}
		return s.toString();
	}
	
	private String removePrepositions(String cmd) {
		cmd = cmd.replaceAll(" the ", " ");
		cmd = cmd.replaceAll(" a ", " ");
		cmd = cmd.replaceAll(" at ", " ");
		cmd = cmd.replaceAll(" to ", " ");
		cmd = cmd.replaceAll(" onto ", " ");
		cmd = cmd.replaceAll(" on ", " ");
		cmd = cmd.replaceAll(" across ", " ");
		cmd = cmd.replaceAll(" in ", " ");
		cmd = cmd.replaceAll(" into ", " ");
		return cmd;
	}
}