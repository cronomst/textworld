interface GameConstants {
	public static String ITEM_FILE = "items.txt";
	public static String ROOM_FILE = "rooms.txt";
	
	public static int EXIT_NORTH = 0;
	public static int EXIT_SOUTH = 1;
	public static int EXIT_EAST = 2;
	public static int EXIT_WEST = 3;
	public static int EXIT_UP = 4;
	public static int EXIT_DOWN = 5;
	public static int EXIT_TOTAL = 6;
	public static String[] EXIT_TEXT = {"North", "South", "East", "West", "Up", "Down"};

	// VERBS
	public static int VERB_UNKNOWN = 0;
	public static int VERB_LOOK =    1;
	public static int VERB_TAKE =    2;
	public static int VERB_USE =     3;
	public static int VERB_DROP =    4;
	public static int VERB_MOVE =    5;
	
	// Movement "go" verbs
	public static int VERB_GO_NORTH =100;
	public static int VERB_GO_SOUTH =101;
	public static int VERB_GO_EAST = 102;
	public static int VERB_GO_WEST = 103;
	public static int VERB_GO_UP =   104;
	public static int VERB_GO_DOWN = 105;
	
	// Misc commands
	public static int VERB_HELP = 500;
	public static int VERB_INVENTORY = 501;
}