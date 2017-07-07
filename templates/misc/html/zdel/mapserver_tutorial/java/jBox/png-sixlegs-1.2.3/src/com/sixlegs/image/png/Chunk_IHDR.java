// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.io.IOException;

final class Chunk_IHDR
extends Chunk
{
    /* package */ int width;
    /* package */ int height;
    /* package */ int depth;
    /* package */ int outputDepth;
    /* package */ int compress;
    /* package */ int filter;
    /* package */ int interlace;
    /* package */ Interlacer interlacer;

    /* package */ int samples = 1;
    /* package */ int colorType;
    /* package */ int cmBits;

    /* package */ boolean paletteUsed = false;
    /* package */ boolean colorUsed = false;
    /* package */ boolean alphaUsed = false;
  
    /* package */ ColorModel alphaModel;
    /* package */ ColorModel model;

    Chunk_IHDR()
    {
        super(IHDR);
    }

    protected boolean multipleOK()
    {
        return false;
    }

    // TODO: shorten
    protected void readData()
    throws IOException
    {
        img.data.header = this; 
        if (length != 13) badLength(13);

        width = in_data.readInt();
        height = in_data.readInt();
        if (width <= 0 || height <= 0) {
            throw new PngException("Bad image size: " + in_data.unsign(width) +
                                   "x" + in_data.unsign(height));
        }

        depth = in_data.readUnsignedByte();
        outputDepth = (depth == 16 ? 8 : depth);

        int blue = 0;
        switch (outputDepth) {
        case 1:  blue = 0x01; break;
        case 2:  blue = 0x03; break;
        case 4:  blue = 0x0F; break;
        case 8:  blue = 0xFF; break;
        default:
            throw new PngException("Bad bit depth: " + depth);
        }

        byte[] sbit = null;

        int green = blue  << outputDepth;
        int red   = green << outputDepth;
        int alpha = red   << outputDepth;
    
        byte b_depth = (byte)depth;

        colorType = in_data.readUnsignedByte();
        switch (colorType) {
        case PngImage.COLOR_TYPE_GRAY: 
            sbit = new byte[]{b_depth, b_depth, b_depth};
            cmBits = 3 * outputDepth;
            break;
        case PngImage.COLOR_TYPE_RGB: 
            sbit = new byte[]{b_depth, b_depth, b_depth};
            cmBits = 3 * outputDepth;
            samples = 3;
            colorUsed = true;
            break;
        case PngImage.COLOR_TYPE_PALETTE: 
            sbit = new byte[]{8, 8, 8};
            cmBits = outputDepth;
            colorUsed = paletteUsed = true;
            break;
        case PngImage.COLOR_TYPE_GRAY_ALPHA: 
            sbit = new byte[]{b_depth, b_depth, b_depth, b_depth};
            cmBits = 4 * outputDepth;
            samples = 2;
            alphaUsed = true; 
            break;
        case PngImage.COLOR_TYPE_RGB_ALPHA: 
            sbit = new byte[]{b_depth, b_depth, b_depth, b_depth};
            cmBits = 4 * outputDepth;
            samples = 4;
            alphaUsed = colorUsed = true; 
            break;
        default:
            cmBits = 0;
            throw new PngException("Bad color type: " + colorType);
        }

        img.data.properties.put("significant bits", sbit);

        if (paletteUsed) {
            // set later when we see the PLTE chunk
        } else {
            if (alphaUsed) {
                model = alphaModel = new DirectColorModel(cmBits, red, green, blue, alpha);
            } else { 
                // we may switch to alphaModel if a tRNS chunk is found later
                alphaModel = ColorModel.getRGBdefault();
                model = new DirectColorModel(24, 0xFF0000, 0x00FF00, 0x0000FF);
            }
        }

        switch (colorType) {
        case PngImage.COLOR_TYPE_GRAY: 
            break;
        case PngImage.COLOR_TYPE_PALETTE:
            if (depth == 16)
                throw new PngException("Bad bit depth for color type " + colorType + ": " + depth);
            break;
        default:
            if (depth <= 4)
                throw new PngException("Bad bit depth for color type " + colorType + ": " + depth);
        }

        if ((compress = in_data.readUnsignedByte()) != PngImage.COMPRESSION_TYPE_BASE) 
            throw new PngException("Unrecognized compression method: " + compress);

        if ((filter = in_data.readUnsignedByte()) != PngImage.FILTER_TYPE_BASE)
            throw new PngException("Unrecognized filter method: " + filter);

        interlace = in_data.readUnsignedByte();
        switch (interlace) {
        case PngImage.INTERLACE_TYPE_NONE:
            interlacer = new NullInterlacer(width, height);
            break;
        case PngImage.INTERLACE_TYPE_ADAM7:
            interlacer = new Adam7Interlacer(width, height);
            break;
        default:
            throw new PngException("Unrecognized interlace method: " + interlace);
        }

        img.data.properties.put("width", new Integer(width));
        img.data.properties.put("height", new Integer(height));
        img.data.properties.put("bit depth", new Integer(depth));
        img.data.properties.put("interlace type", new Integer(interlace));
        img.data.properties.put("compression type", new Integer(compress));
        img.data.properties.put("filter type", new Integer(filter));
        img.data.properties.put("color type", new Integer(colorType));
    }
}
