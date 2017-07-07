
// jBox applet is part of the MapServer client support
// package. - SDL -

// Parameters:
//    color int, int, int - color to use (rgb) for the selection rectangle.
//    jitter int - minimum size (in pixels) for a dragged box.
//    image string - url for the image to display, must be fully qualified.
//    thickness int - thickness (in pixels) of the dragged box.
//    cursorsize
//    name
//    busyimage
//    box on/off - status for box drawing, default is on
//    verbose true/false - turns on verbosity so that all mouse movements are passed out to javascript
//      (was on/off in mapplet)

// Public methods:
//    boxon, boxoff - turns box drawing on/off.
//    lineon, lineoff - turns line drawing on/off, and box drawing off, boxoff() turns off both
//    dragon, dragoff - turns image dragging on/off
//    setimage(string) - displays the image loaded from the passed url.
//    setcursor(string) - sets the cursor to one of crosshair, hand or default
//    version - returns jBox_version string

// javascript functions eval'd:
//    setbox_handler
//    seterror_handler
//    reset_handler
//    mouseenter_handler
//    mouseexit_handler
//    mousemove_handler
//    measure_handler(name, seg, tot, num, area)
//      seg = current segment length
//      tot = running total length
//      num = number of vertices
/*
   Rich Greenwood, rich@greenwoodmap.com
      November 2002:   added line functionality to mimic MapInfo/ArcView ruler tool
      October 2003:    added drag functionality for panning
      November 2003:   added area calculation
      January 2004:    moved evalThread into seperate class
      		           added version()
      May 2004:		  merged jBox & jBoxPNG - set boolean sixlegs true or false below

   Compiler issues:
      Sun JDK 1.2 & 1.3 work ok, but
      1.4 compiled classes won't run under MS VM (IE 5.5)?
      MS J++ won't compile due to un-caught exceptions?
*/

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import netscape.javascript.*;
/*
	Importing sixlegs is only required for sixlegs PNG support
	it can be commented out if you don't have, or don't require sixlegs
*/
import com.sixlegs.image.png.*;

public class jBoxPNG extends Applet implements MouseListener, MouseMotionListener {
   String    jBox_version = "1.1.1";
   boolean   busy=false, box=true, line=false, drag=false, init=true, verbose=false;
   Image     img, busyimg=null;
   double    x1=-1, y1=-1, x2=-1, y2=-1;
   double	 seg=0, tot=0, area=0;
   int       jitter=5, cursorsize=4, thickness=1, ix=0, iy=0;
   Color     color=Color.red;
   JSObject  window;
   Image     offScreenImage;
   Graphics  offScreenGraphics;
   Dimension screenSize;
   String    name;
   Polygon   pl;
	PngImage	 pngimg;

	public Image get_image(String s) {
		URL url=null;

      try {
          url = new URL(s);
      } catch(MalformedURLException e) {
			window.eval("seterror_handler('Applet error. Malformed image URL.');");
			this.stop();
      }
		// comment next line for sixlegs
		// return(getImage(url));

		/* begin sixlegs support ***/
		try {
			 pngimg = new PngImage(url);
		} catch(java.io.IOException e) {
			window.eval("seterror_handler('Applet error. IOException PngImage.');");
			this.stop();
		}
		return(Toolkit.getDefaultToolkit().createImage(pngimg));
		/*** end sixlegs */
	}

