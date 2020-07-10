import java.io.*;

class Item {
	final static int FIXED_FLAG = 1; // Set if the item cannot be picked up.
	final static int INVISIBLE_FLAG = 1<<1; // Set if the item should not be visible on the room item list.
	final static int NODROP_FLAG = 1<<2; // Set if the item cannot be dropped
	String name;
	String description;
	String id;
	int flags;
	String alias; // Word used by the player to refer to the object.
	
	String take_msg;
	String drop_msg;
	String room_msg; // Message appended to room description when this item is in the room.
	String use_alts; // Alternative verbs for this object that can be used in place of "use." ex: rather than USE BUTTON, PUSH BUTTON.
	
	public Item() {
		init();
	}
	
	public Item(String n, String d, String i, String al, boolean fix) {
		name = n;
		description = d;
		id = i;
		setFlag(FIXED_FLAG, fix);
		alias = al;
		init();
	}
	
	private void init() {
		take_msg = "";
		drop_msg = "";
		room_msg = "";
		use_alts = "";
	}
		
	public String getName() { return name; }
	public String getDescription() { return description; }
	public String getId() { return id; }
	public boolean getFixed() { return hasFlag(FIXED_FLAG); }
	public String getAlias() { return alias; }
	public String getTakeMsg() { return take_msg; }
	public String getDropMsg() { return drop_msg; }
	public String getRoomMsg() { return room_msg; }
	public String getUse() { return use_alts; }
	
	public void setUse(String use) {
		if (use==null)
			use = "";
		use_alts = use;
	}
	public void setTakeMsg(String take) {
		if (take==null)
			take = "";
		take_msg = take;
	}
	public void setDropMsg(String drop) {
		if (drop==null)
			drop="";
		drop_msg = drop;
	}
	public void setRoomMsg(String msg) {
		if (msg==null)
			msg = "";
		room_msg = msg;
	}
	
	public void setFlag(int flag, boolean val) {
		if (val==true)
			flags = flags | flag; // OR new flag into flags
		else
			flags = ((flags | flag) ^ flag); // XOR flag from flags (but OR it first, just in case).
	}
	public void setFlags(int f) {
		flags = f;
	}
	public boolean hasFlag(int flag) {
		if ((flags & flag) == flag)
			return true;
		else
			return false;
	}
	
	/* Checks if the given word matches the alias for this item.  Returns true if it does. */
	public boolean matchesAlias(String obj) {
		String[] aliases = alias.split(",");
		
		for (int i=0; i<aliases.length; i++)
			if (aliases[i].equalsIgnoreCase(obj))
				return true;
				
		return false;
	}
	
	public String containsAlias(StringBuilder line) {
		String[] aliases = alias.split(",");
		sortLongestToShortest(aliases); // This way, the best matches (most characters) are found first.
		
		if (line == null)
			return null;
			
		for (int i=0; i<aliases.length; i++)
			if (line.indexOf(aliases[i]) > 0) { // Check > 0 because we don't want the verb to be mistaken for an object (verbs should always be at index 0).
//------ Needs to be moved into the ItemContainer code so that the words aren't removed until the best
//       item match is found.
//				int idx = line.indexOf(aliases[i]);
//				line.replace(idx, idx + aliases[i].length(), "");
//----------------------------------------------------------------------------
				return aliases[i];
			}
				
		return null;
	}
	
	public void sortLongestToShortest(String[] aliases) {
		for (int i=0; i<aliases.length; i++) {
			for (int j=i+1; j<aliases.length; j++) {
				if (aliases[j].length() > aliases[i].length()) {
					String temp = aliases[j];
					aliases[j] = aliases[i];
					aliases[i] = temp;					
				}
			}
		}
	}
	
	/* Checks if the given verb matches the custom "use" verb for this item.*/
	public boolean matchesUse(String verb) {
		String[] verbs = use_alts.split(",");
		
		for (int i=0; i<verbs.length; i++)
			if (verbs[i].equalsIgnoreCase(verb))
				return true;
				
		return false;
/*
		if (verb != null && use_alts != null) {
			if (verb.equalsIgnoreCase(use_alts))
				return true;
		}
		return false;
*/
	}
	
	public boolean equals(Object rhs) {
		if (rhs instanceof Item)
			return id.equals(((Item)rhs).getId());
		else
			return false;
	}
	
	public static Item readItem(DataInputStream in) throws IOException {
		Item new_item = new Item();
		
		new_item.id = in.readUTF();
		new_item.name = in.readUTF();
		new_item.description = in.readUTF();
		new_item.alias = in.readUTF();
		new_item.take_msg = in.readUTF();
		new_item.drop_msg = in.readUTF();
		new_item.room_msg = in.readUTF();
		new_item.use_alts = in.readUTF();
		//new_item.fixed = in.readBoolean();
		new_item.flags = in.readInt();
		//new_item.setFlag(FIXED_FLAG, new_item.fixed);
		
		return new_item;
	}
	
	public void writeItem(DataOutputStream out) throws IOException {
		out.writeUTF(id);
		out.writeUTF(name);
		out.writeUTF(description);
		out.writeUTF(alias);
		out.writeUTF(take_msg);
		out.writeUTF(drop_msg);
		out.writeUTF(room_msg);
		out.writeUTF(use_alts);
		//out.writeBoolean(fixed);
		out.writeInt(flags);
	}
}