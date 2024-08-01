// two approaches to create a frame 1)creating Object of Frame class 2)extending Frame class.  
//ctrl+c ==> to close window forcefully

import java.awt.*;

public class adv_java {
    public static void main(String[] args) {
        Frame f=new Frame(); //frame creation
	    f.setVisible(true); //now frame is visible by default not visible
	    f.setSize(400,400);//default frame is 0 pixel width and 0 pixel height
	    f.setBackground(Color.green); //set the background
	    f.setTitle("VRSEC"); //set the title of the frame

        Label l1 = new Label("UserName :");
	    Label l2 = new Label("Password :");
        
	    TextField tx1=new TextField (30);
	    TextField  tx2=new TextField (30);
        tx2.setEchoChar('*');

        TextArea tx=new TextArea(10,20);
        
	    Button b=new Button("login");
	    f.add(l1);
	    f.add(tx1);
	    f.add(l2);
	    f.add(tx2);
	    f.add(b);
	    //if runned upto here you will see only "login" button ,to overcome this
	    //you need to use layouts concept ,1.flow 2.border 3.grid 4.card
	    //layouts are used to display the components in the specific order
	    //flow layout place all the components row by row
        
        f.setLayout(new FlowLayout());
    }
}
