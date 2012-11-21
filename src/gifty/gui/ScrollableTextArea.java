package gifty.gui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

public class ScrollableTextArea extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JScrollPane scrollPane;
	private JTextArea textArea;

	public ScrollableTextArea() {
		super();

		setLayout(new MigLayout("insets 0", "[grow]", "[grow]"));

		textArea = new JTextArea();
		scrollPane = new JScrollPane(textArea);
		add(scrollPane, "grow");

	}

	public String getText() {
		return textArea.getText();
	}

	public void setText(String text) {
		textArea.setText(text);
	}

	public void append(String text) {
		textArea.append(text);
	}

	public void clearText() {
		textArea.setText("");
	}

}
