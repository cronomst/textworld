import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class ConsolePane extends JPanel implements KeyListener, Runnable {
	String text;
	int caret_stopper = 0;
	final int MAX_LINES = 30;
	boolean cursor_on;
	long cursor_tick;
	final int CURSOR_RATE = 250;
	final static boolean COLOR_ON = true;
	ConsoleListener listener;
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Console");
		ConsolePane cp = new ConsolePane();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(cp);
		frame.addKeyListener(cp);
		frame.pack();
		frame.setVisible(true);
	}
	
	public ConsolePane() {
		init();
	}
	public ConsolePane(ConsoleListener listener) {
		this.listener = listener;
		init();
	}
	
	private void init() {
		Font font = new Font("Monospaced", Font.PLAIN, 14);
		Thread thread = new Thread(this);
		
		thread.start();
		
		setFont(font);
		setPreferredSize(new Dimension(640,480));
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
		
		text = "";
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (text != null) {
			String[] line_array = text.split("\n");
			int font_height = (int)(getFontMetrics(getFont()).getHeight()/1.5);

			if (COLOR_ON) {
				paintColorText(g);
				// DRAW CURSOR WITH COLOR TURNED ON
				if (cursor_on && hasFocus()) {
					String nctext = stripColor(text); // No color text
					int cx=0, cy=0;
					int i = line_array.length-1;
					if (nctext.length() > 0 && nctext.charAt(nctext.length()-1)=='\n') {
						cx = 10;
						cy = 10 + (getLineCount(nctext)*font_height);
					} else {
						cx = 10 + getFontMetrics(getFont()).stringWidth(stripColor(line_array[i]));
						cy = 10 + ((i+1)*font_height);
					}
					g.fillRect(cx+2, cy-font_height, 1, font_height);
				}
			} else {
				for (int i=0; i<line_array.length; i++) {
					g.drawString(line_array[i], 10, 10 + ((i+1) * (font_height)));
				}
	
				if (cursor_on && hasFocus()) {
					int cx=0, cy=0;
					int i = line_array.length-1;
					if (text.length() > 0 && text.charAt(text.length()-1)=='\n') {
						cx = 10;
						cy = 10 + (getLineCount(text)*font_height);
					} else {
						cx = 10 + getFontMetrics(getFont()).stringWidth(line_array[i]);
						cy = 10 + ((i+1)*font_height);
					}
					g.fillRect(cx+2, cy-font_height, 1, font_height);
				}
			}
		}
	}
	
	private void paintColorText(Graphics g) {
		String[] line_array = text.split("\n");
		Color[] colors = {Color.white, new Color(255, 157, 111), Color.green, Color.cyan, Color.red, Color.yellow};
		int font_height = (int)(getFontMetrics(getFont()).getHeight()/1.5);
		int font_width = (getFontMetrics(getFont())).stringWidth("X");
		g.setColor(Color.WHITE);
		for (int i=0; i<line_array.length; i++) {
			int write_pos = 0;
			for (int c=0; c<line_array[i].length(); c++) {
				if (line_array[i].charAt(c)=='`' && c < line_array[i].length()-1) {
					int col = Integer.parseInt(line_array[i].charAt(c+1) + "");
					try {
						g.setColor(colors[col]);
					} catch (ArrayIndexOutOfBoundsException e) {}
					c+=1;
				} else {
					g.drawString(line_array[i].charAt(c)+"", 10 + (write_pos*font_width), 10 + ((i+1) * (font_height)));
					write_pos++;
				}
			}
		}

	}
	
	private int getLineCount(String s) {
		int count = 1;
		for (int i=0; i<s.length(); i++) {
			if (s.charAt(i)=='\n')
				count++;
		}
		return count;
	}
	
	public int getMaxLines() {
		if (getHeight() < 10)
			return MAX_LINES;

		int font_height = (int)(getFontMetrics(getFont()).getHeight()/1.5);
		return (int)Math.floor(getHeight()/font_height)-1;
	}
	
	public String getText() { return text; }
	public void setText(String s) {
		text = GameUtil.wordWrap(s);
		while (getLineCount(text) > getMaxLines()) {
			int cut = text.indexOf('\n');
			text = text.substring(cut+1);
			caret_stopper-=cut;
		}
		repaint();
	}
	public void appendOutput(String s) {
		setText(text + s);
		caret_stopper=text.length();
	}
	public void appendOutputLn(String s) {
		setText(text + s + "\n");
		caret_stopper=text.length();
	}
	public void appendInput(char c) {
		text = GameUtil.wordWrap(text + c);
		repaint();
	}
	public void backspace() {
		if (text.length() > caret_stopper)
			text = text.substring(0, text.length()-1);
		repaint();
	}
	
	public void inputEntered(String s) {
		if (listener != null)
			listener.textEntered(s);
	}
	
	public String stripColor(String colored_string) {
		StringBuilder new_string = new StringBuilder();
		for (int i=0; i<colored_string.length(); i++) {
			if (colored_string.charAt(i)=='`')
				i++;
			else
				new_string.append(colored_string.charAt(i));
		}
		return new_string.toString();
	}
	
	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();
		if (Character.isIdentifierIgnorable(c) == false && c != '\n' && c!='`')
			appendInput(e.getKeyChar());
	}
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_BACK_SPACE:
				backspace();
				break;
			case KeyEvent.VK_ENTER:
			{
				String s = text.substring(caret_stopper, text.length());
				appendOutput("\n");
				inputEntered(s);
			}
				break;
		}
	}
	public void keyReleased(KeyEvent e) {}
	
	public void run() {
		// Flash cursor
		while (true) {
			if (System.currentTimeMillis() - cursor_tick > CURSOR_RATE) {
				cursor_tick = System.currentTimeMillis();
				cursor_on = !cursor_on;
				repaint();
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {}
			
		}
	}
}