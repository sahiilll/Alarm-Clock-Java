  
        import java.awt.*;
        import java.applet.*;
        import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
        import java.awt.event.ItemListener;
        import javax.swing.*;
        class NewEmpty extends JFrame
        {

    public static void main(String args[])
    {
         myswing first = new myswing();
         first.setTitle("Alarm");
         first.setSize(500, 500);
         first.setVisible(true);
         first.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         
    }
    
        }
class myswing extends JFrame implements ActionListener , ItemListener
{ JPanel mypanel;
JLabel name , time ; 
JTextField nme;
JComboBox hours,min,sec;
int hrs,mins,secs;
JButton ok = new JButton("ok");
String str =new String();

      public  myswing()
{
    mypanel = new JPanel();
    mypanel.setLayout(new FlowLayout());
   name = new JLabel("name");
   nme = new JTextField("enter the name of alarm" , 15);
   hours = new JComboBox();
   min = new JComboBox();
   sec = new JComboBox();
   
   for(int i=1;i<=24;i++)
   {
       hours.addItem(String.valueOf(i));
            }
 for(int i=1;i<=60;i++)
 {
     min.addItem(String.valueOf(i));
     sec.addItem(String.valueOf(i));
 }
 ok.addActionListener(this);
 hours.addItemListener(this);
 min.addItemListener(this);
 sec.addItemListener(this);
 mypanel.add(name);
 mypanel.add(nme);
 mypanel.add(new JLabel("hours"));
 mypanel.add(hours);
 mypanel.add(new JLabel("minutes"));
 mypanel.add(min);
 mypanel.add(new JLabel("seconds"));
 mypanel.add(sec);
 mypanel.add(ok);
 this.add(mypanel);
}
 
 

 

    @Override
    public void actionPerformed(ActionEvent e) {
        try{
        Runtime.getRuntime().exec("/bin/bash -c sleep ");
        }
        catch(Exception i)
        {
            System.out.println(i);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getSource()== hours)
        { str = (String)e.getItem();
      hrs = Integer.parseInt(str);
      
    }
      else if(e.getSource()==min)
      { str = (String)e.getItem();
         mins = Integer.parseInt(str);
       
    }
      else 
      {
          str = (String)e.getItem();
           secs = Integer.parseInt(str);
              
      }
        repaint();
   
       }
    public void paint(Graphics g)
    {
        g.drawString(" hours="+hrs+" minutes="+mins+" seconds="+secs, 10,300 );
}
}