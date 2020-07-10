import java.io.*;
import java.util.HashMap;

class Room extends ItemContainer implements GameConstants {
	String name;
	String description;
	String id;
	String[] exits; // ids of exits
	
	public Room() {
		init();
	}
	
	public Room(String n, String d, String i) {
		name = n;
		description = d;
		id = i;
		
		init();
	}
	
	private void init() {
		exits = new String[EXIT_TOTAL];
		/*
		for (int i=0; i<EXIT_TOTAL; i++)
			exits[i] = "";
			*/
	}
	
	public String getName() { return name; }
	public String getDescription() { return description; }
	public String getId() { return id; }
	
	public void setName(String n) { name = n; }
	public void setDescription(String d) { description = d; }
	public void setId(String i) { id = i; }

	public String getExit(int dir) {
		if (dir >= 0 && dir < EXIT_TOTAL)
			return exits[dir];
		else
			return null;
	}
	public void setExit(int dir, String i) {
		if (i=="")
			i=null;
		if (dir >= 0 && dir < EXIT_TOTAL)
			exits[dir] = i;
	}
	public boolean isValidExit(int dir) {
		String e = getExit(dir);
		if (e != null && !e.startsWith("/msg:"))
			return true;
		return false;
	}
	
	public String getRoomMessages() {
		String out = "";
		for (Item item : contents) {
			if (!item.getRoomMsg().equals(""))
				out+="\n" + item.getRoomMsg();
		}
		return out;
	}
	
	public static Room readRoom(DataInputStream in, HashMap<String, Item> items) throws IOException {
		Room new_room = new Room();
		int contents_size;
		
		new_room.id = in.readUTF();
		new_room.name = in.readUTF();
		new_room.description = in.readUTF();
		for (int i=0; i<EXIT_TOTAL; i++)
			new_room.setExit(i, in.readUTF());
		contents_size = in.readInt();
		for (int i=0; i<contents_size; i++)
			new_room.addItem(items.get(in.readUTF()));
		
		return new_room;
	}
	
	public void writeRoom(DataOutputStream out) throws IOException {
		out.writeUTF(id);
		out.writeUTF(name);
		out.writeUTF(description);
		for (int i=0; i<EXIT_TOTAL; i++) {
			if (exits[i]==null)
				out.writeUTF("");
			else
				out.writeUTF(exits[i]);
		}
		out.writeInt(contents.size());
		for (Item i : contents)
			out.writeUTF(i.getId());
	}
}