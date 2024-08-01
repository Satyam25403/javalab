//*********ACTIONLISTENER**********
//javap java.awt.event.ActionListener
// Button ==> ActionEvent ==>ActionListener interface
// MENUITEMS ==> ActionEvent ==>ActionListener interface
import java.awt.*;
import java.awt.event.*;
class myframe extends Frame implements ActionListener{ 
	TextField tx1,tx2,tx3;
	Label l1,l2,l3;
	Button b1,b2;
	int result;

	//for menu
	String label="";
	MenuBar mb;
	Menu m1,m2,m3;
	MenuItem mi1,mi2,mi3;
	myframe()
	{ 
		this.setSize(250,400);
		this.setVisible(true);
		this.setBackground(Color.green);
		this.setLayout(new FlowLayout());

		l1=new Label("first value");
		l2=new Label("second value");
		l3=new Label("result");

		tx1=new TextField(25);
		tx2=new TextField(25);
		tx3=new TextField(25);

		b1=new Button("add");
		b2=new Button("mul");
		b1.addActionListener(this);
		b2.addActionListener(this);

		//for menu
		mb=new MenuBar();
		this.setMenuBar(mb);
		m1=new Menu("new");
		m2=new Menu("option");
		m3=new Menu("edit");
		mb.add(m1);
		mb.add(m2);
		mb.add(m3);

		mi1=new MenuItem("open");
		mi2=new MenuItem("save");
		mi3=new MenuItem("saveas");
		mi1.addActionListener(this);
		mi2.addActionListener(this);
		mi3.addActionListener(this);

		//add menuitems to the first menu
		m1.add(mi1);
		m1.add(mi2);
		m1.add(mi3);

		this.add(l1); this.add(tx1); this.add(l2);
		this.add(tx2); this.add(l3); this.add(tx3);
		this.add(b1); this.add(b2);
	}
	
	public void actionPerformed(ActionEvent e){ 
		try{
			int fval=Integer.parseInt(tx1.getText());
			int sval=Integer.parseInt(tx2.getText());
			String label=e.getActionCommand();
			if(label.equals("add")){ 
				result=fval+sval;
			}
			if(label.equals("mul")){ 
				result=fval*sval;
			}
			tx3.setText(""+result);
	    }catch(Exception ee){
	    	ee.printStackTrace();
	    }
		//for menu
		label=e.getActionCommand();
      	repaint();
	}
}
class action_Listener{
	public static void main(String[] args){ 
	   myframe f=new myframe();
	}
}
