// Window ==> WindowEvent ==>WindowListener interface
//javap java.awt.event.WindowListener

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
class MyFrame extends JFrame{ 

    MyFrame(){ 
        this.setSize(400,500);
        this.setVisible(true);
        this.setTitle("my frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowListener(){ 

        public void windowActivated(WindowEvent e){ 
            System.out.println("window activated");
        }
        public void windowDeactivated(WindowEvent e){ 
            System.out.println("window deactivated");
        }
        public void windowIconified(WindowEvent e){ 
            System.out.println("window iconified");
        }
        public void windowDeiconified(WindowEvent e){ 
            System.out.println("window deiconified");
        }
        public void windowClosed(WindowEvent e){
            System.out.println("window closed");
            System.exit(0);
        }
        public void windowClosing(WindowEvent e){ 
            System.out.println("window is closing");
            
        }
        public void windowOpened(WindowEvent e){
            System.out.println("window Opened");
        }
        });
    }
}

class window_event{ 
    public static void main(String[] args){ 

        MyFrame f=new MyFrame();

    }
}


