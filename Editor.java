import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.event.*;

class Editor extends JFrame implements GameConstants, ListSelectionListener, ActionListener {
	
	HashMap<String, Item> items;
	HashMap<String, Room> rooms;
	
	final int FIELD_WIDTH = 40;
	
	// Item pane objects
	final JTextField item_name_text = new JTextField(FIELD_WIDTH);
	final JTextField item_id_text = new JTextField(FIELD_WIDTH);
	final JTextArea item_description_text = new JTextArea(8, FIELD_WIDTH);
	final JTextField item_alias_text = new JTextField(FIELD_WIDTH);
	final JTextField item_takemsg_text = new JTextField(FIELD_WIDTH);
	final JTextField item_dropmsg_text = new JTextField(FIELD_WIDTH);
	final JTextField item_roommsg_text = new JTextField(FIELD_WIDTH);
	final JTextField item_usealts_text = new JTextField(FIELD_WIDTH);
	final JCheckBox item_fixed_cb = new JCheckBox("Fixed (item can't be taken)");
	final JCheckBox item_invisible_cb = new JCheckBox("Invisible (item not shown in item list)");
	final JCheckBox item_nodrop_cb = new JCheckBox("No Drop");
	
	// Room pane objects
	final JTextField room_name_text = new JTextField(FIELD_WIDTH);
	final JTextField room_id_text = new JTextField(FIELD_WIDTH);
	final JTextArea room_description_text = new JTextArea(8,FIELD_WIDTH);
	final JTextField[] room_exits = new JTextField[EXIT_TOTAL];
	final JTextField room_add_item_text = new JTextField(20);
	
	final JList item_list = new JList();
	final JList room_list = new JList();
	final JList room_contents_list = new JList();
	
	
	public static void main(String[] args) {
		new Editor();
	}
	
