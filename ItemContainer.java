import java.util.ArrayList;

abstract class ItemContainer {
	ArrayList<Item> contents;
	
	public ItemContainer() {
		contents = new ArrayList<Item>();
	}
	
	public Item findItem(String word) {
		for (Item i : contents) {
			if (i.matchesAlias(word))
				return i;
		}
		
		// If not found, return null.
		return null;
	}
	
/** This method will find the "best match" (meaning the most characters in this case) of an alias with
 *  a block of text from the command line.
 *  I don't forsee any major issues, but it does have a problem where it will always choose the
 *  object with the longer alias as object1 and the object with the shorter alias as object2 in
 *  cases where two objects are specified.  It's possible to fix this later by adding code to
 *  reexamine the original command line and determining the indexes of where each object alias starts
 *  in the string and, whichever is the lower index, becomes object1, while the other is set to be
 *  object2.
 */
	public Item findItem(StringBuilder line) {
		String alias_best_match = "";
		Item item_best_match = null;
		
		for (Item i : contents) {
			String match = i.containsAlias(line);
			if (match != null)
				if (match.length() > alias_best_match.length()) {
					item_best_match = i;
					alias_best_match = match;
				}
		}
		// Remove matched alias from command line.
		if (item_best_match != null) {
			int idx = line.indexOf(alias_best_match);
			line.replace(idx, idx + alias_best_match.length(), "");
		}
		// DEBUG
		//System.out.println("BEST MATCH: " + alias_best_match);
		//---------
		
		// If not found, null is returned.
		return item_best_match;
	}

	// Checks if the specified item is in this container.
	public boolean hasItem(Item i) {
		if (contents.indexOf(i) < 0)
			return false;
		else
			return true;
	}
	public boolean hasItem(String id) {
		for (Item i : contents) {
			if (i.getId().equals(id))
				return true;
		}
		return false;
	}
	
	public void addItem(Item i) {
		contents.add(i);
	}
	
	public void removeItem(String id) {
		for (Item i : contents) {
			if (id.equals(i.getId())) {
				contents.remove(i);
				break;
			}				
		}
	}
	
	public void removeItem(Item i) {
		contents.remove(i);
	}
	
	public String[] getNameArray() {
		String[] result = new String[0];// = new String[contents.size()];
		ArrayList<String> array = new ArrayList<String>(contents.size());
		for (int i=0; i<contents.size(); i++) {
			// Only return names of items that are visible
			Item itm = contents.get(i);
			if (!itm.hasFlag(Item.INVISIBLE_FLAG))
				array.add(itm.getName());
		}
		// Convert the ArrayList to an array and return it
		result = array.toArray(result);

		return result;
	}
	
	public String[] getIdArray() {
		String[] result = new String[contents.size()];
		for (int i=0; i<result.length; i++)
			result[i] = contents.get(i).getId();
		
		return result;
	}
}