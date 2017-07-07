// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;
import java.io.IOException;

final class Chunk_hIST
extends Chunk
{
    Chunk_hIST()
    {
        super(hIST);
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
        if (img.data.palette == null)
            throw new PngException("hIST chunk must follow PLTE chunk");
        int num = img.data.palette.r.length;
        if (length != num * 2) badLength(num * 2);
        int[] values = new int[num];
        for (int i = 0; i < num; i++) 
            values[i] = in_data.readUnsignedShort();
        img.data.properties.put("histogram", values);
    }
}
