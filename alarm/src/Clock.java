 


import java.awt.*;              // see above for details of
import java.util.*;             // the classes and methods used
import javax.swing.*;
public class Clock extends Frame implements Runnable
{

   Thread clockThread;          // thread to do animation etc
   int alarmh;                  // alarm hours (-1 if not set)
   int alarmm;                  // alarm minutes
   Image tickimages[];          // image array for tick-tock animations
   Image bongimages[];          // image array for alarm ringing
   int tick;                    // animation counter
   boolean bong = false;        // true when alarm ringing
   Button qb, ab;               // buttons to exit and set/stop alarm
   TextField tstr;              // displays current time (hh:mm:ss)
   TextField astr;              // displays alarm time (hh:mm)
   static final int ALARM_IMAGES = 6;   // number of alarm frames
   static final int TICK_IMAGES = 2;    // number of tick-tock frames
   static final int ALARM_TICK = 150;   // frame display time for alarm
   static final int CLOCK_TICK = 1000;  // frame dislay time for tick-tock

   public static void main(String args[])
   {
      new Clock();                      // make an alarm clock
   }
   // constructor
   public Clock() {
      super("Java Clock");

      Panel imgpanel;
      int i;
      // pick a common font
      Font f = new Font("TimesRoman", Font.PLAIN, 12);
      setFont(f);                       // give the Frame a font
      setBackground(Color.lightGray);   // and a background colour

      tick = 0;
     
      // layout the window
      setLayout(new BorderLayout());
      imgpanel = new Panel();
      qb = new Button("     Quit     ");        // Quit button
      ab = new Button("Set Alarm");             // Set/Stop alarm button
      tstr = new TextField("");         // time display
      tstr.setEditable(false);          // is read-only
      astr = new TextField("");         // and so is alarm display
      astr.setEditable(false);
      Panel q = new Panel();
      q.setLayout(new BorderLayout());
      q.add("North",tstr);
      q.add("South",astr);
      q.add("West",qb);
      q.add("East",ab);
    
      add("South",q);
      // next panel reserves space for the animation images
                       // hide it, so images will show
      pack();
      reshape(100,100,180,160);         // makes ourselves a reasonable size
      show();                           // make ourselves visible
      stopAlarm();                      // ensure alarm is off
      clockThread = new Thread(this);   // create background thread
      clockThread.start();              // and start it running
   }
   // this method turns a SMALL integer into a 2 character string
   // e.g. 12 -> "12" and 9 -> "09"
   String twoDigits(int i) {
      String s;
      s = "" + (100 + i);       // ensure it's got 3 digits
      return (s.substring(1));
   }

   public void setAlarm(int h, int m) {
      // set the alarm display to show the alarm time
      astr.setText("   Alarm set for " + h + ":" + twoDigits(m) + " ");
      alarmh = h; alarmm = m;   // remember the alarm time
      tick = 0;                 // just to play safe
      bong = false;             // we're not ringing
   }

   public void soundAlarm(String t) {
      // set the alarm display to show we're ringing
      astr.setText(" " + t + " alarm ringing!");
      ab.setLabel("Stop Alarm");// label the buttton "Stop"
      bong = true;              // we are ringing
   }

   public void stopAlarm() {
      // set the alarm display to show there isn't one set
      astr.setText("   Alarm not set");
      ab.setLabel("Set Alarm"); // label the button "Set"
      alarmh = -1;              // indicate there's no alarm
      tick = 0;                 // restart animations
      bong = false;             // we're not ringing
   }

   // Handle events that have occurred
   @Override
   public boolean handleEvent(Event evt)
   {
      switch(evt.id) {
         case Event.WINDOW_DESTROY:     // time to go
            System.exit(0);
            return true;
         default:
            // all other events get default handling
            return super.handleEvent(evt);
      }
   }

   @Override
   public boolean action(Event e, Object o) {
      int nh, nm;
      if (qb == e.target) {     // Quit button clicked
         clockThread.stop();    // kill the background thread
         clockThread = null;    // and forget it
         System.exit(0);        // then go
      } else if (ab == e.target) {      // Either Stop or Set
         if (bong) {            // if it's Stop
            stopAlarm();        // we stop it
         } else {               // else we ask for a new time
            Date d = new Date();
            nh = d.getHours();
            nm = d.getMinutes() + 1;
            SetAlarmDialog ab = new SetAlarmDialog(this," " + nh + ":" + 
               twoDigits(nm) + " ");
            ab.show();
         }
      }
      return true;
   }

   // this is the background animation thread, responsible for:
   //   - displaying the time each second
   //   - showing tick-tock animations when alarm is off
   //   - showing alarm animations when alarm is on
   public void run() {
      int nh, nm;
      // loop until the application dies
      while (clockThread != null) {
         // find out what time it is
         Date d = new Date();
         nh = d.getHours();
         nm = d.getMinutes();
         if (bong == false && alarmh == nh && alarmm == nm) {
             // it's time to ring the alarm
             soundAlarm("" + alarmh + ":" + twoDigits(alarmm));
         }
         // do whatever animation is needed
         repaint();
         // then wait a bit (depending on what state we're in
         try {
             clockThread.sleep(bong ? ALARM_TICK : CLOCK_TICK);
         } catch (InterruptedException e){
         }
       }
    }

    public void update(Graphics g) {
       paint(g); // don't clear the background
    }

    public void paint(Graphics g) {
        Date now = new Date();
        // display the time
        tstr.setText(now.getHours() + ":" + twoDigits(now.getMinutes()) + ":" + 
           twoDigits(now.getSeconds()));
        
        
    }
}

// this class handles getting a new Alarm time
class SetAlarmDialog extends Dialog
{
   TextField at;
   Clock parent;

   public SetAlarmDialog(Clock par, String deftime)
   {
      // make a dialog
      super(par, "Set Alarm Clock", true);
      // fill it up with all the necessary widgets
      TextField t;
      parent = par;
      t = new TextField("Enter new alarm time:");
      t.setEditable(false);
      add("West",t);
      at = new TextField(deftime);
      add("East",at);
      Panel p = new Panel();
      p.add("West", new Button("Set Alarm"));
      p.add("East", new Button("Cancel"));
      add("South",p);
      pack();                           // make it just fit
      resize(preferredSize());
      move(200,200);
   }

   public boolean handleEvent(Event evt)
   {
      String s;
      int h, m;
      switch(evt.id) {
         case Event.WINDOW_DESTROY:     // time to go
            dispose();
            return true;
         case Event.ACTION_EVENT:
            if ("Set Alarm".equals(evt.arg)) {
               // OK button was pressed
               // try to parse the time as "hh:mm" or "hh/mm" or "hh mm"
               StringTokenizer st = new StringTokenizer(at.getText(),":/ ");
               try {
                  // get the hour
                  s = st.nextToken();
                  // convert to an integer
                  h = Integer.parseInt(s);
                  // get the minute
                  s = st.nextToken();
                  // convert to an integer
                  m = Integer.parseInt(s);
               } catch (Exception e) {
                  // ignore errors
                  h = -1; m = -1;
               }
               // if valid time, reset the alarm
               if (h != -1)
                  parent.setAlarm(h,m);
               else
                  parent.stopAlarm();
            } else if ("Cancel".equals(evt.arg)) {
               // Cancel button was pressed - do nothi
            }
            dispose();
            return true;
         default:
            return false;
      }
   }
}

