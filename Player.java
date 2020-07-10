import java.util.HashMap;

class Player extends ItemContainer implements GameConstants {
	Room current_room;
	HashMap<String, String> flags;
	
	public Player() {
		flags = new HashMap<String, String>();
	}
	
	public Room getCurrentRoom() {
		return current_room;
	}
	public void setCurrentRoom(Room r) {
		current_room = r;
	}
	
	public String getFlag(String key) {
		return flags.get(key);
	}
	public void setFlag(String key, String val) {
		flags.put(key, val);
	}
	public void removeFlag(String key) {
		flags.remove(key);
	}
	
	public boolean move(int dir) {
		Room new_room;
		// Make sure that the exit for this direction is not an empty string.
		if (current_room.getExit(dir) != null && !current_room.getExit(dir).equals("")) {
			new_room = GameData.getGameData().getRoom(current_room.getExit(dir));
			if (new_room != null) {
				current_room = new_room;
				return true;
			}
		}
		return false;
	}
	
	public String getExitMessage(int exit) {
		String exitmsg = current_room.getExit(exit);
		if (exitmsg != null && exitmsg.startsWith("/msg:")) {
			return exitmsg.substring(5);
		} else
			return "You can't go that way.";
	}
}