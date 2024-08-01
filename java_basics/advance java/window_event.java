// Window ==> WindowEvent ==>WindowListener interface
//javap java.awt.event.WindowListener

import java.awt.*;
import java.awt.event.*;
class MyFrame extends Frame{ 

    MyFrame(){ 
        this.setSize(400,500);
        this.setVisible(true);
        this.setTitle("my frame");
        this.addWindowListener(new myclassimpl());
    }

}
class myclassimpl implements WindowListener{ 

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
    }
    public void windowClosing(WindowEvent e){ 
        System.out.println("window is closing");
        System.exit(0);
    }
    public void windowOpened(WindowEvent e){
        System.out.println("window Opened");
    }
}

class window_event{ 
    public static void main(String[] args){ 

        MyFrame f=new MyFrame();

    }
}


