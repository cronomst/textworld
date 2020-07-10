class ParsedCommand {
	int action;
	Item object1;
	Item object2;
	
	public ParsedCommand(int action, Item object1, Item object2) {
		this.action = action;
		this.object1 = object1;
		this.object2 = object2;
	}
	
	public void setAction(int action) {
		this.action = action;
	}
	public void setObject1(Item object1) {
		this.object1 = object1;
	}
	public void setObject2(Item object2) {
		this.object2 = object2;
	}
	
	public int getAction() { return action; }
	public Item getObject1() { return object1; }
	public Item getObject2() { return object2; }
	
	public boolean matches(int act, String id1, String id2) {
		String objid1 = null;
		String objid2 = null;
		
		// Take care of null values by setting them to empty strings.
		if (object1 != null)
			objid1 = object1.getId();
		else
			objid1 = "";
		if (object2 != null)
			objid2 = object2.getId();
		else
			objid2 = "";
		if (id1 == null)
			id1 = "";
		if (id2 == null)
			id2 = "";
		
		if (act != action)
			return false;
		if (id1.equals(objid1) && id2.equals(objid2))
			return true;
		else if (id1.equals(objid2) && id2.equals(objid1))
			return true;
		else
			return false;
	}
}