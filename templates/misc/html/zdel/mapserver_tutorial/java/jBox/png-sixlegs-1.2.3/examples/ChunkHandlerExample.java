import com.sixlegs.image.png.PngImage;
import com.sixlegs.image.png.ChunkHandler;
import java.io.IOException;

public class ChunkHandlerExample
implements ChunkHandler
{

    public static void main(String[] args)
    throws IOException
    {
        // Register instance of this class as "heLo" handler
        PngImage.registerChunk(new ChunkHandlerExample(), "heLo");

        // Read PNG image from file
        PngImage png = new PngImage(args[0]);

        // Ensures that entire PNG image has been read,
        // triggering handleChunk when a "heLo" chunk is found
        png.getEverything();
    }

    public void handleChunk(String type, byte[] data)
    {
        if (type.equals("heLo")) {
            System.err.println("Found heLo chunk, " + data.length + " bytes.");
        }
    }
}
