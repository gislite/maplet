import com.sixlegs.image.png.PngImage;
import java.util.Enumeration;
import java.io.IOException;

public class PropertiesExample
{
    public static void main(String[] args)
    throws IOException
    {
        // Read PNG image from file
        PngImage png = new PngImage(args[0]);

        // Ensures that entire PNG image has been read
        png.getEverything();

        // Print all available properties
        for (Enumeration e = png.getProperties(); e.hasMoreElements();) {
            String key = (String)e.nextElement();
            System.out.println(key);
            System.out.println("\t" + png.getProperty(key));
        }
    }
}

