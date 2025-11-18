import java.awt.*;
import java.awt.event.*;

class MyFrame extends Frame implements ActionListener { 
    Label l1, l2;
    TextField tx1, tx2;
    TextArea tx;
    Button b;
    String status = "";

    MyFrame() { 
        //Frame settings
        setSize(400, 400);
        setTitle("Login Frame");
        setBackground(Color.red);
        setLayout(new FlowLayout()); // Set layout before adding components

        // Initialize components
        l1 = new Label("user name:");
        l2 = new Label("password:");
        tx1 = new TextField(25);
        tx2 = new TextField(25);
        tx2.setEchoChar('*');           //as soon as a component is added add its corresponding functionality
        b = new Button("login");
        b.addActionListener(this);

        tx=new TextArea(10,20);
        tx.setText("amulya");
        System.out.println(tx.getText());
        // Add components to the frame
        this.add(l1);
        this.add(tx1);
        this.add(l2);
        this.add(tx2);
        this.add(b);
        this.add(tx);

        // Make the frame visible only after adding all components
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) { 
        String uname = tx1.getText();
        String upwd = tx2.getText();
        if(uname.equals("bjln") && upwd.equals("bjln")){
            status = "login success";
        }
        else{
            status = "login failure";
        }
        repaint();      //calls paint internally
    }

    @Override
    public void paint(Graphics g) {
        Font f = new Font("Arial", Font.BOLD, 30);
        g.setFont(f);
        this.setForeground(Color.green);
        g.drawString("Status: " + status, 50, 300);
    }
}

public class Focus_listener { 
    public static void main(String[] args) { 
        new MyFrame();
    }
}