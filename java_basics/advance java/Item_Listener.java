//***************ITEM LISTENER***************
//RadioButton ==> allows to select only one item(ex:male or female)
//CheckBox  ==> allows to select multiple items
//List ==>allows to select multiple items
//Choice ==> allows to select only one item
 
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
class Item_Listener{ 
    public static void main(String[] args){ 
	    Frame f=new Frame(); //frame creation
	    f.setVisible(true); //now frame is visible by default not visible
	    f.setSize(400,400); //set the size of the frame
	    f.setBackground(Color.green); //set the background
	    f.setTitle("myframe"); //set the title of the frame
	    
        
	    Label l1=new Label("Qualifications :");
	    Label l2=new Label("Gender :");
        
	    //checkbox
        Checkbox cb1=new Checkbox ("10th",true); 
	    Checkbox  cb2=new Checkbox ("inter");
	    Checkbox  cb3=new Checkbox ("B.tech");
	    Checkbox  cb4=new Checkbox ("M.tech");
        System.out.println(cb1.getLabel());	
        
	    //using CheckBoxGroup class, and CheckBox class we can create RadioButtons
	    CheckboxGroup cg= new CheckboxGroup();
	    Checkbox c1=new Checkbox ("male",cg,true);
	    Checkbox c2=new Checkbox ("female",cg,false);
        
	    System.out.println(c1.getLabel());
	    Label a1 = new Label("Technologies :");
	    Label a2 = new Label("City :");
        
	    List l=new List(1,true); //no.of items to show at a time , to select multiple items
	    l.add("java");
	    l.add("c");
	    l.add("python"); //by default add method always adds the component at last
	    l.add("cpp");
	    //l.remove("cpp");
	    l.add("php",0);
	    //l.remove(0);
        
	    System.out.println(l.getItemCount());   //to return the number of options available
	    System.out.println(l.getItem(0));
        
	    Choice ch = new Choice();
	    ch.add("Hyderabad");
	    ch.add("chennai");
	    ch.add("bangalore");
	    //ch.remove("chennai");
	    //ch.remove(0);
	    //ch.insert("pune",1);
        
	    System.out.println(ch.getItemCount());          //to return the number of options available
	    System.out.println(ch.getItem(0));
	    //javap java.awt.List
	    //javap java.awt.Choice
        
        
        
        
	    f.add(l1);
	    f.add(cb1);
	    f.add(cb2);
	    f.add(cb3);
	    f.add(cb4);
	    f.add(l2);
	    f.add(c1);
	    f.add(c2);


	    f.add(a1);
	    f.add(l);
	    f.add(a2);
	    f.add(ch);
        f.setLayout(new FlowLayout());
        f.addWindowListener(new Listenerimpl());
        
        
    }
}
// y using WindowAdaptor class we can close the frame. Internally WindowAdaptor
// class implements WindowListener interface. 
// Hence WindowAdaptor class contains empty implementation of abstract methods.
class Listenerimpl extends WindowAdapter
{ 
	public void windowClosing(WindowEvent we)
	{ 
	 System.exit(0);
	}
}