	public Editor() {
		super("Editor");
		
		init();
		JTabbedPane tp = new JTabbedPane();
		tp.addTab("Items", createItemPanel());
		tp.addTab("Rooms", createRoomPanel());
		add(tp);
		/*
		setLayout(new FlowLayout());
		add(createItemPanel());
		add(createRoomPanel());
		*/
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void init() {
		items = new HashMap<String, Item>();
		rooms = new HashMap<String, Room>();
		
		loadFiles();
		updateLists();
	}
	
	public void updateLists() {
		item_list.setListData(getItemListArray());
		room_list.setListData(getRoomListArray());
	}
	
	public JPanel createItemPanel() {
		JPanel item_panel = new JPanel();
		Box box = Box.createVerticalBox();
		Box list_box = Box.createVerticalBox();
		JButton item_update_button = new JButton("Update");
		JButton save_button = new JButton("Save");
		JButton item_delete_button = new JButton("Delete item");
		
		JScrollPane list_scroll = new JScrollPane(item_list,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		item_update_button.addActionListener(this);
		item_update_button.setActionCommand("update_item");
		save_button.addActionListener(this);
		save_button.setActionCommand("save");
		item_delete_button.addActionListener(this);
		item_delete_button.setActionCommand("delete_item");
		
		item_fixed_cb.setAlignmentX(item_id_text.getAlignmentX());
		item_invisible_cb.setAlignmentX(item_id_text.getAlignmentX());
		item_nodrop_cb.setAlignmentX(item_id_text.getAlignmentX());
		item_update_button.setAlignmentX(item_fixed_cb.getAlignmentX());
		
		item_list.setVisibleRowCount(15);
		item_list.addListSelectionListener(this);
		item_list.setPrototypeCellValue("long name of object id"); // Force the list to be kind of wide
		
		item_description_text.setLineWrap(true);
		item_description_text.setWrapStyleWord(true);
		
		item_id_text.setBorder(BorderFactory.createTitledBorder("ID"));
		item_name_text.setBorder(BorderFactory.createTitledBorder("Name"));
		item_description_text.setBorder(BorderFactory.createTitledBorder("Description"));
		item_alias_text.setBorder(BorderFactory.createTitledBorder("Aliases"));
		item_takemsg_text.setBorder(BorderFactory.createTitledBorder("TAKE message"));
		item_dropmsg_text.setBorder(BorderFactory.createTitledBorder("DROP message"));
		item_roommsg_text.setBorder(BorderFactory.createTitledBorder("Room message"));
		item_usealts_text.setBorder(BorderFactory.createTitledBorder("Alternative USE command"));
		list_scroll.setBorder(BorderFactory.createTitledBorder("Items"));
		
		box.add(item_id_text);
		box.add(item_name_text);
		box.add(item_description_text);
		box.add(item_alias_text);
		box.add(item_takemsg_text);
		box.add(item_dropmsg_text);
		box.add(item_roommsg_text);
		box.add(item_usealts_text);
		box.add(item_fixed_cb);
		box.add(item_invisible_cb);
		box.add(item_nodrop_cb);
		box.add(item_update_button);
		
		list_box.add(list_scroll);
		list_box.add(save_button);
		list_box.add(Box.createVerticalStrut(25));
		list_box.add(item_delete_button);

		item_panel.add(list_box);
		item_panel.add(box);

		return item_panel;
	}
	
	public JPanel createRoomPanel() {
		JPanel room_panel = new JPanel();
		Box box = Box.createVerticalBox();
		Box exit_box = Box.createVerticalBox();
		Box list_box = Box.createVerticalBox();
		Box room_contents_box = Box.createVerticalBox();
		
		JButton update = new JButton("Update");
		JButton save = new JButton("Save");
		JButton delete = new JButton("Delete");
		JButton add_item = new JButton("Add item");
		JButton remove_item = new JButton("Remove item");
		
		JScrollPane list_scroll = new JScrollPane(room_list,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		/*
		JScrollPane contents_list_scroll = new JScrollPane(room_contents_list,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		*/
		room_list.setVisibleRowCount(15);
		room_contents_list.setVisibleRowCount(5);
		
		for (int i=0; i<EXIT_TOTAL; i++) {
			room_exits[i] = new JTextField();
			exit_box.add(room_exits[i]);
		}
		
		update.setAlignmentX(0.5f);
		update.addActionListener(this);
		update.setActionCommand("update_room");
		save.addActionListener(this);
		save.setActionCommand("save");
		delete.addActionListener(this);
		delete.setActionCommand("delete_room");
		add_item.addActionListener(this);
		add_item.setActionCommand("add_item_to_room");
		add_item.setAlignmentX(0.5f);
		remove_item.addActionListener(this);
		remove_item.setActionCommand("remove_item_from_room");
		remove_item.setAlignmentX(0.5f);
		
		room_description_text.setLineWrap(true);
		room_description_text.setWrapStyleWord(true);

		room_list.addListSelectionListener(this);
		
		room_id_text.setBorder(BorderFactory.createTitledBorder("Room ID"));
		room_name_text.setBorder(BorderFactory.createTitledBorder("Name"));
		room_description_text.setBorder(BorderFactory.createTitledBorder("Description"));
		exit_box.setBorder(BorderFactory.createTitledBorder("Exit room IDs (N,S,E,W,U,D)"));
		list_scroll.setBorder(BorderFactory.createTitledBorder("Rooms"));
		//contents_list_scroll.setBorder(BorderFactory.createTitledBorder("Room contents"));

		box.add(room_id_text);
		box.add(room_name_text);
		box.add(room_description_text);
		box.add(exit_box);
		box.add(update);
		
		list_box.add(list_scroll);
		list_box.add(save);
		list_box.add(Box.createVerticalStrut(25));
		list_box.add(delete);
		
		//room_contents_box.add(contents_list_scroll);
		room_contents_box.add(room_contents_list);
		room_contents_box.add(room_add_item_text);
		room_contents_box.add(add_item);
		room_contents_box.add(remove_item);
		
		room_panel.add(list_box);
		room_panel.add(box);
		room_panel.add(room_contents_box);
		
		return room_panel;
	}
	
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource().equals(item_list)) {
			String id = (String)item_list.getSelectedValue();
			setItemData(items.get(id));
		} else if (e.getSource().equals(room_list)) {
			String id = (String)room_list.getSelectedValue();
			setRoomData(rooms.get(id));
		}
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("update_item")) {
			Item i = new Item(item_name_text.getText(),
				item_description_text.getText(),
				item_id_text.getText(),
				item_alias_text.getText(),
				item_fixed_cb.isSelected());
			i.setTakeMsg(item_takemsg_text.getText());
			i.setDropMsg(item_dropmsg_text.getText());
			i.setRoomMsg(item_roommsg_text.getText());
			i.setUse(item_usealts_text.getText());
			i.setFlag(Item.INVISIBLE_FLAG, item_invisible_cb.isSelected());
			i.setFlag(Item.NODROP_FLAG, item_nodrop_cb.isSelected());
			
			items.put(i.getId(), i);
			updateLists();
		} else if (e.getActionCommand().equals("save")) {
			saveFiles();
		} else if (e.getActionCommand().equals("delete_item")) {
			Item i;
			if (item_list.getSelectedValue() != null) {
				String key = (String)item_list.getSelectedValue();
				i = items.get(key);
				removeItemFromRooms(i);
				items.remove(key);
				updateLists();
			}
		} else if (e.getActionCommand().equals("update_room")) {
			Room rm = rooms.get(room_id_text.getText());
			if (rm==null)
				rm = new Room();
			
			rm.setId(room_id_text.getText());
			rm.setName(room_name_text.getText());
			rm.setDescription(room_description_text.getText());
			
			for (int i=0; i<EXIT_TOTAL; i++)
				rm.setExit(i, room_exits[i].getText());
			rooms.put(rm.getId(), rm);
			updateLists();
		} else if (e.getActionCommand().equals("add_item_to_room")) {
			Item item = items.get(room_add_item_text.getText());
			Room room = rooms.get(room_id_text.getText());
			if (item != null && room != null)
				room.addItem(item);
			if (item==null)
				JOptionPane.showMessageDialog(this, "That item does not exist.");
			else if (room == null)
				JOptionPane.showMessageDialog(this, "The room specified in Room ID does not yet exist in the room list.");
			setRoomData(room);
				
		} else if (e.getActionCommand().equals("remove_item_from_room")) {
			String id = (String)room_list.getSelectedValue();
			Room room = rooms.get(id);
			Item item = items.get((String)room_contents_list.getSelectedValue());
			if (item != null)
				room.removeItem(item);
			setRoomData(room);			
		} else if (e.getActionCommand().equals("delete_room")) {
			Room r;
			if (room_list.getSelectedValue() != null) {
				String key = (String)room_list.getSelectedValue();
				r = rooms.get(key);
				rooms.remove(key);
				updateLists();
			}
		}

	}
	
