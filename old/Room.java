public Room extends GameObject {
	Exit[] exits;
	boolean dark; // if true, room and exit descriptions are not shown unless there is a light source.
	boolean indoors;
	
	public Room() {
		exits = new Exit[Exit.EXIT_COUNT];
	}
	
	public void setDark(boolean d) {
		dark = d;
	}
	/**
	 *Returns the value of the dark property without regard to light source objects in the room.
	 */
	public boolean getDark() {
		return dark;
	}
	/**
	 * If the room is naturally dark, this first checks to see if the room contains a light source.
	 * If so, it will return false.  Otherwise it will return the value of the dark property.
	 */
	public boolean isDark() {
		boolean is_dark = dark;
		if (is_dark) {
			// Check if room contains a light source.  If so, set is_dark to false.
		}
		return is_dark;
	}
	public void setIndoors(boolean i) {
		indoors = i;
	}
	public boolean isIndoors() {
		return indoors;
	}

	public class Exit {
		static public int NORTH = 0;
		static public int SOUTH = 1;
		static public int EAST = 2;
		static public int WEST = 3;
		static public int UP = 4;
		static public int DOWN = 5;
		static public int EXIT_COUNT = 6;

		public String link; // id of the room to which this exit connects
		public String key; // Object idea required to pass through exit.
		public Exit(String l, String k) {
			link = l;
			key = k;
		}
	}
}