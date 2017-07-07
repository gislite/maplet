// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;
import java.io.IOException;

final class Chunk_sRGB
extends Chunk
{
    Chunk_sRGB()
    {
        super(sRGB);
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
        if (img.data.palette != null)
            throw new PngException("sRGB chunk must precede PLTE chunk");
        if (length != 1) badLength(1);
        int intent = in_data.readUnsignedByte();

        new Chunk_VcHRM(img);
        img.data.properties.put("gamma", new Long(PngImage.DEFAULT_GAMMA));
        img.data.properties.put("srgb rendering intent", new Integer(intent));
    }
}
