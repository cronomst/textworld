class Interpreter extends BasicInterpreter {
	public Interpreter(GameInterface out) {
		super(out);
	}
	
	public void interpretCommand(ParsedCommand cmd) {
		super.interpretCommand(cmd);
		
		Item obj1 = cmd.getObject1();
		Item obj2 = cmd.getObject2();
		Player player = GameData.getGameData().getPlayer();
		Room room = player.getCurrentRoom();
		GameData gamedata = GameData.getGameData();
		
		switch (cmd.getAction()) {
			case VERB_USE:
				// Jail cell door / lock
				if (cmd.matches(VERB_USE, "locked_cell_door", null))
					output.addEventText("You pull on the bars of the door to no effect.  The cell door is locked tight.");
				else if (cmd.matches(VERB_USE, "lockpick", null))
					output.addEventText("The lockpick has to be used ON something.");
				else if (cmd.matches(VERB_USE, "lockpick", "locked_cell_door")) {
					// Use lockpick to unloack the door
					output.addEventText("After a great deal of work you manage to move the lock mechanism into the unlocked position.  You hear a satisfying *click.*");
					room.removeItem(gamedata.getItem("locked_cell_door"));
					room.addItem(gamedata.getItem("unlocked_cell_door"));
					room.setExit(EXIT_NORTH, "/msg:The door is closed.");
				} else if (cmd.matches(VERB_USE, "unlocked_cell_door", null)) {
					// Open the unlocked door
					output.addEventText("You swing the jail cell door outward.  Freedom!");
					room.removeItem(gamedata.getItem("unlocked_cell_door"));
					room.addItem(gamedata.getItem("opened_cell_door"));
					room.setExit(EXIT_NORTH, "the_gear_doors");
				} else if (cmd.matches(VERB_USE, "players_manual", null)) {
					cmd.setAction(VERB_LOOK);
					interpretCommand(cmd);
				} else if (cmd.matches(VERB_USE, "mechanical_door_on", null) ||
					cmd.matches(VERB_USE, "mechanical_door_off", null) ||
					cmd.matches(VERB_USE, "mechanical_door_with_gear", null) ||
					cmd.matches(VERB_USE, "mechanical_door_open", null)) {
					output.addEventText("Even if the entire door was working you have no idea how to open such a device and there is no handle or latch.");
				} else if (cmd.matches(VERB_USE, "steel_door", null)) {
					output.addEventText("The door is shut tight and there is no latch or handle to open it with.");
				} else if (cmd.matches(VERB_USE, "gearbox", null) ||
					cmd.matches(VERB_USE, "gearbox_with_gear", null)) {
					output.addEventText("There is nothing to use here and all the gears are securely fastened in place.");
				} else if (cmd.matches(VERB_USE, "lever_up", null)) {
					// Flip lever to down position.
					room.removeItem(gamedata.getItem("lever_up"));
					room.addItem(gamedata.getItem("lever_down"));
					// Swap door activation
					Room door_room = gamedata.getRoom("the_gear_doors");
					if (door_room.hasItem(gamedata.getItem("mechanical_door_on"))) {
						door_room.removeItem(gamedata.getItem("mechanical_door_on"));
						door_room.addItem(gamedata.getItem("mechanical_door_off"));
					} else {
						door_room.removeItem(gamedata.getItem("mechanical_door_open"));
						door_room.addItem(gamedata.getItem("mechanical_door_with_gear"));
						door_room.setExit(EXIT_NORTH, "/msg:The hatch is mechanically sealed.");
					}
					door_room.setExit(EXIT_EAST, "/msg:No exit linked to this yet, but the door is open.");
					door_room.removeItem(gamedata.getItem("steel_door"));
					door_room.addItem(gamedata.getItem("steel_door_open"));
					output.addEventText("You set the lever into the downward position.  Down the hall to the Northeast you can hear multiple gears clicking into motion and others shutting off.");
				} else if (cmd.matches(VERB_USE, "lever_down", null)) {
					// Flip lever to up position.
					room.removeItem(gamedata.getItem("lever_down"));
					room.addItem(gamedata.getItem("lever_up"));
					// Swap door activation
					Room door_room = gamedata.getRoom("the_gear_doors");
					if (door_room.hasItem(gamedata.getItem("mechanical_door_off"))) {
						door_room.removeItem(gamedata.getItem("mechanical_door_off"));
						door_room.addItem(gamedata.getItem("mechanical_door_on"));
					} else {
						door_room.removeItem(gamedata.getItem("mechanical_door_with_gear"));
						door_room.addItem(gamedata.getItem("mechanical_door_open"));
						door_room.setExit(EXIT_NORTH, "/msg:The door is open, but I haven't made the room that it links to yet.");
					}
					door_room.setExit(EXIT_EAST, "/msg:The door is mechanically sealed.");
					door_room.removeItem(gamedata.getItem("steel_door_open"));
					door_room.addItem(gamedata.getItem("steel_door"));
					output.addEventText("You set the lever into the upward position.  Down the hall to the Northeast you can hear multiple gears clicking into motion and others shutting off.");
				} else if (cmd.matches(VERB_USE, "gear", "gearbox")) {
					// Use the gear on the gearbox
					if (room.hasItem(gamedata.getItem("mechanical_door_on")))
						output.addEventText("No way!  The machinery is on.  You could lose an arm!");
					else {
						// Only do this if the mechanical door is turned off.
						output.addEventText("You insert the gear into the gearbox.  It fits perfectly!");
						player.removeItem(gamedata.getItem("gear"));
						room.removeItem(gamedata.getItem("gear")); // Just in case it's sitting in the room instead.
						room.removeItem(gamedata.getItem("mechanical_door_off"));
						room.addItem(gamedata.getItem("mechanical_door_with_gear"));
					}
				} else {
					// default error
					output.addEventText("You can't use that.");
				}
				break;
			case VERB_MOVE:
				if (cmd.matches(VERB_MOVE, "bedroll", null)) {
					// Moving the bedroll
					room.addItem(gamedata.getItem("lockpick"));
					room.removeItem(gamedata.getItem("bedroll"));
					room.addItem(gamedata.getItem("bedroll2"));
					output.addEventText("Pulling the bedroll aside reveals a primitive LOCKPICK!");
				} else if (cmd.matches(VERB_MOVE, "bedroll2", null)) {
						output.addEventText("No thanks.  You're still sticky from the last time you had to touch it.");
				} else if (cmd.matches(VERB_MOVE, "mechanical_door_on", null) ||
					cmd.matches(VERB_MOVE, "mechanical_door_off", null) ||
					cmd.matches(VERB_MOVE, "mechanical_door_with_gear", null) ||
					cmd.matches(VERB_MOVE, "mechanical_door_open", null)) {
					output.addEventText("The door understandably does not budge.");
				} else if (cmd.matches(VERB_MOVE, "steel_door", null)) {
					output.addEventText("You cannot move the door, it’s set in place and will not budge.");
				} else if (cmd.matches(VERB_MOVE, "gearbox", null) ||
					cmd.matches(VERB_MOVE, "gearbox_with_gear", null)) {
					output.addEventText("The GEARBOX is attached to the MECHANICAL HATCH and cannot be moved on its own.");
				} else if (cmd.matches(VERB_MOVE, "lever_up", null)) {
					cmd.setAction(VERB_USE);
					interpretCommand(cmd);
				} else if (cmd.matches(VERB_MOVE, "lever_down", null)) {
					cmd.setAction(VERB_USE);
					interpretCommand(cmd);
				// Default error message
				} else
					output.addEventText("You can't move that.");
				break;
		}
	}
	
	protected void roomEntered(Room entered_room) {
	}

	protected boolean checkKeys(Room room, Player player) {
		return super.checkKeys(room,player);
	}
}