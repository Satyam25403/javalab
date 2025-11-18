import javax.swing.*;
import java.awt.*;

class swings2 extends JFrame {
	JLabel L1, L2, L3, L4, L5, L6, L7;
	JTextField textfield;
	JPasswordField field;
	JCheckBox cb1, cb2, cb3;
	JRadioButton rb1, rb2;
	ButtonGroup group;
	JTextArea area;
	JComboBox box;
	JList list;
	JButton button;

	swings2() {
		setVisible(true);
		setSize(300, 400);
		setTitle("My Swing App");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		// getContentpane().setBackgroud(Color.red);

		L1 = new JLabel("User Name");
		L2 = new JLabel("password");
		L3 = new JLabel("Gender");
		L4 = new JLabel("Qualification");
		L5 = new JLabel("City");
		L6 = new JLabel("Technologies");
		L7 = new JLabel("Address");

		textfield = new JTextField(20);
		field = new JPasswordField(20);
		field.setToolTipText("Enter Password");

		rb1 = new JRadioButton("male", false);
		rb2 = new JRadioButton("female", false);

		group = new ButtonGroup();
		group.add(rb1);
		group.add(rb2);

		cb1 = new JCheckBox("10th", true);
		cb2 = new JCheckBox("inter", false);
		cb3 = new JCheckBox("B.Tech", false);

		String[] str = { "Hyderabad", "Vijayawada", "Bangalore" };
		box = new JComboBox(str);

		String[] s = { "Java", "Python", "Javascript" };
		list = new JList(s);

		area = new JTextArea(7, 7);

		button = new JButton("Register");

		add(L1);
		add(textfield);
		add(L2);
		add(field);
		add(L3);
		add(rb1);
		add(rb2);
		add(L4);
		add(cb1);
		add(cb2);
		add(cb3);
		add(L5);
		add(box);
		add(L6);
		add(list);
		add(L7);
		add(area);
		add(button);
	}

	public static void main(String[] args) {
		swings2 mf = new swings2();
	}
}