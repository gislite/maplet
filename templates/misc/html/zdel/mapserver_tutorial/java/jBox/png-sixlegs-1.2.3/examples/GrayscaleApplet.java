import com.sixlegs.image.png.PngImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.io.IOException;

public class GrayscaleApplet
extends PngApplet
{
    public GrayscaleApplet()
    {
    }

    protected ImageProducer filter(PngImage png, ImageProducer in)
    throws IOException
    {
        // Get chromaticity property if available
        double[][] chrom = (double[][])png.getProperty("chromaticity xyz");

        // Use luminance info from chromaticity array, or default
        ImageFilter gray = (chrom == null ?
                            new GrayscaleFilter() : 
                            new GrayscaleFilter(chrom[1][1], 
                                                chrom[2][1], 
                                                chrom[3][1]));

        // Chain PngImage to be filtered through GrayscaleFilter
        return new FilteredImageSource(super.filter(png, in), gray);
    }
}
