abstract class BasicInterpreter implements GameConstants {
	GameInterface output;
	
	public BasicInterpreter(GameInterface out) {
		output = out;
	}
	
	public void interpretCommand(ParsedCommand cmd) {
		Item obj1 = cmd.getObject1();
		Item obj2 = cmd.getObject2();
		Player player = GameData.getGameData().getPlayer();
		Room room = player.getCurrentRoom();

		switch (cmd.getAction()) {
			case VERB_LOOK:
				if (obj1==null) {
					// Show room
					boolean no_exits = true;
					output.setRoomText(room.getName() +
						"\n---\n" +
						room.getDescription() +
						room.getRoomMessages());
					for (int i=0; i<EXIT_TOTAL; i++) {
						if (!room.getExit(i).equals("")) {
							Room exitroom = GameData.getGameData().getRoom(room.getExit(i));
							String exitname;
							if (exitroom != null)
								exitname = exitroom.getName();
							else
								exitname = null;
							output.setRoomExit(i, true, exitname);
							no_exits = false;
						}
					}
					// Show "none" if no exits currently exist in this room.
					if (no_exits)
						output.addEventText("none");
					else
						output.addEventText(null);
					output.setRoomItems(room.getNameArray());
					/*
					if (room.getNameArray().length > 0)
						output.addEventText("You see:\n" + formatItemList(room.getNameArray())); // Probably needs to be removed for GUI later.
					*/
				} else {
					output.addEventText(obj1.getDescription());
				}
				break;
			case VERB_TAKE:
				if (obj1 != null) {
					if (player.hasItem(obj1))
						output.addEventText("You already have that!");
					else {
						if (obj1.getFixed()==false) {
							// Object picked up
							player.addItem(obj1);
							room.removeItem(obj1);
						}
						printTakeMsg(obj1); // Display this item's specific take message
					}
				} else
					output.addEventText("Take what?");
				
				break;
			case VERB_DROP:
				if (obj1 != null) {
					if (player.hasItem(obj1)) {
						// Only allow dropping of non-fixed objects
						if (obj1.hasFlag(Item.NODROP_FLAG)==false) {
							// Object dropped
							player.removeItem(obj1);
							room.addItem(obj1);
						}							
						printDropMsg(obj1); // Display this item's specific drop message
					} else
						output.addEventText("You don't have that!");
				} else
					output.addEventText("Drop what?");
				break;
			case VERB_GO_NORTH:
				movePlayer(player, EXIT_NORTH);
				break;
			case VERB_GO_SOUTH:
				movePlayer(player, EXIT_SOUTH);
				break;
			case VERB_GO_EAST:
				movePlayer(player, EXIT_EAST);
				break;
			case VERB_GO_WEST:
				movePlayer(player, EXIT_WEST);
				break;
			case VERB_GO_UP:
				movePlayer(player, EXIT_UP);
				break;
			case VERB_GO_DOWN:
				movePlayer(player, EXIT_DOWN);
				break;
			case VERB_USE:
				break;
			case VERB_MOVE:
				break;
			case VERB_HELP:
				output.addEventText(
					"Basic commands:\n" +
					"n,s,e,w,u,d - Movement (for example, type 'n' and press enter to go north)\n" +
					"i - Show your inventory\n\n" +
					"Basic verbs:\n" +
					"Look, Take, Drop, Use, Move.  Other verbs may work in certain situations, however.\n" +
					"To use two items together, type: 'Use object1 with object2' or something similar.\n\n" +
					"To look at your surroundings, type 'look' by itself."
				);
				break;
			case VERB_INVENTORY:
				{
					output.setInventoryItems(player.getNameArray());
					/*
					String[] item_names = player.getNameArray();
					String out = "\nYou are carrying:\n";
					if (item_names.length == 0)
						out+="nothing\n";
					out+= formatItemList(item_names);
					output.addEventText(out);
					*/
				}
				break;
			default:
				output.addEventText("You can't do that.");
				break;
		}
	}
	
	private void printTakeMsg(Item obj) {
		if (obj.getTakeMsg().equals("")) {
			if (obj.getFixed()==false)
				output.addEventText("You take " + obj.getName() + ".");
			else
				output.addEventText("You can't take that.");
		} else
			output.addEventText(obj.getTakeMsg());
	}
	
	private void printDropMsg(Item obj) {
		if (obj.getDropMsg().equals("")) {
			if (obj.hasFlag(Item.NODROP_FLAG)==false)
				output.addEventText("You drop " + obj.getName() + ".");
			else
				output.addEventText("You can't drop that.");
		} else
			output.addEventText(obj.getDropMsg());
	}

	private void movePlayer(Player player, int exit) {
		Room new_room = GameData.getGameData().getRoom(player.getCurrentRoom().getExit(exit));
		// Check to make sure the player has the approriate "key(s)" to enter the room
		if (new_room != null && checkKeys(new_room, player)==false)
			return;
			
		// If so, move the player.
		if (player.move(exit)) {
			interpretCommand(new ParsedCommand(VERB_LOOK, null, null));
			roomEntered(player.getCurrentRoom());
		} else
			output.addEventText(player.getExitMessage(exit));
	}
	
	// Override in Interpreter to allow special room entry events
	protected void roomEntered(Room entered_room) {
		// Empty
	}
	
	// Override in Interpreter to allow for key checking.
	protected boolean checkKeys(Room room, Player player) {
		return true;
	}
}