import com.sixlegs.image.png.PngImage;
import java.util.Enumeration;
import java.io.IOException;

public class ErrorExample
{
    public static void main(String[] args)
    {
        try {
            // Makes errors in ancillary chunks fatal
            PngImage.setAllErrorsFatal(true);

            // Read PNG image from file
            PngImage png = new PngImage(args[0]);

            // Read entire PNG image (doesn't throw exceptions)
            png.getEverything();

            // Print all errors
            if (png.hasErrors()) {
                System.err.println("Errors in PNG processing:");
                for (Enumeration e = png.getErrors(); e.hasMoreElements();) {
                    // Objects returned by getErrors derive from IOException,
                    // but you usually only want to print them
                    System.err.println("  " + e.nextElement());
                }
            }
        } catch (IOException e) { }
    }
}
