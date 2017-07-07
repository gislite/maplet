import java.awt.image.RGBImageFilter;

final class GrayscaleFilter
extends RGBImageFilter
{
    private double Yr, Yg, Yb;

    public GrayscaleFilter()
    {
        this(0.2126d, 0.7152d, 0.0722d);
    }

    public GrayscaleFilter(double Yr, double Yg, double Yb)
    {
        this.Yr = Yr;
        this.Yg = Yg;
        this.Yb = Yb;
        canFilterIndexColorModel = true;
    }

    public int filterRGB(int x, int y, int rgb)
    {
        int a = (rgb & 0xFF000000);
        int r = (rgb & 0x00FF0000) >>> 16;
        int g = (rgb & 0x0000FF00) >>> 8;
        int b = (rgb & 0x000000FF);
        int Y = (int)(Yr * r + Yg * g + Yb * b);
        if (Y > 255) Y = 255;
        return a | (Y << 16) | (Y << 8) | Y;
    }
}