   public void init () {
      StringTokenizer st;
      String    s=null;
      URL url=null;

      screenSize = this.getSize(); // nab the applet size

      offScreenImage = createImage(screenSize.width, screenSize.height);
      offScreenGraphics = offScreenImage.getGraphics();

      // get the Navigator window handle
      window = JSObject.getWindow(this);

      s = getParameter("jitter");
      if(s != null)
         jitter = Integer.parseInt(s);

      s = getParameter("color");
      if(s != null)
         color = getColorParameter(s);

      s = getParameter("thickness");
      if(s != null)
         thickness = Integer.parseInt(s);

      s = getParameter("cursorsize");
      if(s != null)
         cursorsize = Integer.parseInt(s);

      s = getParameter("verbose");
      if(s != null) {
         if(s.equalsIgnoreCase("true")) verbose = true;
      }

      name = getParameter("name");

      s = getParameter("busyimage");
      if(s != null) {
         try {
            url = new URL(s);
         } catch(MalformedURLException e) {
            window.eval("seterror_handler('Applet error. Malformed image URL.');");
         	this.stop();
         }
         busyimg = getImage(url);
      }

      s = getParameter("box");
      if(s != null) {
         if(s.equalsIgnoreCase("false")) {
            box = false;
         }
      }
      s = getParameter("drag");
      if(s != null) {
         if(s.equalsIgnoreCase("true")) {
            drag = true;
            box = false;
	     }
      }

      // nab the image itself
      s = getParameter("image");
		img = get_image(s);

      // we want mouse events and mouse movement events
      addMouseListener(this);
      addMouseMotionListener(this);
      System.out.println("box="+box+",  drag="+drag );
   }   // end of init()

   private Color getColorParameter(String s) {
      StringTokenizer st;
      int r, g, b;

      // check if a pre-defined color is specified
      if (s.equalsIgnoreCase("black"))
          return(Color.black);
      if (s.equalsIgnoreCase("blue"))
          return(Color.blue);
      if (s.equalsIgnoreCase("cyan"))
          return(Color.cyan);
      if (s.equalsIgnoreCase("darkGray"))
          return(Color.darkGray);
      if (s.equalsIgnoreCase("gray"))
          return(Color.gray);
      if (s.equalsIgnoreCase("green"))
          return(Color.green);
      if (s.equalsIgnoreCase("lightGray"))
          return(Color.lightGray);
      if (s.equalsIgnoreCase("magenta"))
          return(Color.magenta);
      if (s.equalsIgnoreCase("orange"))
          return(Color.orange);
      if (s.equalsIgnoreCase("pink"))
          return(Color.pink);
      if (s.equalsIgnoreCase("red"))
          return(Color.red);
      if (s.equalsIgnoreCase("white"))
          return(Color.white);
      if (s.equalsIgnoreCase("yellow"))
          return(Color.yellow);

      // nope, must be an RGB triplet
      st = new StringTokenizer(s, ",");
      r = Integer.parseInt(st.nextToken());
      g = Integer.parseInt(st.nextToken());
      b = Integer.parseInt(st.nextToken());
      return(new Color(r,g,b));

   }   // end of getColorParameter()

   public void setcursorimage(String s1, String s2, int x, int y) {
      Image img;
      URL url=null;
      Point hotspot = new Point(x, y);
      Toolkit tk = getToolkit();

      try {
          url = new URL(s2);
      } catch(MalformedURLException e) {
          window.eval("seterror_handler('Applet error. Malformed cursor image URL.');");
          this.stop();
      }

      img = getImage(url);

      try {
          this.setCursor(tk.createCustomCursor(img, hotspot, s1));
      } catch(IndexOutOfBoundsException e) {
          window.eval("seterror_handler('Applet error. Cursor hotspot out of bounds.');");
          this.stop();
      }
      return;
   }   // end setcursorimage()

