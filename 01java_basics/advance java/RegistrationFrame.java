import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class RegistrationFrame extends JFrame implements ActionListener {
	JLabel l1, l2, l3, l4, l5, l6, l7;
	JTextField tf;
	JTextArea ta;
	JPasswordField pf;
	JCheckBox cb1, cb2, cb3;
	JRadioButton rb1, rb2;
	JList<String> l;
	JComboBox<String> cb;
	JButton b;
	Container c;
	String sname = "";
	String spwd = "";
	String squal = "";
	String sgender = "";
	String stechs = "";
	String sbranch = "";
	String saddr = "";
	
	JFrame displayFrame;    
	JTextArea displayArea; 
	RegistrationFrame() {

		c = this.getContentPane();
		this.setVisible(true);

		this.setSize(500, 500);
		this.setTitle("RegistrationFrame  Example");
		c.setBackground(Color.green);
		this.setLayout(new FlowLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		l1 = new JLabel("Student name : ");
		l2 = new JLabel("Student Password: ");
		l3 = new JLabel("Student Qualification: ");
		l4 = new JLabel("Student Gender: ");
		l5 = new JLabel("Student Techs ");
		l6 = new JLabel("Inst.Brach");
		l7 = new JLabel("Address");

		tf = new JTextField(20);
		pf = new JPasswordField(20);
		cb1 = new JCheckBox("bsc", false);
		cb2 = new JCheckBox("mca", false);
		cb3 = new JCheckBox("phd", false);
		rb1 = new JRadioButton("male", false);
		rb2 = new JRadioButton("female", false);
		ButtonGroup bg = new ButtonGroup();
		bg.add(rb1);
		bg.add(rb2);
		String[] techs = { "c", "c++", "java", ".net" };
		l = new JList<String>(techs);
		String[] branch = { "Hyd", "chennai", "Bangalore", "pune" };
		cb = new JComboBox<String>(branch);
		ta = new JTextArea(5, 20);
		b = new JButton("Registration");
		

		c.add(l1);
		c.add(tf);
		c.add(l2);
		c.add(pf);
		c.add(l3);
		c.add(cb1);
		c.add(cb2);
		c.add(cb3);
		c.add(l4);
		c.add(rb1);
		c.add(rb2);
		c.add(l5);
		c.add(l);
		c.add(l6);
		c.add(cb);
		c.add(l7);
		c.add(ta);
		c.add(b);
		
		b.addActionListener(this);
		
        displayFrame = new JFrame("User Information");
        displayFrame.setSize(400,300);
        displayFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayFrame.add(new JScrollPane(displayArea));
	}
	public void actionPerformed(ActionEvent e) {
		sname = tf.getText();
		spwd = pf.getText();
		if (cb1.isSelected() == true)
			squal = squal + cb1.getLabel() + " ";
		if (cb2.isSelected() == true)
			squal = squal + cb2.getLabel() + " ";
		if (cb3.isSelected() == true)
			squal = squal + cb3.getLabel() + " ";
		if (rb1.isSelected() == true)
			sgender = sgender + rb1.getLabel() + " ";
		if (rb2.isSelected() == true)
			sgender = sgender + rb2.getLabel() + " ";
		Object[] obj = l.getSelectedValues();
		for (Object o : obj)
			stechs = stechs + o.toString() + " ";
		sbranch = (String) cb.getSelectedItem();
		saddr = ta.getText();

        displayArea.setText("");

        displayArea.append("Name: " + sname + "\n");
        displayArea.append("Password: " + spwd + "\n");
        displayArea.append("Qualification: " + squal + "\n");
        displayArea.append("Gender: " + sgender + "\n");
        displayArea.append("Techs: " + stechs + "\n");
        displayArea.append("Branch: " + sbranch + "\n");
        displayArea.append("Address: " + saddr);

        displayFrame.setVisible(true);		
	}
	
	public static void main(String[] args) {
		new RegistrationFrame();
	}
}
