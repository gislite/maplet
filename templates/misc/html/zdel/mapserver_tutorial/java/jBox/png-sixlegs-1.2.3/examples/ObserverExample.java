import com.sixlegs.image.png.PngImage;
import java.io.IOException;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.image.ImageObserver;

public class ObserverExample
implements ImageObserver
{
    private static int counter = 0;
    
    public static void main(String[] args)
    throws IOException
    {
        // Update ImageObservers after each interlace pass
        PngImage.setProgressiveDisplay(true);

            // Read PNG image from file
        PngImage png = new PngImage(args[0]);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image img = tk.createImage(png);
        int w = png.getWidth();
        int h = png.getHeight();

        // Call prepareImage using an instance of this class
        // as the ImageObserver; triggers imageUpdate (below)
        tk.prepareImage(img, w, h, new ObserverExample());
    }

    public boolean imageUpdate(Image img, int infoflags,
                               int x, int y, int w, int h)
    {
        if ((infoflags & ALLBITS) != 0 || 
            (infoflags & FRAMEBITS) != 0) {
            // We have a frame, do something with image here
            System.out.println("Received frame " + ++counter);
        }
        return true;
    }
}
