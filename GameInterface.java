interface GameInterface {
	public void setRoomText(String text);
	public void setRoomExit(int exitdirection, boolean passable, String dest_name);
	public void setRoomItems(String[] itemtext);
	public void setInventoryItems(String[] itemtext);
	public void addEventText(String text);
}