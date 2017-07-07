Copyright (C) 1998, 1999, 2001 Chris Nokleberg
All rights reserved.

README - com.sixlegs.image.png v1.2.3

Features

  Supports all valid bit depths (grayscale/color), interlacing, paletted
  images, alpha channel/transparency, gamma correction, access to most
  standard chunks, private chunk handling, and progressive display.

Revision History

  Changes from v1.2.2 -> v1.2.3 (2002-5-14):

  * Bug fix: make read() methods obey InputStream contract. [Joseph Bowbeer]

  Changes from v1.2.0 -> v1.2.2 (2001-9-14):

  * Bug fix: wide images with alpha channels triggered
    ArrayIndexOutOfBoundsException. [Thomas Krammer]

  Changes from v1.1 -> v1.2.0 (2001-9-4):

  * Bug fix: now supports smooth scaling via AreaAveragingScaleFilter
    class or Image.getScaledInstance method. [Eugen Dueck]
  * Bug fix: NullPointerException, interlaced images, Win32 JRE 1.3.1.
    [Ahmed Lacevic]
  * Bug fix: isGrayscale() method was backwards.
  * Changed default progressive display option to false.
  * Optimized loading of non-interlaced images.
  * Fatal errors are now always available via getErrors().
  * Added isIndexedColor() method.
  * Added TextChunk.getChunkType() method.
  * Added hasChunk(String) method.
  * Added "background low bytes" property.
  * Added "background index" property.
  * Added "transparency" property.
  * Added "transparency low bytes" property.
  * Added "transparency size" property.
  * Added alpha array to "palette" property when transparency info is present.

  Changes from v1.0a -> v1.1 (2001-7-7):

  * Support for gIFg and gIFx chunks.
  * gAMA chunks with a value of zero are ignored. [Michael Toennies]
  * Multiple productions of image data are now supported again (undid
    some of the changes from v0.8 -> v0.9). [Klaus Goessl]
  * File handles are now properly closed after the images are read.
    [Steve Thorne]
  * Added "setFlushAfterNextProduction" method. [Klaus Goessl]


  Changes from v0.9 -> v1.0a (1999-4-1):

  * Released as open-source under LGPL.
  * Workaround for IE4.0, should work fine now.

  Changes from v0.8 -> v0.9 (1999-1-12):

  * Changes to significantly reduce memory usage (up to 40%):
    + "setViewingGammaLocal", "setDisplayGammaLocal" methods removed.
    + Multiple productions of image data no longer supported.
    + Call "getEverything" only when you do not need image data.
  * Support for proposed iTXt chunk (TextChunk class).
  * "getTextChunk" now returns a TextChunk instead of String.
  * "getTextKeys" changed to "getTextChunks".
  * CR, LF, and CR-LF combinations in text chunks are transformed 
    to local newline conventions.
  * Standard text chunks keywords are now case-sensitive.
  * Character codes 0-8,11,12,14-31,127-159 in text chunks are
    displayed using octal "\nnn" notation.

  Changes from v0.7 -> v0.8 (1998-10-13):

  * Support for oFFs, pCAL, sBIT, sCAL, and sPLT chunks.
  * ChunkHandlers can now override standard ancillary chunks.
  * Added documentation on reducing distribution size (see below).
  * Fixed bug with getProperty("background").
  * Fixed bug with getProperty("time").
  * Modest performance improvements.
  * Changed some property names and constants for consistency.
  * Added source files for each example class.

  Changes from v0.6 -> v0.7 (1998-7-28):

  * Added getProperty method for easy access to chunk data.
  * Added ChunkHandler interface for private chunk handling.
  * Support for sRGB and iCCP chunks.
  * Removed PngHeader class. Equivalent methods are now in PngImage.
  * Improved text chunk handling.
  * Changed "setViewingGamma" and "setDisplayGamma" to 
    "setViewingExponent" and "setDisplayExponent" to reflect proposed 
    changes to the PNG specification.
  * Added GrayscaleFilter class (getGrayscaleFilter method removed).
  * Example code documentation.

  Changes from v0.5 -> v0.6 (1998-3-11):

  * Progressive display of interlaced images.
  * Grayscale filtering.
  * "setViewingGammaLocal" and "setDisplayGammaLocal" allow you to
    override the default gamma settings for an image.
  * Added a RemoveAlphaFilter example class for better display
    of transparent images.
  * Fixed a bug with 16-bit grayscale images with bKGD chunks.
  * Fixed a bug with very small interlaced images.
  * Fixed a possible bug with interlaced images in which the first
    scanline of each pass did not use the Sub or None filter.
  * Package has changed from com.sixlegs.PNG to com.sixlegs.image.png;
    this is the final package name.
  * More documentation.

