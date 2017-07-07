import java.awt.Color;
import java.awt.image.ColorModel;
import java.awt.image.ImageFilter;

// This class composites an image with an alpha channel
// onto a background color.

final class RemoveAlphaFilter
extends ImageFilter
{
    private final int r_bg;
    private final int g_bg;
    private final int b_bg;
    private final int pixel_bg;
    private final static ColorModel directcm = ColorModel.getRGBdefault();

    public RemoveAlphaFilter(Color c)
    {
        // this(c.getRed(), c.getGreen(), c.getBlue()); // some compiler problem
        r_bg = c.getRed();
        g_bg = c.getGreen();
        b_bg = c.getBlue();
        pixel_bg = 0xFF000000 | (r_bg << 16) | (g_bg << 8) | b_bg;
    }

    public RemoveAlphaFilter(int r, int g, int b)
    {
        r_bg = r;
        g_bg = g;
        b_bg = b;
        pixel_bg = 0xFF000000 | (r_bg << 16) | (g_bg << 8) | b_bg;
    }

    public void setPixels(int x, int y, int w, int h,
                          ColorModel model,
                          int[] pixels,
                          int off,
                          int scansize)
    {
        int[] buf = new int[w * h];
        for (int yc = 0, i = off, bp = 0; yc < h; yc++) {
            for (int xc = 0; xc < w; xc++) {
                int pixel = pixels[i++];
                int alpha = model.getAlpha(pixel);
                int red   = model.getRed(pixel);
                int green = model.getGreen(pixel);
                int blue  = model.getBlue(pixel);
                switch (alpha) {
                case 0:
                    buf[bp++] = pixel_bg; 
                    break;
                case 255:
                    buf[bp++] = 0xFF000000 | (red << 16) | (green << 8) | blue;
                    break;
                default:
                    red   = (alpha * red   + (255 - alpha) * r_bg) / 255;
                    green = (alpha * green + (255 - alpha) * g_bg) / 255;
                    blue  = (alpha * blue  + (255 - alpha) * b_bg) / 255;
                    buf[bp++] = 0xFF000000 | (red << 16) | (green << 8) | blue;
                }
            }
            i += (scansize - w);
        }
        super.setPixels(x, y, w, h, directcm, buf, 0, w);
    }
}