	// Removes the specified item from all of the rooms.
	// Used when an item is being deleted from the item list.
	public void removeItemFromRooms(Item item) {
		Set<String> keys = rooms.keySet();
		for (String k : keys) {
			Room r = rooms.get(k);
			r.removeItem(item);
		}
	}
	
	public void setItemData(Item item) {
		if (item==null)
			return;
		item_id_text.setText(item.getId());
		item_name_text.setText(item.getName());
		item_description_text.setText(item.getDescription());
		item_alias_text.setText(item.getAlias());
		item_takemsg_text.setText(item.getTakeMsg());
		item_dropmsg_text.setText(item.getDropMsg());
		item_roommsg_text.setText(item.getRoomMsg());
		item_usealts_text.setText(item.getUse());
		item_fixed_cb.setSelected(item.getFixed());
		item_invisible_cb.setSelected(item.hasFlag(Item.INVISIBLE_FLAG));
		item_nodrop_cb.setSelected(item.hasFlag(Item.NODROP_FLAG));
	}
	
	public void setRoomData(Room room) {
		if (room==null)
			return;
		room_id_text.setText(room.getId());
		room_name_text.setText(room.getName());
		room_description_text.setText(room.getDescription());

		for (int i=0; i<EXIT_TOTAL; i++) {
			room_exits[i].setText(room.getExit(i));
		}
		room_contents_list.setListData(room.getIdArray());
	}
	
	public String[] getItemListArray() {
		String[] list = new String[items.size()];
		int i=0;
		Set<String> keys = items.keySet();
		for (String key : keys) {
			list[i] = key;
			i++;
		}
		Arrays.sort(list);
		return list;
	}
	public String[] getRoomListArray() {
		String[] list = new String[rooms.size()];
		int i=0;
		Set<String> keys = rooms.keySet();
		for (String key : keys) {
			list[i] = key;
			i++;
		}
		Arrays.sort(list);
		return list;
	}
	
	public void saveFiles() {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(ITEM_FILE));
			Set<String> keys = items.keySet();
			for (String key : keys)
				items.get(key).writeItem(out);
			out.close();
		
			out = new DataOutputStream(new FileOutputStream(ROOM_FILE));
			keys = rooms.keySet();
			for (String key : keys)
				rooms.get(key).writeRoom(out);
			out.close();
		} catch (IOException e) {
			System.err.println("Error while saving: " + e);
		}
	}

	public void loadFiles() {
		try {
			DataInputStream in = new DataInputStream(new FileInputStream(ITEM_FILE));
			try {
				while (true) {
					Item new_item = Item.readItem(in);
					items.put(new_item.getId(), new_item);
				}
			} catch (EOFException eof) {}
			in.close();
			
			in = new DataInputStream(new FileInputStream(ROOM_FILE));
			try {
				while (true) {
					Room new_room = Room.readRoom(in, items);
					rooms.put(new_room.getId(), new_room);
				}
			} catch (EOFException eof) {}
			in.close();
			
		} catch (IOException e) {
			/*
			System.err.println(e);
			System.exit(0);
			*/
		}
	}
}