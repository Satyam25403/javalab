import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Lockapp extends JFrame
{
	JButton[] b;
	String password="123456",typed="",fake=""; 
	String[] nums= {"1","2","3","4","5","6","7","8","9","CLEAR","0","Enter"};
	JTextField tx;
	JPanel top,bottom;
	Lockapp()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//add a display panel to frame
		top=new JPanel(new FlowLayout());
		tx=new JTextField(43);
		tx.setHorizontalAlignment(JTextField.CENTER);
		tx.setEditable(false);
		tx.setText("CLOSE");
		top.add(tx);
		this.add(top,BorderLayout.NORTH);

		//create buttons panel
		bottom=new JPanel(new GridLayout(4,3));
		b=new JButton[12];
		for(int i=0;i<12;i++)
		{
			b[i]=new JButton(nums[i]);
			//add buttons to panel
			bottom.add(b[i]);
			b[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent a){
					String act=a.getActionCommand();
					if(act.equals("CLEAR"))
					{
						tx.setText("CLOSE");
						fake="";
						typed="";
					}
					else if(!act.equals("Enter"))		//a number
					{
						typed+=act;
						fake+="*";
						tx.setText(fake);
					}
					else if(act.equals("Enter"))
					{
						if(checkPIN(typed))
						{
							unlock();
						}
						else
						{
							lock();
						}
					}
				}
			});
		}
		//add button panel to frame
		this.add(bottom);
	}
	public void lock()
	{
		tx.setText("WRONG PIN");
		fake="";
		typed="";
	}
	public void unlock()
	{
		tx.setText("OPEN");
		fake="";
		typed="";
	}
	public boolean checkPIN(String PIN)
	{
		return PIN.equals(password)?true:false;
	}
	
	public static void main(String[] args) 
	{
		Lockapp f=new Lockapp();
		f.setVisible(true);
		f.setSize(450,300);
		f.setTitle("electronic lock");
		
	}	
}
