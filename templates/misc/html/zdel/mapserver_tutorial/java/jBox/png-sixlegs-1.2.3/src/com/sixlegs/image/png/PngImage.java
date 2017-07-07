/*
com.sixlegs.image.png - Java package to read and display PNG images
Copyright (C) 1998, 1999, 2001 Chris Nokleberg

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Library General Public
License as published by the Free Software Foundation; either
version 2 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Library General Public License for more details.

You should have received a copy of the GNU Library General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA  02111-1307, USA.
*/

package com.sixlegs.image.png;

import java.awt.Color;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * For more information visit <a href="http://www.sixlegs.com/">http://www.sixlegs.com/</a>
 * @see         java.awt.image.ImageProducer
 * @version     1.2.3 May 14, 2002
 * @author      Chris Nokleberg <a href="mailto:chris@sixlegs.com">&lt;chris@sixlegs.com&gt;</a>
 */

public final class PngImage
implements ImageProducer
{
    /* package */ static boolean allFatal = false;
    /* package */ static final int BUFFER_SIZE = 8192;
    private static boolean progressive = true;
    private static Hashtable prototypes = new Hashtable();

    /* package */ static final String ASCII_ENCODING = "US-ASCII";
    /* package */ static final String LATIN1_ENCODING = "8859_1";
    /* package */ static final String UTF8_ENCODING = "UTF8";
    /* package */ static final long DEFAULT_GAMMA = 45455;
    private static double DISPLAY_EXPONENT = 2.2;
    private static double USER_EXPONENT = 1.0;

    /* package */ Data data = new Data();
    private Vector errorList;

    final class Data {
        private final Vector consumers = new Vector();
        private final Hashtable chunks = new Hashtable();
        
        private int[] pixels;
        private boolean produceFailed;
        private boolean useFlush = false;

        /* package */ IDATInputStream in_idat;
        /* package */ Chunk_IHDR header;
        /* package */ Chunk_PLTE palette;
        /* package */ final int[] gammaTable = new int[256];
        /* package */ final Hashtable textChunks = new Hashtable();
        /* package */ final Hashtable properties = new Hashtable();
        /* package */ final Hashtable palettes = new Hashtable(1);
        /* package */ final Vector gifExtensions = new Vector();

        private Data() {}
    }

    /////////////////// start public ////////////////////////

    public static final int COLOR_TYPE_GRAY = 0;
    public static final int COLOR_TYPE_GRAY_ALPHA = 4;
    public static final int COLOR_TYPE_PALETTE = 3;
    public static final int COLOR_TYPE_RGB = 2;
    public static final int COLOR_TYPE_RGB_ALPHA = 6;
  
    public static final int INTERLACE_TYPE_NONE = 0;
    public static final int INTERLACE_TYPE_ADAM7 = 1;

    public static final int FILTER_TYPE_BASE = 0;
    public static final int FILTER_TYPE_INTRAPIXEL = 64;

    public static final int COMPRESSION_TYPE_BASE = 0;  

    public static final int UNIT_UNKNOWN = 0;
    public static final int UNIT_METER = 1;
    public static final int UNIT_PIXEL = 0;
    public static final int UNIT_MICROMETER = 1;
    public static final int UNIT_RADIAN = 2;

    public static final int SRGB_PERCEPTUAL = 0;
    public static final int SRGB_RELATIVE_COLORIMETRIC = 1;
    public static final int SRGB_SATURATION_PRESERVING = 2;
    public static final int SRGB_ABSOLUTE_COLORIMETRIC = 3;

    /** 
     * Constructs a PngImage object from a local PNG file.
     * @param filename full path to local PNG file
     */
    public PngImage(String filename)
    throws IOException
    {
        FileInputStream fs = new FileInputStream(filename);
        init(new BufferedInputStream(fs, BUFFER_SIZE));
    }

    /** 
     * Constructs a <code>PngImage</code> object from a URL.
     * @param filename URL of PNG image
     */
    public PngImage(URL url)
    throws IOException
    {
        InputStream is = url.openConnection().getInputStream();
        init(new BufferedInputStream(is, BUFFER_SIZE));
    }
  
    /** 
     * Constructs a <code>PngImage</code> object from an input stream.
     * Buffer the stream for better performance.
     * @param filename URL of PNG image
     * @see java.io.BufferedInputStream
     */
    public PngImage(InputStream is)
    {
        init(is);
    }

    /**
     * Adds an <code>ImageConsumer</code> to the list of consumers interested in
     * data for this image.
     * @see java.awt.image.ImageConsumer
     */
    public void addConsumer(ImageConsumer ic)
    {
        if (data == null) return;
        if (data.consumers.contains(ic)) return;
        data.consumers.addElement(ic);
    }

    /**
     * Determine if an <code>ImageConsumer</code> is on the list of consumers currently
     * interested in data for this image.
     * @return true if the consumer is on the list, false otherwise.
     * @see java.awt.image.ImageConsumer
     */
    public boolean isConsumer(ImageConsumer ic)
    {
        if (data == null) return false;
        return data.consumers.contains(ic);
    }

    /**
     * Remove an <code>ImageConsumer</code> from the list of consumers interested in
     * data for this image.
     * @see java.awt.image.ImageConsumer
     */
    public void removeConsumer(ImageConsumer ic)
    {
        if (data == null) return;
        data.consumers.removeElement(ic);
    }

    /**
     * Adds an <code>ImageConsumer</code> to the list of consumers interested in
     * data for this image, and immediately start delivery of the
     * image data through the consumer/producer interface.
     * @see java.awt.image.ImageConsumer
     */
    public void startProduction(ImageConsumer ic)
    {
        if (data == null) {
            throw new RuntimeException("Object has been flushed.");
        }
        addConsumer(ic);
        ImageConsumer[] ics = new ImageConsumer[data.consumers.size()];
        data.consumers.copyInto(ics);
        produceHelper(ics);
    }
  
    /**
     * Requests delivery of image data to the specified <code>ImageConsumer</code>
     * one more time in top-down, left-right order.
     * @see #startProduction
     * @see java.awt.image.ImageConsumer
     */
    public void requestTopDownLeftRightResend(ImageConsumer ic)
    {
        if (data == null || data.pixels == null) return;
        startProduction(ic);
    }

    /**
     * Sets the default desired final user exponent. Ideal setting 
     * depends on user viewing conditions. The default value is 1.0.
     * Set greater than 1.0 to darken the mid-level tones, or less than
     * 1.0 to lighten them.
     * <p>
     * This method sets the user exponent for new <code>PngImage</code> objects.
     * It is not possible to change the user exponent of an existing 
     * <code>PngImage</code>.
     * @param exponent desired user exponent
     */
    static public void setUserExponent(double exponent)
    {
        USER_EXPONENT = exponent;
    }

    /**
     * Sets the default display exponent. Depends on monitor and gamma lookup
     * table settings (if any). Default value is 2.2, which should 
     * work well with most PC displays. If the operating system has
     * a gamma lookup table (Macintosh) the display exponent should be lower.
     * <p>
     * This method sets the display exponent for new <code>PngImage</code> objects.
     * It is not possible to change the display exponent of an existing 
     * <code>PngImage</code>.
     * @param exponent desired display exponent
     */
    static public void setDisplayExponent(double exponent)
    {
        DISPLAY_EXPONENT = exponent;
    }

    /**
     * Checks if there were errors during image production. 
     * A good time to check this is when you implement the <code>ImageObserver</code>
     * interface and the <code>ERROR</code> flag is set.
     * @see java.awt.image.ImageObserver
     * @see #getErrors
     */
    public boolean hasErrors()
    {
        if (errorList == null) return false;
        return errorList.size() > 0;
    }

    public boolean hasFatalError()
    {
        return hasErrors() &&
            !(errorList.elementAt(errorList.size() - 1) instanceof PngExceptionSoft);
    }

    /**
     * Returns an <code>Enumeration</code> of all the errors that occurred during 
     * image production. This includes any non-fatal errors.
     * @see #hasErrors
     */
    public Enumeration getErrors()
    {
        return errorList.elements();
    }

    /**
     * Specifies whether all errors will abort the image production.
     * Normally, a value error in a non-critical chunk causes the
     * PNG loader to simply skip the offending chunk.
     */
    static public void setAllErrorsFatal(boolean allFatal)
    {
        PngImage.allFatal = allFatal;
    }

    /**
     * Interlaced images can either be displayed when completely
     * read (default) or progressively. When progressive display is
     * enabled, a <code>PngImage</code> will call the <code>imageComplete</code> 
     * method of its registered image consumers with a <code>SINGLEFRAMEDONE</code> 
     * status after each pass. This, in turn, will trigger an <code>imageUpdate</code>
     * with the <code>FRAMEBITS</code> flag set to watching <code>ImageObservers</code>.
     * <p>
     * <b>Note:</b> Images are only delivered progressively on the
     * first production of the image data. Subsequent requests for the
     * (cached) image data will send the image as a complete single
     * frame.
     * @see java.awt.image.ImageConsumer
     * @see java.awt.image.ImageObserver
     */
    static public void setProgressiveDisplay(boolean progressive)
    {
        PngImage.progressive = progressive;
    }

    /** 
     * Get a suggested background color (from the bKGD chunk).
     * @see #getProperty
     * @return the suggested Color, or null if no valid bKGD was found.
     */
    public Color getBackgroundColor()
    throws IOException
    {
        return (Color)getProperty("background");
    }

    /** 
     * Gets width of image in pixels. 
     * @see #getProperty
     */
    public int getWidth()
    throws IOException
    {
        readToData();
        return data.header.width; 
    }

    /** 
     * Gets height of image in pixels.
     * @see #getProperty
     */
    public int getHeight()
    throws IOException
    {
        readToData();
        return data.header.height;
    }

    /** 
     * Gets bit depth of image data.
     * @see #getProperty
     * @return 1, 2, 4, 8, or 16.
     */
    public int getBitDepth()
    throws IOException
    {
        readToData();
        return data.header.depth;
    }

    /** 
     * Gets the interlacing method used by this image.
     * @see #getProperty
     * @return one of the INTERLACE_TYPE_* constants.
     */
    public int getInterlaceType()
    throws IOException {
        readToData();
        return data.header.interlace; 
    }

    /**
     * Gets the alpha and color properties of an image.
     * An image can either be grayscale, grayscale with alpha channel,
     * RGB, RGB with alpha channel, or paletted.
     * @see #getProperty
     * @return COLOR_TYPE_GRAY<br>
     *         COLOR_TYPE_GRAY_ALPHA<br>
     *         COLOR_TYPE_PALETTE<br>
     *         COLOR_TYPE_RGB<br>
     *         COLOR_TYPE_RGB_ALPHA
     */
    public int getColorType()
    throws IOException
    {
        readToData();
        return data.header.colorType;
    }

    /** 
     * Returns true if the image has an alpha channel.
     * @see #getProperty
     * @see #getColorType
     */
    public boolean hasAlphaChannel()
    throws IOException
    {
        readToData();
        return data.header.alphaUsed; 
    }

    /** 
     * Returns true if the image is grayscale.
     * @see #getProperty
     * @see #getColorType
     */
    public boolean isGrayscale()
    throws IOException
    {
        readToData();
        return !data.header.colorUsed;
    }

    /** 
     * Returns true if the image is paletted.
     * @see #getProperty
     * @see #getColorType
     */
    public boolean isIndexedColor()
    throws IOException
    {
        readToData();
        return data.header.paletteUsed;
    }

    /**
     * Gets a property of this image by name. If a property is not 
     * defined for a particular image, this method returns <code>null</code>.
     * <p>
     * <b>Note:</b> This method will only read up to the beginning of
     * the image data unless the image data has already been read,
     * either through the consumer/producer interface or by calling
     * <code>getEverything</code>.
     * <p>
     * The following properties are guaranteed to be defined:
     * <p>
     * <center><table border=1 cellspacing=0 cellpadding=4 width="80%">
     * <tr bgcolor="#E0E0E0"><td nowrap><b>Name</b></td><td nowrap><b>Type</b></td>
     *     <td><b>Description</b></td></tr>
     * <tr><td nowrap>"width"</td><td nowrap><code>Integer</code></td>
     *     <td>Image width in pixels</td></tr>
     * <tr><td nowrap>"height"</td><td nowrap><code>Integer</code></td>
     *     <td>Image height in pixels</td></tr>
     * <tr><td nowrap>"interlace type"</td><td nowrap><code>Integer</code></td>
     *     <td>See <a href="#getInterlaceType">getInterlaceType</a></td></tr>
     * <tr><td nowrap>"compression type"</td><td nowrap><code>Integer</code></td>
     *     <td><code>COMPRESSION_TYPE_BASE</code></td></tr>
     * <tr><td nowrap>"filter type"</td><td nowrap><code>Integer</code></td>
     *     <td><code>FILTER_TYPE_BASE</code></td></tr>
     * <tr><td nowrap>"color type"</td><td nowrap><code>Integer</code></td>
     *     <td>See <a href="#getColorType">getColorType</a></td></tr>
     * <tr><td nowrap>"bit depth"</td><td nowrap><code>Integer</code></td>
     *     <td>1, 2, 4, 8, or 16 <sup><a href="#fn1">(1)</a></sup></td></tr>
     * <tr><td nowrap>"gamma"</td><td nowrap><code>Long</code></td>
     *     <td>File gamma * 100000 <sup><a href="#fn2">(2)</a></sup></td></tr>
     * <tr valign=top><td nowrap>"significant bits"</td><td nowrap><code>byte[]</code></td>
     *     <td>Significant bits per component: <br><nowrap><code>[r,g,b]</code></nowrap> or <nowrap><code>[r,g,b,alpha]</code></nowrap> <sup><a href="#fn3">(3)</a></sup></td></tr>
     * </table></center>
     * <center><table border=0 cellspacing=0 cellpadding=4 width="80%">
     * <tr><td><b><sup><a name="fn1">1</a></sup></b> 16-bit pixel components are reduced to 8 bits<br>
     *         <b><sup><a name="fn2">2</a></sup></b> Uses value from <code>sRGB</code> or <code>gAMA</code> chunks, 
     *         or default (<code>45455</code>)<br>
     *         <b><sup><a name="fn3">3</a></sup></b> For grayscale images, <code>r == g == b</code></td></tr>
     * </table></center>

     * <p>
     * The following properties are optional:<p>
     * <center><table border=1 cellspacing=0 cellpadding=4 width="80%">
     * <tr bgcolor="#E0E0E0"><td nowrap><b>Name</b></td><td nowrap><b>Type</b></td>
     *     <td><b>Description</b></td></tr>
     * <tr valign=top><td nowrap>"palette"</td><td nowrap><code>int[][]</td>
     *     <td>Palette or suggested palette (PLTE chunk):<br>
     *     <nowrap><code>[r,g,b][entry]</code></nowrap> or <nowrap><code>[r,g,b][entry]</code></nowrap></td></tr>
     * <tr><td nowrap>"palette size"</td><td nowrap><code>Integer</td>
     *     <td>Size of palette, 1 - 256</td></tr>
     * <tr valign=top><td nowrap>"histogram"</td><td nowrap><code>int[]</td>
     *     <td>Palette entry usage frequency</td></tr>
     * <tr><td nowrap>"background"</td><td nowrap><code>java.awt.Color</td>
     *     <td>Suggested background color</td></tr>
     * <tr><td nowrap>"background low bytes"</td><td nowrap><code>java.awt.Color</td>
     *     <td>The low (least significant) bytes of a 16-bit background color</td></tr>
     * <tr><td nowrap>"background index"</td><td nowrap><code>Integer</td>
     *     <td>The palette index of the suggested background color</td></tr>
     * <tr><td nowrap>"time"</td><td nowrap><code>java.util.Date</code></td>
     *     <td>Time of last image modification</td></tr>
     * <tr><td nowrap>"pixel dimensions x"</td><td nowrap><code>Long</code></td>
     *     <td>Pixels per unit, X axis</td></tr>
     * <tr><td nowrap>"pixel dimensions y"</td><td nowrap><code>Long</code></td>
     *     <td>Pixels per unit, Y axis</td></tr>
     * <tr valign=top><td nowrap>"pixel dimensions unit"</td><td nowrap><code>Integer</code></td>
     *     <td><code>UNIT_UNKNOWN</code> or <code>UNIT_METER</code></td></tr>
     * <tr valign=top><td nowrap>"image position x"</td><td nowrap><code>Integer</code></td>
     *     <td>Horizontal offset from left of page</td></tr>
     * <tr valign=top><td nowrap>"image position y"</td><td nowrap><code>Integer</code></td>
     *     <td>Vertical offset from top of page</td></tr>
     * <tr valign=top><td nowrap>"image position unit"</td><td nowrap><code>Integer</code></td>
     *     <td><code>UNIT_PIXEL</code> or <code>UNIT_MICROMETER</code></td></tr>
     * <tr valign=top><td nowrap>"pixel scale x"</td><td nowrap><code>Double</code></td>
     *     <td>Pixel width, physical scale of subject</td></tr>
     * <tr valign=top><td nowrap>"pixel scale y"</td><td nowrap><code>Double</code></td>
     *     <td>Pixel height, physical scale of subject</td></tr>
     * <tr valign=top><td nowrap>"pixel scale unit"</td><td nowrap><code>Integer</code></td>
     *     <td><code>UNIT_METER</code> or <code>UNIT_RADIAN</code></td></tr>
     * <tr valign=top><td nowrap>"chromaticity xy"</td><td nowrap><code>long[][]</code></td>
     *     <td>CIE x,y chromaticities * 100000: <nowrap><code>[white,r,g,b][x,y]</code></nowrap></td></tr>
     * <tr valign=top><td nowrap>"chromaticity xyz"</td><td nowrap><code>double[][]</code></td>
     *     <td>CIE XYZ chromaticities: <nowrap><code>[white,r,g,b][X,Y,Z]</code></nowrap></td></tr>
     * <tr valign=top><td nowrap nowrap>"srgb rendering intent"</td><td nowrap><code>Integer</code></td><td>
     *     <code> SRGB_PERCEPTUAL</code> or<br>
     *     <code> SRGB_RELATIVE_COLORIMETRIC</code> or<br>
     *     <code> SRGB_SATURATION_PRESERVING</code> or<br>
     *     <code> SRGB_ABSOLUTE_COLORIMETRIC</code></td></tr>
     * <tr><td nowrap>"icc profile name"</td><td nowrap><code>String</code></td>
     *     <td>Internal ICC profile name </td></tr>
     * <tr><td nowrap>"icc profile"</td><td nowrap><code>String</code></td>
     *     <td>Uncompressed ICC profile </td></tr>
     * <tr><td nowrap>"pixel calibration purpose"</td><td nowrap><code>String</code></td>
     * <td> Equation identifier</td></tr>
     * <tr><td nowrap>"pixel calibration x0"</td><td nowrap><code>Integer</code></td>
     * <td> Lower limit of original sample range</td></tr>
     * <tr><td nowrap>"pixel calibration x1"</td><td nowrap><code>Integer</code></td>
     * <td> Upper limit of original sample range</td></tr>
     * <tr valign=top><td nowrap>"pixel calibration type"</td><td nowrap><code>Integer</code></td>
     * <td> 
     *     <code>0</code>: Linear mapping<br>
     *     <code>1</code>: Base-e exponential mapping<br>
     *     <code>2</code>: Arbitrary-base exponential mapping<br>
     *     <code>3</code>: Hyperbolic mapping
     * </td></tr>
     * <tr><td nowrap>"pixel calibration n"</td><td nowrap><code>Integer</code></td>
     * <td> Number of parameters</td></tr>
     * <tr><td nowrap>"pixel calibration unit"</td><td nowrap><code>String</code></td>
     * <td> Symbol or description of unit</td></tr>
     * <tr><td nowrap>"pixel calibration parameters"</td><td nowrap><code>double[]</code></td>
     * <td> &nbsp;</td></tr>
     * <tr><td nowrap>"gif disposal method"</td><td nowrap><code>Integer</code></td>
     * <td>See GIF89a Graphic Control Extension specification</td></tr>
     * <tr><td nowrap>"gif user input flag"</td><td nowrap><code>Integer</code></td>
     * <td>See GIF89a Graphic Control Extension specification</td></tr>
     * <tr><td nowrap>"gif delay time"</td><td nowrap><code>Integer</code></td>
     * <td>See GIF89a Graphic Control Extension specification</td></tr>

     * <tr><td nowrap>"transparency"</td><td nowrap><code>java.awt.Color</td>
     *     <td>Transparent color <sup><a href="#fn4">(4)</a></sup></td></tr>
     * <tr><td nowrap>"transparency low bytes"</td><td nowrap><code>java.awt.Color</td>
     *     <td>The low (least significant) bytes of a 16-bit transparency color <sup><a href="#fn4">(4)</a></sup></td></tr>
     * <tr><td nowrap>"transparency size"</td><td nowrap><code>Integer</td>
     *     <td>The number of palette entries with transparency information <sup><a href="#fn5">(5)</a></sup></td></tr>
     
     * </table></center>
     * <center><table border=0 cellspacing=0 cellpadding=4 width="80%">
     * <tr><td><b><sup><a name="fn4">4</a></sup></b> Grayscale or truecolor images only<br>
     *         <b><sup><a name="fn5">5</a></sup></b> Indexed-color images only</td></tr>
     * </table></center>     

     * <p>
     * In addition, certain common (but still optional) text chunks 
     * are available through the <code>getProperty</code> interface:<p>
     * <center><table border=1 cellspacing=0 cellpadding=4 width="80%">
     * <tr bgcolor="#E0E0E0"><td nowrap><b>Name</b></td><td nowrap><b>Type</b></td>
     *     <td><b>Description</b></td></tr>
     * <tr><td nowrap>"title"</td><td nowrap><code>TextChunk</code></td>
     *     <td>Short (one line) title or caption for image</td></tr>
     * <tr><td nowrap>"author"</td><td nowrap><code>TextChunk</code></td>
     *     <td>Name of image's creator</td></tr>
     * <tr><td nowrap>"description"</td><td nowrap><code>TextChunk</code></td>
     *     <td>Description of image (possibly long)</td></tr>
     * <tr><td nowrap>"copyright"</td><td nowrap><code>TextChunk</code></td>
     *     <td>Copyright notice</td></tr>
     * <tr><td nowrap>"creation time"</td><td nowrap><code>TextChunk</code></td>
     *     <td>Time of original image creation</td></tr>
     * <tr><td nowrap>"software"</td><td nowrap><code>TextChunk</code></td>
     *     <td>Software used to create the image</td></tr>
     * <tr><td nowrap>"disclaimer"</td><td nowrap><code>TextChunk</code></td>
     *     <td>Legal disclaimer</td></tr>
     * <tr><td nowrap>"warning"</td><td nowrap><code>TextChunk</code></td>
     *     <td>Warning of nature of content</td></tr>
     * <tr><td nowrap>"source"</td><td nowrap><code>TextChunk</code></td>
     *     <td>Device used to create the image</td></tr>
     * <tr><td nowrap>"comment"</td><td nowrap><code>TextChunk</code></td>
     *     <td>Miscellaneous comment</td></tr>
     * </table></center>

     * @see #getEverything
     * @see #getWidth
     * @see #getHeight
     * @see #getInterlaceType
     * @see #getColorType
     * @see #getTextChunk
     * @see #getBackgroundColor
     * @param name a property name
     * @return the value of the named property. 
     */
    public Object getProperty(String name)
    throws IOException
    {
        readToData();
        return data.properties.get(name);
    }

    /**
     * Returns an <code>Enumeration</code> of the available properties.
     * @see #getProperty
     */
    public Enumeration getProperties()
    throws IOException
    {
        readToData();
        return data.properties.keys();
    }

    /**
     * Ensures that the entire PNG file has been read. No exceptions
     * are throws; errors are available by calling <code>getErrors</code>.
     * <p>
     * <b>Note:</b> The consumer/producer interface automatically
     * reads the entire PNG file. It usually is not necessary to call
     * <code>getEverything</code> unless you do not need the actual
     * image data.
     * @see #getErrors
     */
    public void getEverything()
    {
        startProduction(new DummyImageConsumer());
    }

    public boolean hasChunk(String type)
    {
        return data.chunks.get(new Integer(Chunk.stringToType(type))) != null;
    }

    /**
     * Register a <code>ChunkHandler</code> to handle a user defined
     * chunk type.
     * <p>
     * The chunk type must be four characters, ancillary (lowercase first letter),
     * and may not already be registered. You may register one of the supported 
     * ancillary chunk types (except <code>tRNS</code>) to override the standard behavior.
     * @see ChunkHandler
     * @param handler object to send chunk data to
     * @param type chunk type
     */
    public static void registerChunk(ChunkHandler handler, String type)
    throws PngException
    {
        if (type.length() < 4) {
            throw new PngException("Invalid chunk type length.");
        }

        int type_int = Chunk.stringToType(type);

        if (prototypes.containsKey(new Integer(type_int))) {
            throw new PngException("Chunk type already registered.");
        }
        if ((type_int & 0x20000000) == 0) {
            throw new PngException("Chunk must be ancillary.");
        }

        registerChunk(new UserChunk(handler, type_int));
    }

    /**
     * Returns an <code>Enumeration</code> of the available suggested palette names.
     * @see #getSuggestedPalette
     */
    public Enumeration getSuggestedPalettes()
    throws IOException
    {
        readToData();
        return data.palettes.keys();
    }

    /**
     * Returns the suggested palette (sPLT chunk) specified by the 
     * palette name.
     * @see #getSuggestedPalette
     * @param name the name of the suggested palette
     * @return <nowrap><code>[r,g,b,alpha,freq][entry]</code></nowrap>, or null if not present.
     */
    public int[][] getSuggestedPalette(String name)
    throws IOException
    {
        readToData();
        return (int[][])data.palettes.get(name);
    }

    /**
     * Returns the specified text chunk.
     * <p>
     * <b>Note:</b> Text chunks may appear anywhere in the file. This
     * method will only read up to the beginning of the image data
     * unless the image data has already been read, either through the
     * consumer/producer interface or by calling
     * <code>getEverything</code>.
     * @see #getTextChunks
     * @see #getEverything
     * @see #getProperty
     * @param key the key of the desired chunk
     * @return the text chunk, or null if not present.
     */
    public TextChunk getTextChunk(String key)
    throws IOException
    {
        readToData();
        return (TextChunk)data.textChunks.get(key);
    }

    /**
     * Returns the keys of all known text chunks.
     * <p>
     * <b>Note:</b> Text chunks may appear anywhere in the file. This
     * method will only read up to the beginning of the image data
     * unless the image data has already been read, either through the
     * consumer/producer interface or by calling
     * <code>getEverything</code>.
     * @see #getTextChunk
     * @see #getEverything
     * @return an <code>Enumeration</code> of the keys of text chunks read so far.
     */
    public Enumeration getTextChunks()
    throws IOException
    {
        readToData();
        return data.textChunks.elements();
    }

    /**
     * Returns all known GIF Application Extensions.
     * <p>
     * <b>Note:</b> GIF Application Extensions may appear anywhere in
     * the file. This method will only read up to the beginning of the
     * image data unless the image data has already been read, either
     * through the consumer/producer interface or by calling
     * <code>getEverything</code>.
     * @see #getEverything
     * @return an <code>Enumeration</code> of all GifExtension objects read so far.
     * @see GifExtension
     */
    public Enumeration getGifExtensions()
    throws IOException
    {
        readToData();
        return data.gifExtensions.elements();
    }

    /**
     * Readies this PngImage to be flushed after the next image
     * production, to free memory (default false).
     * <p>
     * After flushing, you may only call the <code>getErrors</code>
     * and <code>hasErrors</code> methods on this object. The pixel
     * data will no longer be available through the consumer/producer
     * interface.
     * <p>
     * <b>Note:</b> Using a PixelGrabber object on an Image produced
     * by this PngImage object will ask for a second production of the
     * pixel data, which will fail if the object has been flushed.
     * @see #getErrors
     * @see #hasErrors
     */
    public void setFlushAfterNextProduction(boolean useFlush)
    {
        data.useFlush = useFlush;
    }

    private void flush()
    {
        if (data != null) {
            try {
                data.in_idat.close();
            } catch (IOException e) {
                // TODO: ignore?
            }
            data = null;
        }
    }

    /////////////////// end public ////////////////////////

    static {
        registerChunk(new Chunk_IHDR());
        registerChunk(new Chunk_PLTE());
        registerChunk(new Chunk_IDAT());
        registerChunk(new Chunk_IEND());
        registerChunk(new Chunk_tRNS());
    }

    private void init(InputStream in_raw)
    {
        data.properties.put("gamma", new Long(DEFAULT_GAMMA));
        data.in_idat = new IDATInputStream(this, in_raw);
    }

    private synchronized void readToData()
    throws IOException
    {
        try {
            if (data == null) {
                throw new EOFException("Object has been flushed.");
            }
            data.in_idat.readToData();
        } catch (PngException e) {
            addError(e);
            throw e;
        }
    }

    private static void registerChunk(Chunk proto)
    {
        prototypes.put(new Integer(proto.type), proto);
    }

    /* package */ static Chunk getRegisteredChunk(int type)
    {
        Integer type_obj = new Integer(type);
        if (prototypes.containsKey(type_obj)) {
            return ((Chunk)prototypes.get(type_obj)).copy();
        } else {
            try {
                String clsName =
                    "com.sixlegs.image.png.Chunk_" + Chunk.typeToString(type);
                registerChunk((Chunk)Class.forName(clsName).newInstance());
                return getRegisteredChunk(type);
            } catch (Exception e) {
                return new Chunk(type);
            }
        }
    }

    /* package */ Chunk getChunk(int type)
    {
        return (Chunk)data.chunks.get(new Integer(type));
    }

    /* package */ void putChunk(int type, Chunk c)
    {
        data.chunks.put(new Integer(type), c);
    }

    /* package */ void addError(Exception e)
    {
        if (errorList == null) {
            errorList = new Vector();
        }
        errorList.addElement(e);
    }

    /* package */ void fillGammaTable()
    {
        try {
            long file_gamma = ((Long)getProperty("gamma")).longValue();
            int max = (data.header.paletteUsed ? 0xFF : (1 << data.header.outputDepth) - 1);
            double decoding_exponent =
                (USER_EXPONENT * 100000d / (file_gamma * DISPLAY_EXPONENT));
            for (int i = 0; i <= max; i++) {
                int v = (int)(Math.pow((double)i / max, decoding_exponent) * 0xFF);
                if (!data.header.colorUsed) {
                    data.gammaTable[i] = v | v << 8 | v << 16;
                } else {
                    data.gammaTable[i] = v;
                }
            }
            if (data.palette != null) data.palette.calculate();
        } catch (IOException e) { }
    }

    private synchronized void produceHelper(ImageConsumer[] ics)
    {
        try {
            readToData();
            for (int i = 0; i < ics.length; i++) {
                ics[i].setDimensions(data.header.width, data.header.height);
                ics[i].setProperties(data.properties);
                ics[i].setColorModel(data.header.model);
                if (data.produceFailed) ics[i].imageComplete(ImageConsumer.IMAGEERROR);
            }
            if (data.produceFailed) return;
            if (data.pixels == null) {
                firstProduction(ics);
            } else {
                setHints(ics);
                for (int i = 0; i < ics.length; i++) {
                    ics[i].setPixels(0, 
                                     0, 
                                     data.header.width, 
                                     data.header.height, 
                                     data.header.model, 
                                     data.pixels, 
                                     0,
                                     data.header.width);
                    ics[i].imageComplete(ImageConsumer.STATICIMAGEDONE);
                }
            }
        } catch (IOException e) {
            data.produceFailed = true;
            addError(e);
            for (int i = 0; i < ics.length; i++) {
                ics[i].imageComplete(ImageConsumer.IMAGEERROR);
            }
        }
        if (data.useFlush) flush();
    }

    private void firstProduction(ImageConsumer[] ics)
    throws IOException
    {
        UnfilterInputStream in_filter
            = new UnfilterInputStream(this, data.in_idat);
        InputStream is =
            new BufferedInputStream(in_filter, BUFFER_SIZE);
        PixelInputStream pis =
            new PixelInputStream(this, is);

        setHints(ics);

        if (data.header.interlace == INTERLACE_TYPE_NONE) {
            // this is just for optimization
            // omitting it will cause NullInterlacer to be used
            produceNonInterlaced(ics, pis);
        }  else {
            produceInterlaced(ics, pis);
        }

        for (int i = 0; i < ics.length; i++) {
            ics[i].imageComplete(ImageConsumer.STATICIMAGEDONE);
        }
    }

    private void setHints(ImageConsumer[] ics)
    {
        for (int i = 0; i < ics.length; i++) {
            if (progressive &&
                data.pixels == null &&
                (data.header.interlace != INTERLACE_TYPE_NONE)) {
                ics[i].setHints(ImageConsumer.RANDOMPIXELORDER);
            } else {
                ics[i].setHints(ImageConsumer.TOPDOWNLEFTRIGHT | 
                                ImageConsumer.SINGLEPASS |
                                ImageConsumer.SINGLEFRAME |
                                ImageConsumer.COMPLETESCANLINES);
            }
        }
    }

    private void produceNonInterlaced(ImageConsumer[] ics, PixelInputStream pis)
    throws IOException
    {
        int w = data.header.width, h = data.header.height;

        // if we're going to flush, don't bother saving pixel data
        if (!data.useFlush) {
            data.pixels = new int[w * h];
        }

        int[] rowbuf = new int[w + 8];
        int pixelsWidth = w;
        int extra = w % pis.fillSize;
        if (extra > 0) pixelsWidth += (pis.fillSize - extra);
        int off = 0;
        for (int y = 0; y < h; y++) {
            pis.read(rowbuf, 0, pixelsWidth);
            for (int i = 0; i < ics.length; i++) {
                ics[i].setPixels(0, y, w, 1, data.header.model, rowbuf, 0, pixelsWidth);
            }
            if (!data.useFlush) {
                System.arraycopy(rowbuf, 0, data.pixels, w * y, w);
            }
        }
    }

    private void produceInterlaced(ImageConsumer[] ics, PixelInputStream pis)
    throws IOException
    {            
        int w = data.header.width, h = data.header.height;
        data.pixels = new int[w * h];
        int[] rowbuf = new int[w + 8];

        int numPasses = data.header.interlacer.numPasses();
        Interlacer lace = data.header.interlacer;

        for (int pass = 0; pass < numPasses; pass++) {
            int passWidth = lace.getPassWidth(pass);
            int extra = passWidth % pis.fillSize;
            if (extra > 0) passWidth += (pis.fillSize - extra);

            int blockWidth   = (progressive ? lace.getBlockWidth(pass) : 1);
            int blockHeight  = (progressive ? lace.getBlockHeight(pass) : 1);
            int rowIncrement = lace.getSpacingY(pass);
            int colIncrement = lace.getSpacingX(pass);
            int offIncrement = rowIncrement * w;
            int colStart     = lace.getOffsetX(pass);
            int row          = lace.getOffsetY(pass);
            int off          = row * w;

            while (row < h) {
                pis.read(rowbuf, 0, passWidth);
                int col = colStart;
                int x = 0;
                while (col < w) {
                    int bw = Math.min(blockWidth,  w - col);
                    int bh = Math.min(blockHeight, h - row);
                    int poff = off + col;
                    int pix = rowbuf[x++];
                    while (bh-- > 0) {
                        int poffend = poff + bw;
                        while (poff < poffend) {
                            data.pixels[poff++] = pix;
                        }
                        poff += w - bw;
                    }
                    col += colIncrement;
                }
                off += offIncrement;
                row += rowIncrement;
            }
            if (progressive) {
                for (int i = 0; i < ics.length; i++) {
                    ics[i].setPixels(0, 0, w, h, data.header.model, data.pixels, 0, w);
                    ics[i].imageComplete(ImageConsumer.SINGLEFRAMEDONE);
                }
            }
        }
        if (!progressive) {
            for (int i = 0; i < ics.length; i++) {
                ics[i].setPixels(0, 0, w, h, data.header.model, data.pixels, 0, w);
            }
        }
    }
}
