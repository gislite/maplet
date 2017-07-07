import com.sixlegs.image.png.PngImage;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageProducer;
import java.awt.image.FilteredImageSource;
import java.io.IOException;
import java.net.URL;

public class PngApplet
extends Applet
{
    private Color imgbg;
    private Image img;
    private PngImage png;

    public PngApplet()
    {
    }

    public void init()
    {
        String loc = getParameter("URL");
        try {
            if (loc != null) {
                URL url = new URL(getCodeBase(), loc);
                png = new PngImage(url);
                String bgparam = getParameter("bgcolor");
                if (bgparam != null)
                    imgbg = new Color(Integer.valueOf(bgparam, 16).intValue());
                if (imgbg == null)
                    imgbg = png.getBackgroundColor();
                if (imgbg == null)
                    imgbg = Color.white;
                img = Toolkit.getDefaultToolkit().createImage(filter(png, png));
            }
        } catch (IOException e) {
            System.err.println("error reading " + loc + ": " + e);
        }
    }

    protected ImageProducer filter(PngImage png, ImageProducer in)
    throws IOException
    {
        return new FilteredImageSource(in, new RemoveAlphaFilter(imgbg));
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    public void paint(Graphics g)
    {
        try {
            if (img != null)
                g.drawImage(img, 0, 0, png.getWidth(), png.getHeight(), imgbg, null);
        } catch (Exception e) { }
    }
}
