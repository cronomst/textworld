class GameObject {
	String id;
	String name;
	String description;
	boolean lightsource;
	Vector contents;
	
	public GameObject() {
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}