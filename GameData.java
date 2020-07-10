import java.util.*;
import java.io.*;

class GameData implements GameConstants {
	// Singleton object
	static GameData gamedata;
	
	HashMap<String, Room> rooms;
	HashMap<String, Item> items;
	Player player;
	long play_start_time;
	
	static GameData getGameData() {
		if (gamedata==null)
			gamedata = new GameData();
		
		return gamedata;
	}
	
	private GameData() {
		rooms = new HashMap<String, Room>();
		items = new HashMap<String, Item>();
		player = new Player();
		
		play_start_time = System.currentTimeMillis();
		
		//genLists();
		
		//player.setCurrentRoom(getRoom("start"));
	}
	
	public void genLists() {
		// Non-item for when the player tries to look at an object that doesn't exist.
		Item noitem = new Item("nothing", "You don't see that here.", "no_item", "", true);
		items.put(noitem.getId(), noitem);
		
		try {
			//DataInputStream in = new DataInputStream(new FileInputStream(ITEM_FILE));
			DataInputStream in = new DataInputStream(getClass().getResourceAsStream(ITEM_FILE));
			try {
				while (true) {
					Item new_item = Item.readItem(in);
					items.put(new_item.getId(), new_item);
				}
			} catch (EOFException eof) {}
			in.close();
			
			in = new DataInputStream(getClass().getResourceAsStream(ROOM_FILE));
			try {
				while (true) {
					Room new_room = Room.readRoom(in, items);
					rooms.put(new_room.getId(), new_room);
				}
			} catch (EOFException eof) {}
			in.close();
			
		} catch (IOException e) {
			System.err.println(e);
			System.exit(0);
		}
		
		player.setCurrentRoom(getRoom("start"));
	}
	
	// Looks up the given room id in the hash map and returns the room.
	public Room getRoom(String id) {
		return rooms.get(id);
	}
	// Looks up the given item id in the hash map and returns the item.
	public Item getItem(String id) {
		return items.get(id);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public String getPlayTimeString() {
		long diff = System.currentTimeMillis() - play_start_time;
		long s = diff / 1000;
		String minutes = (s / 60) + ":";
		String seconds = (s % 60) + "";
		
		return minutes + seconds;		
	}
	
	public void movePlayerToRoom(String r) {
		Room rm = getRoom(r);
		if (rm != null)
			getPlayer().setCurrentRoom(rm);
	}
}