   public void setcursor(String name) {
      if (name.equalsIgnoreCase("hand"))
         this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      else if (name.equalsIgnoreCase("crosshair"))
         this.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.CROSSHAIR_CURSOR));
      else
         this.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
   }

   public String version () {
      return("jBox "+jBox_version+" Java VM "+System.getProperty("java.version")+" from "+System.getProperty("java.vendor"));
   }

   public void boxon () {
      box = true;
      line = drag = false;
      x1 = y1 = x2 = y2 = 0; // RWG - added this & following line to clean up lines if going from lineon -> boxon
      repaint();
      return;
   }

   public void boxoff () {
      int c = cursorsize;

      box = line = drag = false;
      x2 = x1; // collapse
      y2 = y1;
      cursorsize = 0;   // RWG kluge to elminate drawing a cross on cleanup
      repaint();
      cursorsize = c;
      window.eval("reset_handler('" + name + "'," + Math.min(x1,x2) + "," + Math.min(y1,y2) + "," + Math.max(x1,x2) + "," + Math.max(y1,y2) + ");");
      return;
   }

   public void lineon () {
      box = drag = false;
      line = true;
      pl = new Polygon();
      tot = seg = 0;
      return;
   }

   public void lineoff () {   // synonym for boxoff()
      boxoff();
   }

   public void dragon () {
		x2 = x1 = y2 = y1 = -1;
		boxoff();
      // box = line = false;
      drag=true;
      return;
   }

   public void dragoff () {   // synonym for boxoff()
      boxoff();
   }

   public void setimage(String s) {   // was swap() in mapplet
   	ix=iy=0;	// Adam Ryan's fix for panning problem (aryan@co.linn.or.us)
//      URL url=null;
      MediaTracker tracker = new MediaTracker(this);

      busy = true;

      // reinitialize the cursor position
      x1 = x2 = (screenSize.width-1)/2.0;
      y1 = y2 = (screenSize.height-1)/2.0;

      if(busyimg != null)
          repaint(); // show busy image

      // img.flush();

		img = get_image(s);
		tracker.addImage(img, 0);

      try {
         tracker.waitForID(0); // wait till it loads
      } catch (InterruptedException e) {
         return;
      }

      busy = false;
      repaint();
      window.eval("reset_handler('" + name + "'," + Math.min(x1,x2) + "," + Math.min(y1,y2) + "," + Math.max(x1,x2) + "," + Math.max(y1,y2) + ");");

      return;
   }   // end setimage()

   //
   // Mouse event handlers
   //
   public void mouseClicked(MouseEvent event) {
      // look for double click; reset line
      if (event.getClickCount() >= 2)   {
         if (line) {
            pl = new Polygon();
            // forcing x1 and x2 to be different prevents this double click
            // from starting a new pline / measure sequence by causing it
            // to fail the mouse click test in paint()
            x1=0;
            x2=1;
         }
      }
   }

   public void mouseEntered(MouseEvent event) {
      if(verbose)   window.eval("mouseenter_handler('" + this.name + "');");
   }

   public void mouseExited(MouseEvent event) {
      if(verbose) window.eval("mouseexit_handler('" + this.name + "');");
   }

   public void mouseMoved(MouseEvent event) {
      if(verbose)
         window.eval("mousemove_handler('" + this.name + "'," + event.getX() + "," + event.getY() + ");");
      if(line) {
         x2 = event.getX();
         y2 = event.getY();
         repaint();
      }
   }

   public void mousePressed(MouseEvent event) {
      x1 = x2 = event.getX();
      y1 = y2 = event.getY();
      if (line) repaint();
   }

   public void mouseDragged(MouseEvent event) {
      if(verbose)
         window.eval("mousemove_handler('" + this.name + "'," + event.getX() + "," + event.getY() + ");");

      x2 = event.getX();
      y2 = event.getY();
      if(!box && !line && !drag) {
         x1 = x2;
         y1 = y2;
      }
      if (drag) {
         ix = (int) (x2-x1);
         iy = (int) (y2-y1);
      }
      repaint();
   }

   public void mouseReleased(MouseEvent event) {
      x2 = event.getX();
      y2 = event.getY();
      if(box || line) {
         if ( x2 > screenSize.width) { x2 = screenSize.width-1; }
         if ( x2 < 0 ) { x2 = 0; }
         if ( y2 > screenSize.height) { y2 = screenSize.height-1; }
         if ( y2 < 0 ) { y2 = 0; }

         // check to see if (x2,y2) forms a large enough rectangle
         // to be considered a new extent or if the user is just a
         // poor mouse clicker
         if((Math.abs(x1-x2) <= jitter) || (Math.abs(y1-y2) <= jitter)) {
            x2 = x1;
            y2 = y1;
         }
         if (line) {
            area=calcArea();
         }
      } else if (drag) {
         x1 = x2 = ((screenSize.width-1)/2.0)-(x2-x1);
         y1 = y2 = ((screenSize.height-1)/2.0)-(y2-y1);
      } else {
         x2 = x1;
         y2 = y1;
      }

      repaint();

      // this a time for a re-draw if the application so chooses
      if(!busy && !line) {   // don't want a form-submit in line drawing mode
        new evalThread(window, name, x1, y1, x2, y2).start();
        // window.eval("setbox_handler('" + name + "'," + Math.min(x1,x2) + "," + Math.min(y1,y2) + "," + Math.max(x1,x2) + "," + Math.max(y1,y2) + ");");
        // String[] args = {name, Double.toString(Math.min(x1,x2)), Double.toString(Math.min(y1,y2)), Double.toString(Math.max(x1,x2)), Double.toString(Math.max(y1,y2))};
   		// window.call("setbox_handler", args);
      }
   }   // mouseReleased

   public double calcArea() {
      double a=0;
      int i, n=pl.npoints-1;   // n=number_of_point adjusted to a zero based array

      if (n >=2) {   // there are 3 or more points
         a=pl.xpoints[0] * (pl.ypoints[1] - pl.ypoints[n]);   // calc first point
         for (i=1; i<n; i++) {
            a+=pl.xpoints[i] * (pl.ypoints[i+1] - pl.ypoints[i-1]);
         }
         a+=pl.xpoints[n] * (pl.ypoints[0] - pl.ypoints[n-1]);   // calc last point
      }
      return(Math.abs(a/2));
   }

   public void paint(Graphics g) {
      int x, y, w, h, i;
      // draw the image
      if (drag) {   // erase the map area
         offScreenGraphics.setColor (Color.white);
         offScreenGraphics.fillRect (0, 0, screenSize.width,screenSize.height);
      }
      offScreenGraphics.drawImage(img,ix,iy,this);

      // draw the user defined pline, rectangle or crosshair
      offScreenGraphics.setColor(color);

      if (line) {
         // added "&& (x1!=0) && (y1!=0)" below based on Wim Blanken's comments re frames
         if ((x1==x2) && (y1==y2) && ((x1!=0) && (y1!=0))) {   // mouse click
            if (pl.npoints == 0) {      // don't question first point
               pl.addPoint((int)x2, (int)y2);
               tot=0;
            } else if (! ((x1 == pl.xpoints[pl.npoints-1]) && (y1 == pl.ypoints[pl.npoints-1]))) {
               pl.addPoint((int)x2, (int)y2);
               tot+=seg;
            }
         }
         if (pl.npoints > 0) {   // don't do anything w/o starting point
            offScreenGraphics.drawPolyline(pl.xpoints, pl.ypoints, pl.npoints);
            offScreenGraphics.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
            seg = Math.sqrt(Math.pow(y2 - pl.ypoints[pl.npoints-1],2) + Math.pow(x2 - pl.xpoints[pl.npoints-1],2));
            window.eval("measure_handler('" + name + "'," + seg + "," + tot +"," + pl.npoints + "," + area + ");");
         }
      } else if (box) {
         x = (int)Math.min(x1,x2);
         y = (int)Math.min(y1,y2);
         w = (int)Math.abs(x1-x2);
         h = (int)Math.abs(y1-y2);

         for(i=0; i<thickness; i++)
            offScreenGraphics.drawRect(x+i, y+i, w-(2*i), h-(2*i));
      } else if((x1==x2) && (y1==y2)) {
         if(cursorsize > 0) {
            offScreenGraphics.drawLine((int)(x2-cursorsize), (int)y2, (int)(x2+cursorsize), (int)y2);
            offScreenGraphics.drawLine((int)x2, (int)(y2-cursorsize), (int)x2, (int)(y2+cursorsize));
         }
      }
      if(busy && busyimg != null) {
         x = screenSize.width/2 - busyimg.getWidth(this)/2;
         y = screenSize.height/2 - busyimg.getHeight(this)/2;
         offScreenGraphics.drawImage(busyimg,x,y,this);
      }
      g.drawImage(offScreenImage, 0,0, this);
   }   // end of paint

   public void destroy () {}

   public void update (Graphics g) {
      paint(g);
   }

}   // end of class jBox
