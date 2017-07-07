// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

import java.awt.Color;
import java.io.IOException;

final class Chunk_tRNS
extends Chunk
{
    /* package */ int rgb;
    /* package */ int rgb_low;
    /* package */ int r, g, b;

    Chunk_tRNS()
    {
        super(tRNS);
    }

    protected boolean multipleOK()
    {
        return false;
    }

    protected boolean beforeIDAT()
    {
        return true;
    }

    protected void readData()
    throws IOException
    {
        int d = img.data.header.outputDepth;
        switch (img.data.header.colorType) {
        case PngImage.COLOR_TYPE_GRAY:
            if (length != 2) badLength(2);
            if (img.data.header.depth == 16) {
                r = g = b = in_data.readUnsignedByte();
                int low = in_data.readUnsignedByte();
                img.data.properties.put("transparency low bytes", new Color(low, low, low));
            } else {
                r = g = b = in_data.readUnsignedShort();
            }
            rgb = r | r << d | r << (d * 2);
            img.data.header.model = img.data.header.alphaModel;
            img.data.properties.put("transparency", new Color(r, g, b));
            break;

        case PngImage.COLOR_TYPE_RGB:
            if (length != 6) badLength(6);
            if (img.data.header.depth == 16) {
                r = in_data.readUnsignedByte();
                int low_r = in_data.readUnsignedByte();
                g = in_data.readUnsignedByte();
                int low_g = in_data.readUnsignedByte();
                b = in_data.readUnsignedByte();
                int low_b = in_data.readUnsignedByte();
                rgb_low = low_b | low_g << 8 | low_r << 16;
                img.data.properties.put("transparency low bytes", new Color(low_r, low_g, low_b));
            } else {
                r = in_data.readUnsignedShort();
                g = in_data.readUnsignedShort();
                b = in_data.readUnsignedShort();
            }
            rgb = b | g << d | r << (d * 2);
            img.data.header.model = img.data.header.alphaModel;
            img.data.properties.put("transparency", new Color(r, g, b));
            break;

        case PngImage.COLOR_TYPE_PALETTE:
            if (img.data.palette == null)
                throw new PngException("tRNS chunk must follow PLTE chunk");
            Chunk_PLTE p = img.data.palette;
            int size = p.r.length;
            if (length > size) badLength();
            p.a_raw = new int[size];
            p.a = new byte[size];
            int i = 0;
            while (i < length) {
                p.a[i] = (byte)(p.a_raw[i] = in_data.readUnsignedByte());
                i++;
            }
            while (i < size) {
                p.a[i] = (byte)(p.a_raw[i] = 0xFF);
                i++;
            }
            img.data.properties.put("transparency size", new Integer(length));
            p.updateProperties(true);
            break;

        default:
            throw new PngException("tRNS prohibited for color type " + img.data.header.colorType);
        }
    }
}
