//*****MOUSELISTENER INTERFACE**********
// mouse related event ==> MouseEvent ==> MouseListener interface
//javap java.awt.event.MouseListener
import java.awt.*;
import java.awt.event.*;
class myframe extends Frame implements MouseListener{ 
	String[] msg=new String[5];
	myframe(){ 
		this.setSize(500,500);
		this.setVisible(true);
		this.addMouseListener(this);
	}
	public void mouseClicked(MouseEvent e){ 
		msg[0]="mouse clicked......("+e.getX()+","+e.getY()+")";
		repaint();
	}
	public void mousePressed(MouseEvent e){ 
		msg[1]="mouse pressed......("+e.getX()+","+e.getY()+")";
		repaint();
	}
    public void mouseReleased(MouseEvent e){ 
	   msg[2]="mouse released......("+e.getX()+","+e.getY()+")";
       repaint();
    }
    public void mouseEntered(MouseEvent e){ 
	   msg[3]="mouse entered......("+e.getX()+","+e.getY()+")";
       repaint();
    }
    public void mouseExited(MouseEvent e){ 
		msg[4]="mouse exited......("+e.getX()+","+e.getY()+")";
		repaint();
    }
	public void paint(Graphics g){ 
		int X=50;
		int Y=100;
		for(int i=0;i<msg.length;i++){ 
			if(msg[i]!=null){ 
				g.drawString(msg[i],X,Y);
				Y=Y+50;
			}
		}
	}
}
class Mouse_Listener{
	public static void main(String[] args){ 
	   new myframe();
    }
} 