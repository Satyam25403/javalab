//*****MOUSELISTENER INTERFACE**********
// mouse related event ==> MouseEvent ==> MouseListener interface
//javap java.awt.event.MouseListener
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
class myframe extends JFrame { 
	
	myframe(){ 
		this.setSize(500,500);
		this.setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){ 
				System.out.println("mouse clicked......("+e.getX()+","+e.getY()+")");
			}
			public void mousePressed(MouseEvent e){ 
				System.out.println("mouse pressed......("+e.getX()+","+e.getY()+")");
			}
			public void mouseReleased(MouseEvent e){ 
				System.out.println("mouse released......("+e.getX()+","+e.getY()+")");
			}
			public void mouseEntered(MouseEvent e){ 
				System.out.println("mouse entered......("+e.getX()+","+e.getY()+")");
			}
			public void mouseExited(MouseEvent e){ 
				System.out.println("mouse exited......("+e.getX()+","+e.getY()+")");
			}
		});
	}
	
}
class Mouse_Listener{
	public static void main(String[] args){ 
	   new myframe();
    }
} 