Requirements

  This package uses the java.util.zip package for ZLIB decompression,
  and so requires Java 1.1 or higher. This means that it will not work
  in older browsers, such as Netscape 3.x. Let me know if you encounter
  problems on platforms that supposedly support Java 1.1.

Getting Started

  The entire com.sixlegs.PNG package has been archived in the png.jar
  file. The easiest way to start using the package is to simply include
  the path to the jar file in your CLASSPATH environment variable. 

  Alternatively, you can extract the class files by running the command

      jar xvf png.jar

  You would still probably want to add the location of the extracted
  files to your CLASSPATH.

Using the Package

  Usually you will 'import com.sixlegs.image.png.*;' at the beginning 
  of your code. The PngImage class is an ImageProducer, so getting an 
  Image object from a local PNG file is easy:

      PngImage png = new PngImage("test.png");
      Image img = Toolkit.getDefaultToolkit().createImage(png);

  The PngImage object also has constructors to read from a URL or an
  InputStream. In addition, you can set variables, such as gamma
  parameters, that will affect the resulting image. See the
  documentation located in the 'javadoc' directory for more details.

  Example code is located in the 'examples' directory.

Reducing distribution size

  Not all of the class files in the library are critical. By removing
  unnecessary classes you can reduce the total size of the jar file
  by about half.

  Each chunk type has its own class. The only five that must be 
  included are IHDR, IDAT, IEND, PLTE, and tRNS. All others may be 
  safely removed.

  Be aware that the removal of certain chunks classes, such as gAMA, 
  sRGB, and bKGD, may affect the displayed image.

  Furthermore, there is a separate "BitMover" class for each image 
  color format. You may safely remove the formats that you *know* you 
  do not need to support. Here is a key to the BitMover classes:

    BitMover1G     - 1 bit grayscale
    BitMover1P     - 1 bit paletted
    BitMover2G     - 2 bit grayscale
    BitMover2P     - 2 bit paletted
    BitMover4G     - 4 bit grayscale
    BitMover4P     - 4 bit paletted
    BitMover8G     - 8 bit grayscale
    BitMover8GA    - 8 bit grayscale + 8 bit alpha
    BitMover8P     - 8 bit paletted
    BitMover8RGB   - 24 bit truecolor
    BitMover8RGBA  - 24 bit truecolor + 8 bit alpha
    BitMover16G    - 16 bit grayscale
    BitMover16GA   - 16 bit grayscale + 16 bit alpha
    BitMover16RGB  - 48 bit truecolor
    BitMover16RGBA - 48 bit truecolor + 16 bit alpha

  Miscellaneous:

  * Remove KeyValueChunk only if you remove AbstractTextChunk and iCCP.
  * Remove AbstractTextChunk only if you remove tEXt, zTXt, and iTXt.
  * Remove VcHRM only if you remove sRGB and cHRM.
  * If you do not need private chunk handling you may remove
    UserChunk and ChunkHandler.
  * Remove GifExtension only if you remove gIFx and gIFg.

Contact Information

  The latest version of this package is available from:
  http://www.sixlegs.com/

  Please let me know about any problems you encounter, or features
  that you would find valuable. 

  Chris Nokleberg <chris@sixlegs.com>

