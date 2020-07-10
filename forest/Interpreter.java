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
				if (cmd.matches(VERB_USE, "cantine", null)) {
					output.addEventText("You take a swig from the cantine.  Refreshing!");
				} else if (cmd.matches(VERB_USE, "lots_of_trees", null)) {
					output.addEventText("There aren't any branches low enough for you to reach.");
				} else if (cmd.matches(VERB_USE, "weathered_sign", null)) {
					cmd.setAction(VERB_LOOK);
					interpretCommand(cmd);
				} else if (cmd.matches(VERB_USE, "swift_river", null)) {
					output.addEventText("You dive into river and swim as hard as you can.  The current is too strong for you and carries you down river and over a waterfall!");
					output.addEventText("** SPLASH! **");
					player.setCurrentRoom(gamedata.getRoom("the_waterfall"));
				} else if (cmd.matches(VERB_USE, "dummy_climbable_tree", null)) {
					output.addEventText("You grab the nearest branch and begin climbing high into the tree...");
					player.setCurrentRoom(gamedata.getRoom("in_dummy_tree"));
					performLook();
				} else if (cmd.matches(VERB_USE, "east_climbable_tree", null)) {
					output.addEventText("You grab the nearest branch and begin climbing high into the tree...");
					player.setCurrentRoom(gamedata.getRoom("in_east_bank_tree"));
					performLook();
				} else if (cmd.matches(VERB_USE, "west_climbable_tree", null)) {
					output.addEventText("You grab the nearest branch and begin climbing high into the tree...");
					player.setCurrentRoom(gamedata.getRoom("in_west_bank_tree"));
					performLook();
				} else if (cmd.matches(VERB_USE, "branches_to_west", null)) {
					output.addEventText("You take a deep breath and leap as far as you can!");
					output.addEventText("You land safely on a branch of the other tree.");
					player.setCurrentRoom(gamedata.getRoom("in_west_bank_tree"));
					performLook();
				} else if (cmd.matches(VERB_USE, "branches_to_east", null)) {
					output.addEventText("You take a deep breath and leap as far as you can!");
					output.addEventText("You land safely on a branch of the other tree.");
					player.setCurrentRoom(gamedata.getRoom("in_east_bank_tree"));
					performLook();
				} else if (cmd.matches(VERB_USE, "dense_foliage", null)) {
					output.addEventText("With your bare hands?  Too many splinters.");
				} else if (cmd.matches(VERB_USE, "dense_foliage", "pocket_knife")) {
					output.addEventText("It would take you months to cut through all this with that tiny little knife!");
				} else if (cmd.matches(VERB_USE, "dense_foliage", "prop_blade")) {
					output.addEventText("You pull out the prop blade and begin hacking away violently at the offending plant-life.");
					output.addEventText("With one final, satisfying ** CHOP ** you cut the last tangled vine blocking your path.");
					room.removeItem(gamedata.getItem("dense_foliage"));
					room.setExit(EXIT_SOUTH, "outside_cave");
				} else {
					// default error
					output.addEventText("You can't do that.");
				}
				break;
			case VERB_MOVE:
				if (cmd.matches(VERB_MOVE, "damaged_prop", null)) {
					output.addEventText("You tug at the prop blade until it finally breaks free in your hands.");
					room.removeItem(gamedata.getItem("damaged_prop"));
					player.addItem(gamedata.getItem("prop_blade"));
				} else
					output.addEventText("You can't move that.");
				break;
		}
	}
	
	private void performLook() {
		interpretCommand(new ParsedCommand(VERB_LOOK, null, null));
	}
	
	protected void roomEntered(Room entered_room) {
	}

	protected boolean checkKeys(Room room, Player player) {
		if (room.getId().equals("cave_entrance")) {
			if (player.hasItem("flashlight"))
				return true;
			else {
				output.addEventText("It's too dark.  You need a light source.");
				return false;
			}
		}
		return true;
	}

}