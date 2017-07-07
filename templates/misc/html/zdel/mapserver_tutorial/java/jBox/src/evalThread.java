import netscape.javascript.*;

class evalThread extends Thread {
	JSObject twindow;
	double tx1, tx2, ty1, ty2;
	String tname;

	public evalThread(JSObject window, String name, double x1, double y1, double x2, double y2) {
		twindow = window;
		tname = name;
		tx1 = x1;
		ty1 = y1;
		tx2 = x2;
		ty2 = y2;
	}

	public void run () {
		twindow.eval("setbox_handler('" + tname + "'," + Math.min(tx1,tx2) + "," + Math.min(ty1,ty2) + "," + Math.max(tx1,tx2) + "," + Math.max(ty1,ty2) + ");");
	}
}	// end of class evalThread