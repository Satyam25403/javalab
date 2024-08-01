//*********KEYLISTENER INTERFACE***********
// when input is received from keyboard ==>KeyEvent ==>KeyListener interface
import java.awt.*;
import java.awt.event.*;
class myframe extends Frame{ 
	
	myframe(){ 
		this.setSize(400,400);
		this.setVisible(true);
		this.setBackground(Color.green);
		this.addKeyListener(new keyboardimpl());
	}
}
class keyboardimpl implements KeyListener{
	public void keyTyped(KeyEvent e){ 
		System.out.println("key typed "+e.getKeyChar());
	}
	public void keyPressed(KeyEvent e){ 
		System.out.println("key pressed "+e.getKeyChar());
	}
	public void keyReleased(KeyEvent e){ 
		System.out.println("key released "+e.getKeyChar());
	}
}
class Key_Listener{ 
	public static void main(String[] args){ 
		myframe f=new myframe();
	}
}
