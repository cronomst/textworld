class GameUtil {
	
	public static void main(String[] args) {
		/*
		String s = "Staring through the jail cell door you can see the adjacent room of this "+
			"dungeon, a strange locked doorway opposite this cell and the area appears to "+
			"lead both East and West away from this room.  The door itself is comprised "+
			"of rusty iron, probably weak enough to break with the appropriate tools.  "+
			"Unfortunately for you, you have no such things.";
			*/
		String s = "This is a test.";
		System.out.println(wordWrap(s, 40));
	}
	
	public static String wordWrap(String s) {
		return wordWrap(s, 79);
	}
	public static String wordWrap(String s, int count) {
		int last_space = 0;
		int last_cr = 0;
		int pos = 0;
		char[] c = s.toCharArray();
		while (pos < s.length()) {
			if (c[pos]==' ')
				last_space = pos;
			if (c[pos]=='\n')
				last_cr = pos;
			if ((pos-last_cr) >= count && (pos - last_space) < count) {
				c[last_space] = '\n';
				pos = last_space;
				last_cr = last_space;
				last_space = 0;
			}
			pos++;
		}
		
		/*
		// Create a new string, split it around \n, trim each segment, and then reassemble.
		String[] seg = new String(c).split("\n");
		String new_string = "";
		// Don't trim the last line
		for (int i=0; i<seg.length-1; i++) {
			seg[i] = seg[i].trim();
		}
		for (int i=0; i<seg.length; i++) {
			new_string+=seg[i] + "\n";
		}
		if (new_string.length() > 0)
			new_string = new_string.substring(0, new_string.length()-1);
		
		return new_string;
		*/
		return new String(c);
	}
}